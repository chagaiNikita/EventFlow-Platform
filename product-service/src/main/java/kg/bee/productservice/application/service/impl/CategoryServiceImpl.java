package kg.bee.productservice.application.service.impl;

import kg.bee.productservice.application.service.CategoryService;
import kg.bee.productservice.domain.exception.CategoryAlreadyExistException;
import kg.bee.productservice.domain.model.Category;
import kg.bee.productservice.domain.model.CategoryId;
import kg.bee.productservice.domain.repository.CategoryRepository;
import kg.bee.productservice.interfaces.api.dto.CategoryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(String name) {
        if (categoryRepository.existCategoryByName(name)) {
            throw new CategoryAlreadyExistException();
        }
        Category category = Category.create(name);

        return categoryRepository.save(category);
    }

    @Override
    public Category changeCategoryName(CategoryId categoryId, String name) {
        if (categoryRepository.existCategoryByName(name)) {
            throw new CategoryAlreadyExistException();
        }
        Category category = categoryRepository.findById(categoryId);

        category.changeName(name);

        return categoryRepository.save(category);
    }
}
