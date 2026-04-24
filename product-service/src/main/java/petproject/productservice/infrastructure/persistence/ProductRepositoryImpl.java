package petproject.productservice.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import petproject.productservice.domain.exception.ProductNotFoundException;
import petproject.productservice.domain.model.Product;
import petproject.productservice.domain.model.ProductId;
import petproject.productservice.domain.model.UserId;
import petproject.productservice.domain.repository.ProductRepository;
import petproject.productservice.infrastructure.persistence.mapper.ProductEntityMapper;
import petproject.productservice.infrastructure.persistence.model.ProductEntity;
import petproject.productservice.infrastructure.persistence.repository.ProductJpaRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = productJpaRepository.findById(product.getId().getId())
                .map(existing -> {
                    productEntityMapper.updateEntity(product, existing);
                    return existing;
                })
                .orElseGet(() -> productEntityMapper.toEntity(product));

        return productEntityMapper.toDomain(productJpaRepository.save(entity));
    }

    @Override
    public Product findById(ProductId productId) {
        ProductEntity productEntity = productJpaRepository.findById(productId.getId()).orElseThrow(ProductNotFoundException::new);
        return productEntityMapper.toDomain(productEntity);
    }

    @Override
    public List<Product> findAllProductsBySellerId(UserId userId) {
        List<ProductEntity> productEntities = productJpaRepository.findAllBySellerId(userId.getId());
        return productEntities.stream()
                .map(productEntityMapper::toDomain)
                .toList();
    }
}
