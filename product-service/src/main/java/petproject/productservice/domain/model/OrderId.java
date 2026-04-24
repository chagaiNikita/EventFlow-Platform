package petproject.productservice.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class OrderId {
    private final UUID id;

    public OrderId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderId orderId = (OrderId) o;
        return Objects.equals(id, orderId.id);
    }

    public UUID getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
