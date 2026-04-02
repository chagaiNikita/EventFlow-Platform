# ТЗ: OrderFlow — Платформа управления заказами (мини-маркетплейс)

## Концепция

**OrderFlow** — упрощённый маркетплейс, где продавцы размещают товары, а покупатели оформляют заказы.
Ключевая сложность — распределённая Saga при оформлении заказа: резерв товара → оплата → передача в доставку.
При любом сбое на любом шаге — автоматический откат через compensating transactions.

---

## Архитектура

```
[Client]
    │
    ▼
[auth-service :8082]   (уже реализован — JWT, регистрация)
    │
    ├── Kafka: user.registered ──────────────────────────────► [user-service :8083]
    │
[product-service :8084] ── Kafka: stock.reserved ──────────► [order-service :8085]
                        ◄─ Kafka: stock.reserve.failed ───────       │
                        ◄─ Kafka: stock.release ───────────────       │
                        ── Kafka: stock.low ──────────────────► [notification-service :8089]
    │
[order-service :8085]  ── Kafka: order.created ────────────► [payment-service :8086]
                       ◄─ Kafka: payment.succeeded ────────────       │
                       ◄─ Kafka: payment.failed ───────────────       │
                       ── Kafka: order.confirmed ──────────────► [shipping-service :8087]
                       ── Kafka: order.cancelled ──────────────► [notification-service :8089]
    │
[shipping-service :8087] ── Kafka: shipment.status.updated ─► [order-service :8085]
                         ── Kafka: shipment.status.updated ──► [notification-service :8089]
    │
[analytics-service :8088] ◄─ Kafka: order.confirmed
                          ◄─ Kafka: payment.succeeded
                          ◄─ Kafka: shipment.status.updated
```

---

## Kafka Topics

| Топик | Producer | Consumer(s) |
|-------|----------|-------------|
| `user.registered` | auth-service | user-service, notification-service |
| `order.created` | order-service | payment-service |
| `order.confirmed` | order-service | shipping-service, analytics-service, notification-service |
| `order.cancelled` | order-service | notification-service |
| `payment.succeeded` | payment-service | order-service |
| `payment.failed` | payment-service | order-service |
| `stock.reserved` | product-service | order-service |
| `stock.reserve.failed` | product-service | order-service |
| `stock.release` | order-service | product-service |
| `stock.low` | product-service | notification-service |
| `shipment.status.updated` | shipping-service | order-service, analytics-service, notification-service |
| `events.dlq` | Spring DLT | — |

Все топики: 3 партиции, replication-factor 1.

---

## Микросервис 1: User-Service (доделать, :8083)

### Что уже есть
- БД: таблицы `users`, `addresses`

### Kafka Consumer
- Топик: `user.registered` → создать профиль пользователя
- Идемпотентность: если userId уже существует — пропустить

### REST API

| Метод | Endpoint | Auth | Описание |
|-------|----------|------|----------|
| GET | `/users/me` | JWT | Свой профиль |
| PUT | `/users/me` | JWT | Обновить имя, фамилию |
| POST | `/users/me/addresses` | JWT | Добавить адрес доставки |
| GET | `/users/me/addresses` | JWT | Список адресов |
| DELETE | `/users/me/addresses/{id}` | JWT | Удалить адрес |

### Бизнес-правила
- Максимум 5 адресов на пользователя
- Нельзя удалить адрес, если он указан в активном заказе

---

## Микросервис 2: Product-Service (новый, :8084)

### Домен
Каталог товаров с управлением остатками. Продавец — любой зарегистрированный пользователь.

### БД Schema

```sql
products (
  id           UUID PRIMARY KEY,
  seller_id    UUID NOT NULL,
  name         VARCHAR(255) NOT NULL,
  description  TEXT,
  category     VARCHAR(100) NOT NULL,
  price        NUMERIC(10,2) NOT NULL,
  currency     VARCHAR(3) DEFAULT 'USD',
  stock        INT NOT NULL DEFAULT 0,      -- фактический остаток
  reserved     INT NOT NULL DEFAULT 0,      -- зарезервировано (в процессе заказа)
  status       VARCHAR(20) NOT NULL,        -- ACTIVE, INACTIVE, OUT_OF_STOCK
  version      BIGINT NOT NULL DEFAULT 0,   -- optimistic locking
  created_at   TIMESTAMP NOT NULL,
  updated_at   TIMESTAMP NOT NULL
)

-- Лог резервирований для idempotency
stock_reservations (
  id           UUID PRIMARY KEY,
  order_id     UUID NOT NULL UNIQUE,        -- уникальность для idempotency
  product_id   UUID NOT NULL,
  quantity     INT NOT NULL,
  status       VARCHAR(20) NOT NULL,        -- RESERVED, RELEASED, CONFIRMED
  created_at   TIMESTAMP NOT NULL
)
```

### REST API

| Метод | Endpoint | Auth | Описание |
|-------|----------|------|----------|
| POST | `/products` | JWT | Добавить товар |
| PUT | `/products/{id}` | JWT (owner) | Обновить товар |
| PUT | `/products/{id}/stock` | JWT (owner) | Пополнить остаток |
| DELETE | `/products/{id}` | JWT (owner) | Снять с продажи (→ INACTIVE) |
| GET | `/products` | — | Каталог (фильтры + пагинация) |
| GET | `/products/{id}` | — | Детали товара |
| GET | `/products/my` | JWT | Мои товары |

**Фильтры GET /products:** `category`, `priceMin`, `priceMax`, `inStock`, `page`, `size`

### Kafka: Consumer

**`stock.release`** → освободить зарезервированный остаток (при отмене заказа):
```json
{ "orderId": "uuid", "productId": "uuid", "quantity": 2 }
```

### Kafka: Producers

**`stock.reserved`** — резерв успешен:
```json
{ "orderId": "uuid", "productId": "uuid", "quantity": 2, "unitPrice": 29.99 }
```

**`stock.reserve.failed`** — недостаточно товара:
```json
{ "orderId": "uuid", "productId": "uuid", "reason": "Insufficient stock" }
```

**`stock.low`** — остаток упал ниже порога (< 5 единиц):
```json
{ "productId": "uuid", "sellerId": "uuid", "productName": "...", "currentStock": 3 }
```

### Бизнес-правила
- `available = stock - reserved` — доступный остаток
- Резервирование: `reserved += quantity` (через optimistic locking)
- Подтверждение заказа: `stock -= quantity`, `reserved -= quantity`
- Отмена заказа: `reserved -= quantity`
- Если `available = 0` → статус `OUT_OF_STOCK` автоматически
- Продавец не может удалить товар с активными резервами

---

## Микросервис 3: Order-Service (новый, :8085)

### Домен
Центральный оркестратор Saga. Управляет жизненным циклом заказа.

### БД Schema

```sql
orders (
  id              UUID PRIMARY KEY,
  buyer_id        UUID NOT NULL,
  product_id      UUID NOT NULL,
  quantity        INT NOT NULL,
  unit_price      NUMERIC(10,2) NOT NULL,
  total_amount    NUMERIC(10,2) NOT NULL,
  delivery_address VARCHAR(500) NOT NULL,
  status          VARCHAR(30) NOT NULL,
  -- статусы: PENDING → STOCK_RESERVED → PAYMENT_PROCESSING →
  --          CONFIRMED → SHIPPED → DELIVERED | CANCELLED
  failure_reason  VARCHAR(255),
  created_at      TIMESTAMP NOT NULL,
  updated_at      TIMESTAMP NOT NULL
)

order_status_history (
  id          UUID PRIMARY KEY,
  order_id    UUID NOT NULL REFERENCES orders(id),
  old_status  VARCHAR(30),
  new_status  VARCHAR(30) NOT NULL,
  reason      VARCHAR(255),
  changed_at  TIMESTAMP NOT NULL
)

-- Outbox Pattern: гарантированная доставка событий
outbox_events (
  id           UUID PRIMARY KEY,
  aggregate_id UUID NOT NULL,       -- order_id
  event_type   VARCHAR(100) NOT NULL,
  payload      JSONB NOT NULL,
  published    BOOLEAN DEFAULT FALSE,
  created_at   TIMESTAMP NOT NULL
)
```

### REST API

| Метод | Endpoint | Auth | Описание |
|-------|----------|------|----------|
| POST | `/orders` | JWT | Создать заказ |
| GET | `/orders/my` | JWT | Мои заказы (пагинация) |
| GET | `/orders/{id}` | JWT | Детали заказа |
| POST | `/orders/{id}/cancel` | JWT (owner) | Отменить заказ |

**POST /orders — тело:**
```json
{
  "productId": "uuid",
  "quantity": 2,
  "deliveryAddress": "ул. Пушкина, д. 10"
}
```

### Saga: Оформление заказа

```
POST /orders
    │
    ▼
1. Проверить продукт существует и ACTIVE (HTTP GET → product-service)
2. Создать order со статусом PENDING
3. Сохранить событие в outbox: order.reserve.stock
   (product-service слушает и резервирует товар)
    │
    ├── stock.reserved → order → STOCK_RESERVED
    │   └── outbox: order.created (payment-service начинает оплату)
    │
    └── stock.reserve.failed → order → CANCELLED (reason: no stock)
        └── outbox: order.cancelled

Оплата:
    ├── payment.succeeded → order → CONFIRMED
    │   └── outbox: order.confirmed (shipping-service берёт в работу)
    │
    └── payment.failed → order → CANCELLED (reason: payment failed)
        └── outbox: stock.release (освободить резерв)
            └── outbox: order.cancelled
```

### Kafka Consumers

| Топик | Действие |
|-------|----------|
| `stock.reserved` | order → STOCK_RESERVED, запустить оплату |
| `stock.reserve.failed` | order → CANCELLED |
| `payment.succeeded` | order → CONFIRMED, запустить доставку |
| `payment.failed` | order → CANCELLED, освободить резерв |
| `shipment.status.updated` | обновить статус order (SHIPPED / DELIVERED) |

### Outbox Processor
- `@Scheduled` каждые 5 секунд: найти unpublished события → опубликовать в Kafka → пометить published
- Гарантирует at-least-once доставку даже при падении сервиса

### Kafka Producers (через outbox)

**`order.created`**:
```json
{
  "orderId": "uuid",
  "buyerId": "uuid",
  "productId": "uuid",
  "quantity": 2,
  "totalAmount": 59.98
}
```

**`order.confirmed`**:
```json
{
  "orderId": "uuid",
  "buyerId": "uuid",
  "productId": "uuid",
  "deliveryAddress": "...",
  "totalAmount": 59.98
}
```

**`order.cancelled`**:
```json
{
  "orderId": "uuid",
  "buyerId": "uuid",
  "reason": "...",
  "cancelledAt": "ISO-8601"
}
```

**`stock.release`**:
```json
{ "orderId": "uuid", "productId": "uuid", "quantity": 2 }
```

### Бизнес-правила
- Нельзя отменить заказ в статусе SHIPPED или DELIVERED
- Максимум 10 единиц одного товара в заказе
- Один пользователь не может иметь более 3 PENDING заказов одновременно
- Все смены статуса пишутся в `order_status_history`

---

## Микросервис 4: Payment-Service (новый, :8086)

### Домен
Имитирует платёжный шлюз. Хранит историю транзакций.

### БД Schema

```sql
payment_accounts (
  id         UUID PRIMARY KEY,
  user_id    UUID NOT NULL UNIQUE,
  balance    NUMERIC(10,2) NOT NULL DEFAULT 1000.00,  -- стартовый баланс для теста
  currency   VARCHAR(3) DEFAULT 'USD',
  updated_at TIMESTAMP NOT NULL
)

transactions (
  id         UUID PRIMARY KEY,
  order_id   UUID NOT NULL UNIQUE,    -- idempotency key
  user_id    UUID NOT NULL,
  amount     NUMERIC(10,2) NOT NULL,
  type       VARCHAR(20) NOT NULL,    -- CHARGE, REFUND
  status     VARCHAR(20) NOT NULL,    -- SUCCESS, FAILED
  reason     VARCHAR(255),
  created_at TIMESTAMP NOT NULL
)
```

### Kafka Consumer

**`order.created`** → попытаться списать средства:
1. Проверить idempotency: если транзакция с этим orderId уже есть → пропустить
2. Найти payment_account пользователя (или создать с балансом 1000.00)
3. Если balance >= amount → списать, status SUCCESS
4. Если balance < amount → status FAILED

### Kafka Producers

**`payment.succeeded`**:
```json
{ "orderId": "uuid", "userId": "uuid", "amount": 59.98, "transactionId": "uuid" }
```

**`payment.failed`**:
```json
{ "orderId": "uuid", "userId": "uuid", "reason": "Insufficient funds" }
```

### REST API

| Метод | Endpoint | Auth | Описание |
|-------|----------|------|----------|
| GET | `/payments/my` | JWT | История транзакций |
| GET | `/payments/balance` | JWT | Текущий баланс |
| POST | `/payments/topup` | JWT | Пополнить баланс (для тестов) |

---

## Микросервис 5: Shipping-Service (новый, :8087)

### Домен
Управляет доставкой подтверждённых заказов. Имитирует смену статусов через scheduler.

### БД Schema

```sql
shipments (
  id            UUID PRIMARY KEY,
  order_id      UUID NOT NULL UNIQUE,
  buyer_id      UUID NOT NULL,
  address       VARCHAR(500) NOT NULL,
  status        VARCHAR(20) NOT NULL,   -- PREPARING, IN_TRANSIT, DELIVERED
  tracking_code VARCHAR(50) NOT NULL,
  created_at    TIMESTAMP NOT NULL,
  updated_at    TIMESTAMP NOT NULL
)
```

### Kafka Consumer

**`order.confirmed`** → создать shipment со статусом PREPARING, сгенерировать tracking_code

### Kafka Producer

**`shipment.status.updated`**:
```json
{
  "orderId": "uuid",
  "shipmentId": "uuid",
  "trackingCode": "TRK-XXXXXX",
  "oldStatus": "PREPARING",
  "newStatus": "IN_TRANSIT",
  "updatedAt": "ISO-8601"
}
```

### Scheduler (имитация доставки)
- Каждые 30 секунд: случайно продвигать shipments по статусам
  - PREPARING → IN_TRANSIT (через ~1 мин)
  - IN_TRANSIT → DELIVERED (через ~2 мин)
- При каждом переходе публиковать `shipment.status.updated`

### REST API

| Метод | Endpoint | Auth | Описание |
|-------|----------|------|----------|
| GET | `/shipments/my` | JWT | Мои доставки |
| GET | `/shipments/{trackingCode}` | — | Трекинг по коду |

---

## Микросервис 6: Analytics-Service (новый, :8088)

### Домен
Агрегирует бизнес-метрики в реальном времени. Только чтение — никогда не пишет в другие сервисы.

### БД Schema

```sql
daily_sales (
  id             UUID PRIMARY KEY,
  date           DATE NOT NULL,
  product_id     UUID NOT NULL,
  total_orders   INT NOT NULL DEFAULT 0,
  total_quantity INT NOT NULL DEFAULT 0,
  total_revenue  NUMERIC(12,2) NOT NULL DEFAULT 0,
  UNIQUE (date, product_id)
)

seller_stats (
  id             UUID PRIMARY KEY,
  seller_id      UUID NOT NULL UNIQUE,
  total_orders   INT NOT NULL DEFAULT 0,
  total_revenue  NUMERIC(12,2) NOT NULL DEFAULT 0,
  updated_at     TIMESTAMP NOT NULL
)
```

### Kafka Consumers

| Топик | Действие |
|-------|----------|
| `order.confirmed` | обновить daily_sales и seller_stats через upsert |
| `payment.succeeded` | дополнительная валидация revenue |

### REST API

| Метод | Endpoint | Auth | Описание |
|-------|----------|------|----------|
| GET | `/analytics/products/top` | JWT | Топ-10 продаваемых товаров |
| GET | `/analytics/products/{id}/stats` | JWT (owner) | Статистика по товару |
| GET | `/analytics/sellers/{id}/stats` | JWT (owner) | Статистика продавца |
| GET | `/analytics/summary` | JWT | Общая сводка за период |

---

## Микросервис 7: Notification-Service (новый, :8089)

### БД Schema

```sql
notifications (
  id         UUID PRIMARY KEY,
  user_id    UUID NOT NULL,
  type       VARCHAR(50) NOT NULL,
  subject    VARCHAR(255) NOT NULL,
  body       TEXT NOT NULL,
  status     VARCHAR(20) NOT NULL,   -- SENT, FAILED
  created_at TIMESTAMP NOT NULL
)
```

### Kafka Consumers + шаблоны уведомлений

| Топик | Кому | Тема |
|-------|------|------|
| `user.registered` | покупатель | "Добро пожаловать в OrderFlow!" |
| `order.confirmed` | покупатель | "Заказ #{orderId} подтверждён" |
| `order.cancelled` | покупатель | "Заказ #{orderId} отменён: {reason}" |
| `payment.failed` | покупатель | "Не удалось оплатить заказ" |
| `shipment.status.updated` | покупатель | "Ваш заказ {status}" |
| `stock.low` | продавец | "Товар '{name}' заканчивается: {stock} шт." |

### Особенности
- `@RetryableTopic`: 3 попытки с backoff 1s/2s/4s, затем DLT `events.dlq`
- Логировать "отправку": `log.info("[EMAIL] To: {} | Subject: {} | Body: {}", userId, subject, body)`

---

## Общие технические требования

### Security
- JWT claims: `userId`, `email`
- Скопировать `JwtFilter` + `JwtService` из auth-service в каждый сервис
- Открытые эндпоинты: `GET /products`, `GET /products/{id}`, `GET /shipments/{trackingCode}`

### Inter-Service HTTP (RestClient)
- order-service → product-service: проверка товара при создании заказа
- Таймаут 3с, 1 retry

### Databases
| Сервис | БД |
|--------|-----|
| user-service | user_db (уже есть) |
| product-service | product_db |
| order-service | order_db |
| payment-service | payment_db |
| shipping-service | shipping_db |
| analytics-service | analytics_db |
| notification-service | notification_db |

### Docker Compose
Добавить все сервисы по образцу auth-service. Порты: 8083–8089.

---

## Порядок реализации

1. **User-Service** — Kafka consumer + REST (разминка, всё знакомо)
2. **Product-Service** — каталог + управление остатками + Kafka producer/consumer
3. **Order-Service** — Saga + Outbox Pattern (самое сложное, делать в последнюю очередь логики)
4. **Payment-Service** — имитация оплаты, idempotency
5. **Shipping-Service** — scheduler + трекинг
6. **Notification-Service** — retry + DLT
7. **Analytics-Service** — агрегации, upsert

---

## Что продемонстрирует проект

| Паттерн | Где |
|---------|-----|
| **Saga (Choreography)** | order ↔ product ↔ payment ↔ shipping |
| **Outbox Pattern** | order-service: гарантированная публикация событий |
| **Compensating Transaction** | откат резерва при ошибке оплаты |
| **Idempotent Consumer** | payment-service: дедупликация по orderId |
| **Optimistic Locking** | product-service: конкурентное резервирование остатков |
| **Dead Letter Topic** | notification-service: retry + DLT |
| **CQRS (лёгкий)** | analytics-service: только читает события, отдельная модель |
| **Scheduler** | shipping-service: продвижение статусов доставки |
| **State Machine** | order статусы с историей переходов |