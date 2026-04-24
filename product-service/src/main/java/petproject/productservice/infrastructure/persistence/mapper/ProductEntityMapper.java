package petproject.productservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import petproject.productservice.domain.model.*;
import petproject.productservice.infrastructure.persistence.model.ProductEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {

    @Mapping(target = "price", source = "price")
    @Mapping(target = "currency", source = "price")
    ProductEntity toEntity(Product product);

//    @Mapping(target = "id", ignore = true)
    void updateEntity(Product product, @MappingTarget ProductEntity entity);

    default Product toDomain(ProductEntity entity) {
        return Product.restore(
                new ProductId(entity.getId()),
                new UserId(entity.getSellerId()),
                entity.getName(),
                entity.getDescription(),
                new CategoryId(entity.getCategoryId()),
                Money.create(entity.getPrice(), entity.getCurrency()),
                entity.getStock(),
                entity.getReserved(),
                ProductStatus.valueOf(entity.getStatus())
        );
    }

    // ID
    default UUID map(ProductId id) { return id.getId(); }
    default UUID map(UserId id) { return id.getId(); }
    default UUID map(CategoryId id) { return id.getId(); }

    // Money
    default BigDecimal map(Money money) { return money.getPrice(); }
    default String mapCurrency(Money money) { return money.getCurrency(); }

    // Enum
    default String map(ProductStatus status) { return status.name(); }





}
