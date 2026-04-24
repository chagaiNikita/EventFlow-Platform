package petproject.productservice.domain.model;

import java.util.UUID;

public class StockReservation {
    private final StockReservationId id;
    private final OrderId orderId;
    private final ProductId productId;
    private final Integer quantity;
    private StockReservationStatus status;

    private StockReservation(StockReservationId id, OrderId orderId, ProductId productId, Integer quantity, StockReservationStatus status) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    public StockReservationId getId() {
        return id;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public StockReservationStatus getStatus() {
        return status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public static StockReservation createReservation(OrderId orderId, ProductId productId, Integer quantity) {
        return new StockReservation(new StockReservationId(UUID.randomUUID()), orderId, productId, quantity, StockReservationStatus.RESERVED);
    }

    public static StockReservation restoreReservation(StockReservationId stockReservationId, OrderId orderId, ProductId productId, Integer quantity, StockReservationStatus status) {
        return new StockReservation(stockReservationId, orderId, productId, quantity, status);
    }


}
