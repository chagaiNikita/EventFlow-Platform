package petproject.productservice.domain.repository;

import petproject.productservice.domain.model.Product;
import petproject.productservice.domain.model.ProductId;
import petproject.productservice.domain.model.UserId;

import java.util.List;

public interface ProductRepository {
    Product save(Product product);

    Product findById(ProductId productId);

    List<Product> findAllProductsBySellerId(UserId userId);
}
