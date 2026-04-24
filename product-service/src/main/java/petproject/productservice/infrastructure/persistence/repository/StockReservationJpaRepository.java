package petproject.productservice.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petproject.productservice.infrastructure.persistence.model.StockReservationEntity;

import java.util.UUID;
@Repository
public interface StockReservationJpaRepository extends JpaRepository<StockReservationEntity, UUID> {

    boolean existsByOrderId(UUID orderId);
}
