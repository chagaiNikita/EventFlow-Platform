package petproject.productservice.domain.model;

import java.util.UUID;

public final class CategoryId {
    private final UUID id;

    public CategoryId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
