package petproject.userservice.infrastructure.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "addresses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}