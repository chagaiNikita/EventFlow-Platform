package petproject.productservice.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petproject.productservice.infrastructure.persistence.model.ProductEntity;

import java.util.UUID;
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
}
