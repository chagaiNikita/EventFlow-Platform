package petproject.userservice.domain.repository;

import petproject.userservice.domain.model.User;

public interface UserRepository {
    boolean existUserByEmail(String email);

    User save(User user);
}
