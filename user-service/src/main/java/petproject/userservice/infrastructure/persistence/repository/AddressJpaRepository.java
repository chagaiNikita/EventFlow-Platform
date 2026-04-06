package petproject.userservice.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petproject.userservice.infrastructure.persistence.model.AddressEntity;

import java.util.UUID;
@Repository
public interface AddressJpaRepository extends JpaRepository<AddressEntity, UUID> {
}
