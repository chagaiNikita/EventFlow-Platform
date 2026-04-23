package petproject.productservice.domain.repository;

import petproject.productservice.domain.model.Product;

public interface ProductRepository {
    Product save(Product product);
}
