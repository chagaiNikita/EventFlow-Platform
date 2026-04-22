package petproject.productservice.domain.repository;

import petproject.productservice.domain.model.Category;
import petproject.productservice.domain.model.CategoryId;

import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();

    Category save(Category category);

    Category findById(CategoryId categoryId);

    boolean existCategoryByName(String name);
}
