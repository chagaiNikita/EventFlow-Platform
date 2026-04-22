package kg.bee.productservice.application.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kg.bee.productservice.domain.model.Category;
import kg.bee.productservice.domain.model.CategoryId;
import kg.bee.productservice.interfaces.api.dto.CategoryRequestDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> getAllCategories();

    Category createCategory(String name);

    Category changeCategoryName(CategoryId categoryId, String name);
}
