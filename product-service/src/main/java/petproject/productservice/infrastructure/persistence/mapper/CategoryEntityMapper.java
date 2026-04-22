package petproject.productservice.infrastructure.persistence.mapper;

import petproject.productservice.domain.model.Category;
import petproject.productservice.domain.model.CategoryId;
import petproject.productservice.infrastructure.persistence.model.CategoryEntity;

public class CategoryEntityMapper {


    public static Category toCategory(CategoryEntity categoryEntity) {
        return Category.restore(new CategoryId(categoryEntity.getId()), categoryEntity.getName());
    }

    public static CategoryEntity toCategoryEntity(Category category) {
        return CategoryEntity.builder()
                .id(category.getId().getId())
                .name(category.getName())
                .build();
    }
}
