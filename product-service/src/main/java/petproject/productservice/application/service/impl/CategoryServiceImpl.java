package petproject.productservice.application.service.impl;

import petproject.productservice.application.service.CategoryService;
import petproject.productservice.domain.exception.CategoryAlreadyExistException;
import petproject.productservice.domain.model.Category;
import petproject.productservice.domain.model.CategoryId;
import petproject.productservice.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
