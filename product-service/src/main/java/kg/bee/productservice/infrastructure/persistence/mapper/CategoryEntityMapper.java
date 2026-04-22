package kg.bee.productservice.infrastructure.persistence.mapper;

import kg.bee.productservice.domain.model.Category;
import kg.bee.productservice.domain.model.CategoryId;
import kg.bee.productservice.infrastructure.persistence.model.CategoryEntity;

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
