package petproject.userservice.domain.model;

public final class Address {
    private final String address;

    private Address(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public static Address create(String address) {
        return new Address(address);
    }
}
