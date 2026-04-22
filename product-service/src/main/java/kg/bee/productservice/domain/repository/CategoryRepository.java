package kg.bee.productservice.domain.repository;

import kg.bee.productservice.domain.model.Category;
import kg.bee.productservice.domain.model.CategoryId;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository {
    List<Category> findAll();

    Category save(Category category);

    Category findById(CategoryId categoryId);

    boolean existCategoryByName(String name);
}
