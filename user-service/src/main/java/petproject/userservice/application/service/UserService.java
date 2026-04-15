package petproject.userservice.application.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petproject.userservice.domain.exception.UserAlreadyExistException;
import petproject.userservice.domain.model.Address;
import petproject.userservice.domain.model.User;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.domain.repository.UserRepository;
import petproject.userservice.interfaces.api.dto.AddressDto;

import java.util.List;
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

    public User changeNames(UserId userId, String firstName, String lastName) {
        User user = findUserById(userId);

        user.changeName(firstName, lastName);

        return userRepository.save(user);
    }

    public List<Address> addAddress(UserId userId, String address) {
        User user = findUserById(userId);

        user.addAddress(Address.create(address));

        userRepository.save(user);

        return user.getAddresses();
    }

    public List<Address> getAddress(UserId userId) {
        return userRepository.findAddressesByUserId(userId);
    }
}
