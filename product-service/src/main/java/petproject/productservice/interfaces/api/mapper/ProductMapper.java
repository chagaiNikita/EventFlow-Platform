package petproject.productservice.interfaces.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import petproject.productservice.application.command.CreateProductCommand;
import petproject.productservice.application.command.UpStockProductCommand;
import petproject.productservice.application.command.UpdateProductCommand;
import petproject.productservice.domain.model.*;
import petproject.productservice.interfaces.api.dto.CreateProductRequestDto;
import petproject.productservice.interfaces.api.dto.ProductResponseDto;
import petproject.productservice.interfaces.api.dto.UpStockRequestDto;
import petproject.productservice.interfaces.api.dto.UpdateProductRequestDto;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    CreateProductCommand fromDtoToCommand(CreateProductRequestDto createProductRequestDto, UUID userId);

    UpdateProductCommand fromDtoToCommand(UpdateProductRequestDto updateProductRequestDto, UUID userId, UUID productId);

    UpStockProductCommand fromDtoToCommand(UpStockRequestDto upStockRequestDto, UUID userId, UUID productId);




    @Mapping(target = "price", source = "price")
    @Mapping(target = "currency", source = "price")
    ProductResponseDto toProductResponseDto(Product product);


    // ID
    default UUID map(ProductId id) { return id.getId(); }
    default UUID map(UserId id) { return id.getId(); }
    default UUID map(CategoryId id) { return id.getId(); }
    default UUID map(String string) {return UUID.fromString(string);}

    // Money
    default BigDecimal map(Money money) { return money.getPrice(); }
    default String mapCurrency(Money money) { return money.getCurrency(); }


}
