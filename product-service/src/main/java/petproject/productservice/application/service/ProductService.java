package petproject.productservice.application.service;

import petproject.productservice.application.command.CreateProductCommand;
import petproject.productservice.domain.model.Product;

public interface ProductService {
    Product createProduct(CreateProductCommand createProductCommand);
}
