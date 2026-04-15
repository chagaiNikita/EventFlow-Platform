package petproject.userservice.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import petproject.userservice.domain.exception.UserNotFoundException;
import petproject.userservice.domain.model.User;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.domain.repository.UserRepository;
import petproject.userservice.infrastructure.persistence.mapper.UserEntityMapper;
import petproject.userservice.infrastructure.persistence.model.UserEntity;
import petproject.userservice.infrastructure.persistence.repository.UserJpaRepository;


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
        UserEntity userEntity = userJpaRepository.findById(user.getId().getId())
                .orElseThrow(UserNotFoundException::new);

        UserEntityMapper.updateEntity(user, userEntity);

        userJpaRepository.save(userEntity);

        return UserEntityMapper.toDomain(userEntity);
    }


    @Override
    public User findById(UserId id) {
        UserEntity userEntity = userJpaRepository.findById(id.getId())
                .orElseThrow(UserNotFoundException::new);

        return UserEntityMapper.toDomain(userEntity);
    }
}
