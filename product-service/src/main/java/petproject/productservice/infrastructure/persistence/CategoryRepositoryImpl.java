package petproject.productservice.infrastructure.persistence;

import petproject.productservice.domain.exception.CategoryNotFoundException;
import petproject.productservice.domain.model.Category;
import petproject.productservice.domain.model.CategoryId;
import petproject.productservice.domain.repository.CategoryRepository;
import petproject.productservice.infrastructure.persistence.mapper.CategoryEntityMapper;
import petproject.productservice.infrastructure.persistence.model.CategoryEntity;
import petproject.productservice.infrastructure.persistence.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<Category> findAll() {
        List<CategoryEntity> categoryEntities = categoryJpaRepository.findAll();
        return categoryEntities.stream()
                .map(CategoryEntityMapper::toCategory)
                .toList();
    }

    @Override
    public Category save(Category category) {
        CategoryEntity categoryEntity = CategoryEntityMapper.toCategoryEntity(category);

        return CategoryEntityMapper.toCategory(categoryJpaRepository.save(categoryEntity));
    }

    @Override
    public Category findById(CategoryId categoryId) {
        return CategoryEntityMapper.toCategory(
                categoryJpaRepository.findById(categoryId.getId())
                .orElseThrow(CategoryNotFoundException::new)
        );
    }

    @Override
    public boolean existCategoryByName(String name) {
        return categoryJpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existCategoryById(CategoryId categoryId) {
        return categoryJpaRepository.existsById(categoryId.getId());
    }
}
