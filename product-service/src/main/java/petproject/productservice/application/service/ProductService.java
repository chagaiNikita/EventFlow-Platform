package petproject.productservice.application.service;

import org.apache.logging.log4j.simple.internal.SimpleProvider;
import petproject.productservice.application.command.CreateProductCommand;
import petproject.productservice.domain.model.Product;
import petproject.productservice.domain.model.UserId;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductCommand createProductCommand);

    List<Product> findProductsBySeller(UserId userId);
}
