package petproject.productservice.domain.repository;

import petproject.productservice.domain.model.StockReservation;

import java.util.UUID;

public interface StockReservationRepository {
    boolean existsByOrderId(UUID orderId);

    StockReservation save(StockReservation stockReservation);
}
