package petproject.userservice.domain.model;


import java.util.Objects;
import java.util.UUID;

public final class AddressId {
    private final UUID id;

    public AddressId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AddressId addressId = (AddressId) o;
        return Objects.equals(id, addressId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
