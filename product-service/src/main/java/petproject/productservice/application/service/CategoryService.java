package petproject.productservice.application.service;

import petproject.productservice.domain.model.Category;
import petproject.productservice.domain.model.CategoryId;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category findCategoryById(CategoryId categoryId);

    Category createCategory(String name);

    Category changeCategoryName(CategoryId categoryId, String name);
}
