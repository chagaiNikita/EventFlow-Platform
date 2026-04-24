package petproject.productservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import petproject.productservice.domain.model.*;
import petproject.productservice.infrastructure.persistence.model.StockReservationEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface StockReservationEntityMapper {


    void updateEntity(StockReservation stockReservation, @MappingTarget StockReservationEntity stockReservationEntity);




    StockReservationEntity toEntity(StockReservation stockReservation);

    default StockReservation toDomain(StockReservationEntity stockReservation) {
        return StockReservation.restoreReservation(
                new StockReservationId(stockReservation.getId()),
                new OrderId(stockReservation.getOrderId()),
                new ProductId(stockReservation.getProductId()),
                stockReservation.getQuantity(),
                StockReservationStatus.valueOf(stockReservation.getStatus())
        );
    }



    // ID
    default UUID map(ProductId id) { return id.getId(); }
    default UUID map(OrderId id) { return id.getId(); }
    default UUID map(StockReservationId id) { return id.getId(); }

    // Enum
    default String map(StockReservationStatus status) { return status.name(); }
}
