package petproject.productservice.domain.model;

import java.util.UUID;

public final class ProductId {
    private final UUID id;


    public ProductId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
