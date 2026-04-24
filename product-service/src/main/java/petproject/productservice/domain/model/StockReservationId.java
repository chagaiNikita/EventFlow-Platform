package petproject.productservice.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class StockReservationId {
    private final UUID id;

    public StockReservationId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockReservationId that = (StockReservationId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
