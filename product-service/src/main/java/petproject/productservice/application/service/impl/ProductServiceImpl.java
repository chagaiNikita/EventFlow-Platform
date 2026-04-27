package petproject.productservice.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petproject.productservice.application.command.CreateProductCommand;
import petproject.productservice.application.command.UpStockProductCommand;
import petproject.productservice.application.command.UpdateProductCommand;
import petproject.productservice.application.service.ProductService;
import petproject.productservice.domain.exception.CategoryNotFoundException;
import petproject.productservice.domain.exception.NoAvailableItemsException;
import petproject.productservice.domain.model.*;
import petproject.productservice.domain.publisher.StockEventPublisher;
import petproject.productservice.domain.repository.CategoryRepository;
import petproject.productservice.domain.repository.ProductRepository;
import petproject.productservice.domain.repository.StockReservationRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StockReservationRepository stockReservationRepository;
    private final StockEventPublisher stockEventPublisher;

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
    public Product updateProduct(UpdateProductCommand updateProductCommand) {
        Product product = productRepository.findById(new ProductId(updateProductCommand.productId()));
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

    @Override
    @Transactional
    public Product upStockProduct(UpStockProductCommand upStockProductCommand) {
        Product product = productRepository.findById(new ProductId(upStockProductCommand.productId()));

        product.upTheStock(new UserId(upStockProductCommand.userId()), upStockProductCommand.amount());

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void removeProductFromTheSale(UUID productId, UUID userId) {
        Product product = productRepository.findById(new ProductId(productId));

        product.removeFromSale(new UserId(userId));

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void reserveProduct(UUID productId, UUID orderId, int amount) {
        if (!stockReservationRepository.existsByOrderId(orderId)) {
            try {
                Product product = productRepository.findById(new ProductId(productId));
                product.reserveItems(amount);
                StockReservation stockReservation = StockReservation.createReservation(
                        new OrderId(orderId),
                        new ProductId(productId),
                        amount
                );
                productRepository.save(product);
                stockReservationRepository.save(stockReservation);
                stockEventPublisher.publishReserved(productId, orderId, amount);

            } catch (Exception e) {
                log.warn(e.getMessage());
                stockEventPublisher.publishReserveFailed(productId, orderId);
            }


        }
    }
}
