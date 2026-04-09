package petproject.userservice.domain.repository;

import petproject.userservice.domain.model.User;
import petproject.userservice.domain.model.UserId;

import java.util.UUID;

public interface UserRepository {
    boolean existUserByEmail(String email);

    User save(User user);

    User findById(UserId id);
}
