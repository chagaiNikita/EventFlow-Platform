package petproject.productservice.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petproject.productservice.application.command.CreateProductCommand;
import petproject.productservice.application.service.CategoryService;
import petproject.productservice.application.service.ProductService;
import petproject.productservice.domain.model.*;
import petproject.productservice.domain.repository.CategoryRepository;
import petproject.productservice.domain.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
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
}
