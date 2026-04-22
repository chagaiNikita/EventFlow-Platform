package petproject.productservice.interfaces.api.mapper;

import petproject.productservice.domain.model.Category;
import petproject.productservice.interfaces.api.dto.CategoryResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "id.id", target = "id")
    CategoryResponseDto categoryToCategoryDto(Category category);
}
