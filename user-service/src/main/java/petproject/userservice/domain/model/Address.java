package petproject.userservice.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Address {
    private final AddressId id;
    private final String address;

    private Address(AddressId id, String address) {
        this.id = id;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public AddressId getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static Address create(String address) {
        return new Address(new AddressId(UUID.randomUUID()), address);
    }

    public static Address restore(AddressId id, String address) {
        return new Address(id, address);
    }
}
