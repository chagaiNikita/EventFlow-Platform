package petproject.userservice.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import petproject.userservice.domain.exception.UserNotFoundException;
import petproject.userservice.domain.model.User;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.domain.repository.UserRepository;
import petproject.userservice.infrastructure.persistence.model.UserEntity;
import petproject.userservice.infrastructure.persistence.repository.UserJpaRepository;

import java.util.ArrayList;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;


    @Override
    public boolean existUserByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntity.builder()
                .id(user.getId().getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        userJpaRepository.save(userEntity);

        return User.restore(
                new UserId(userEntity.getId()),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                new ArrayList<>(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }


    @Override
    public User findById(UserId id) {
        UserEntity userEntity = userJpaRepository.findById(id.getId())
                .orElseThrow(UserNotFoundException::new);

        return User.restore(
                new UserId(userEntity.getId()),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                new ArrayList<>(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }
}
