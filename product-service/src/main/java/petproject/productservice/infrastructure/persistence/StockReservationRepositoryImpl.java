package petproject.productservice.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import petproject.productservice.domain.model.StockReservation;
import petproject.productservice.domain.repository.StockReservationRepository;
import petproject.productservice.infrastructure.persistence.mapper.StockReservationEntityMapper;
import petproject.productservice.infrastructure.persistence.model.ProductEntity;
import petproject.productservice.infrastructure.persistence.model.StockReservationEntity;
import petproject.productservice.infrastructure.persistence.repository.StockReservationJpaRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StockReservationRepositoryImpl implements StockReservationRepository {
    private final StockReservationJpaRepository stockReservationJpaRepository;
    private final StockReservationEntityMapper stockReservationEntityMapper;

    @Override
    public boolean existsByOrderId(UUID orderId) {
        return stockReservationJpaRepository.existsByOrderId(orderId);
    }

    @Override
    public StockReservation save(StockReservation stockReservation) {
        StockReservationEntity entity = stockReservationJpaRepository.findById(stockReservation.getId().getId())
                .map(existing -> {
                    stockReservationEntityMapper.updateEntity(stockReservation, existing);
                    return existing;
                })
                .orElseGet(() -> stockReservationEntityMapper.toEntity(stockReservation));

        return stockReservationEntityMapper.toDomain(stockReservationJpaRepository.save(entity));

    }
}
