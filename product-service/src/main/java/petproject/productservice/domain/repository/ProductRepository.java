package petproject.productservice.domain.repository;

import petproject.productservice.domain.model.Product;
import petproject.productservice.domain.model.UserId;

import java.util.List;

public interface ProductRepository {
    Product save(Product product);

    List<Product> findAllProductsBySellerId(UserId userId);
}
