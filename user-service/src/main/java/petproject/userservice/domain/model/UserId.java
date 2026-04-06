package petproject.userservice.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class UserId {
    private final UUID id;

    public UserId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(id, userId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public UUID getId() {
        return id;
    }
}
