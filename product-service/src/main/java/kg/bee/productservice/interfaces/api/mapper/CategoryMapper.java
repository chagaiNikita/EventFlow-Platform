package kg.bee.productservice.interfaces.api.mapper;

import kg.bee.productservice.domain.model.Category;
import kg.bee.productservice.interfaces.api.dto.CategoryResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "id.id", target = "id")
    CategoryResponseDto categoryToCategoryDto(Category category);
}
