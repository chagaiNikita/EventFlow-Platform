package petproject.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import petproject.authservice.model.Outbox;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, UUID> {

    @Query("""
                SELECT m FROM Outbox m
                WHERE m.processed = false
                ORDER BY m.createdAt
                LIMIT :batchSize
            """)
    List<Outbox> findPendingMessages(int batchSize);

    @Modifying
    @Query("UPDATE Outbox m SET m.processed = true WHERE m.id = :id")
    @Transactional
    void markAsProcessed(UUID id);
}
