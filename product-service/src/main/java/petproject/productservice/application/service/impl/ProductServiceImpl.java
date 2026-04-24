package petproject.productservice.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petproject.productservice.application.command.CreateProductCommand;
import petproject.productservice.application.command.UpdateProductCommand;
import petproject.productservice.application.service.ProductService;
import petproject.productservice.domain.exception.CategoryNotFoundException;
import petproject.productservice.domain.model.*;
import petproject.productservice.domain.repository.CategoryRepository;
import petproject.productservice.domain.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Product createProduct(CreateProductCommand createProductCommand) {
        Category category = categoryRepository.findById(new CategoryId(createProductCommand.categoryId()));
        Product product = Product.create(
                new UserId(createProductCommand.userId()),
                createProductCommand.name(),
                createProductCommand.description(),
                category.getId(),
                Money.create(BigDecimal.valueOf(createProductCommand.price()), createProductCommand.currency()),
                createProductCommand.stock()
        );

        return productRepository.save(product);
    }

    @Override
    public List<Product> findProductsBySeller(UserId userId) {
        return productRepository.findAllProductsBySellerId(userId);
    }

    @Override
    @Transactional
    public Product updateProduct(UUID productId, UpdateProductCommand updateProductCommand) {
        Product product = productRepository.findById(new ProductId(productId));
        CategoryId categoryId = new CategoryId(updateProductCommand.categoryId());

        if (!categoryRepository.existCategoryById(categoryId)) {
            throw new CategoryNotFoundException();
        }

        product.authorUpdate(
                new UserId(updateProductCommand.userId()),
                updateProductCommand.name(),
                updateProductCommand.description(),
                categoryId,
                Money.create(BigDecimal.valueOf(updateProductCommand.price()), updateProductCommand.currency())

        );

        return productRepository.save(product);
    }
}
