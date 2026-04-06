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
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private User(UserId id, String email, String firstName,
                 String lastName, List<Address> addresses,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addresses = new ArrayList<>(addresses);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(UserId userId, String email, String firstName, String lastName) {
        return new User(
                userId,
                email,
                firstName,
                lastName,
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static User create(UserId userId, String email) {
        return new User(
                userId,
                email,
                null,
                null,
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static User restore(UserId id, String email, String firstName,
                               String lastName, List<Address> addresses,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new User(id, email, firstName, lastName, addresses, createdAt, updatedAt);
    }

    // бизнес-методы
    public void addAddress(Address address) {
        addresses.add(address);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEmail(String newEmail) {
        if (newEmail == null || newEmail.isBlank())
            throw new IllegalArgumentException("Email cannot be blank");
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
    }

    public UserId getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public List<Address> getAddresses() { return List.copyOf(addresses); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

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