package petproject.userservice.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petproject.userservice.domain.exception.UserAlreadyExistException;
import petproject.userservice.domain.model.User;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.domain.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(UserId userId, String email) {
        boolean existUserByEmail = userRepository.existUserByEmail(email);

        if (!existUserByEmail) {
            return userRepository.save(User.create(userId, email));
        } else {
            throw new UserAlreadyExistException();
        }
    }

    public User findUserById(UserId userId) {
        return userRepository.findById(userId);
    }
}
