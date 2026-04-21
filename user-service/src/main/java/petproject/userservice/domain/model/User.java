package petproject.userservice.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class User {
    private final UserId id;
    private String email;
    private String firstName;
    private String lastName;
    private final List<Address> addresses;

    private User(UserId id, String email, String firstName,
                 String lastName, List<Address> addresses) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addresses = new ArrayList<>(addresses);
    }

    public static User create(UserId userId, String email, String firstName, String lastName) {
        return new User(
                userId,
                email,
                firstName,
                lastName,
                new ArrayList<>()
        );
    }

    public static User create(UserId userId, String email) {
        return new User(
                userId,
                email,
                null,
                null,
                new ArrayList<>()
        );
    }

    public static User restore(UserId id, String email, String firstName,
                               String lastName, List<Address> addresses,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new User(id, email, firstName, lastName, addresses);
    }

    // бизнес-методы
    public void addAddress(Address address) {
        if (addresses.size() >= 5) {
            throw new IllegalArgumentException("Max address count is 5");
        }
        addresses.add(address);
    }

    public void removeAddress(AddressId addressId) {
        addresses.stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .ifPresentOrElse(
                        addresses::remove,
                        () -> { throw new IllegalArgumentException("Address not found"); }
                );
    }

    public void changeName(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be blank");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be blank");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void updateEmail(String newEmail) {
        if (newEmail == null || newEmail.isBlank())
            throw new IllegalArgumentException("Email cannot be blank");
        this.email = newEmail;
    }

    public UserId getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public List<Address> getAddresses() { return List.copyOf(addresses); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}