create table products
(
    id          UUID PRIMARY KEY,
    seller_id   UUID           NOT NULL,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    category    VARCHAR(100)   NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    currency    VARCHAR(3)              DEFAULT 'USD',
    stock       INT            NOT NULL DEFAULT 0, -- фактический остаток
    reserved    INT            NOT NULL DEFAULT 0, -- зарезервировано (в процессе заказа)
    status      VARCHAR(20)    NOT NULL,           -- ACTIVE, INACTIVE, OUT_OF_STOCK
    version     BIGINT         NOT NULL DEFAULT 0, -- optimistic locking
    created_at  TIMESTAMP      NOT NULL,
    updated_at  TIMESTAMP      NOT NULL
);

-- Лог резервирований для idempotency
create table stock_reservations
(
    id         UUID PRIMARY KEY,
    order_id   UUID        NOT NULL UNIQUE, -- уникальность для idempotency
    product_id UUID        NOT NULL,
    quantity   INT         NOT NULL,
    status     VARCHAR(20) NOT NULL,        -- RESERVED, RELEASED, CONFIRMED
    created_at TIMESTAMP   NOT NULL
)