package petproject.userservice.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import petproject.userservice.domain.exception.UserNotFoundException;
import petproject.userservice.domain.model.Address;
import petproject.userservice.domain.model.User;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.domain.repository.UserRepository;
import petproject.userservice.infrastructure.persistence.mapper.UserEntityMapper;
import petproject.userservice.infrastructure.persistence.model.AddressEntity;
import petproject.userservice.infrastructure.persistence.model.UserEntity;
import petproject.userservice.infrastructure.persistence.repository.UserJpaRepository;

import java.util.List;


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
                .map(existing -> {
                    UserEntityMapper.updateEntity(user, existing);
                    return existing;
                })
                .orElseGet(() -> UserEntityMapper.toEntity(user));

        userJpaRepository.save(userEntity);
        return UserEntityMapper.toDomain(userEntity);
    }

    @Override
    public User findById(UserId id) {
        UserEntity userEntity = userJpaRepository.findById(id.getId())
                .orElseThrow(UserNotFoundException::new);

        return UserEntityMapper.toDomain(userEntity);
    }

    @Override
    public List<Address> findAddressesByUserId(UserId userId) {
        UserEntity userEntity = userJpaRepository.findById(userId.getId())
                .orElseThrow(UserNotFoundException::new);

        List<AddressEntity> addressEntities = userEntity.getAddressEntities();

        return addressEntities.stream()
                .map(UserEntityMapper::toDomain)
                .toList();
    }
}
