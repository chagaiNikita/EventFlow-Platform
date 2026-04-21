package petproject.userservice.infrastructure.persistence.mapper;

import petproject.userservice.domain.model.Address;
import petproject.userservice.domain.model.AddressId;
import petproject.userservice.domain.model.User;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.infrastructure.persistence.model.AddressEntity;
import petproject.userservice.infrastructure.persistence.model.UserEntity;

import java.util.*;
import java.util.stream.Collectors;

public class UserEntityMapper {

    public static UserEntity toEntity(User user) {
        UserEntity userEntity = UserEntity.builder()
                .id(user.getId().getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .addressEntities(new ArrayList<>()) // пустой список
                .build();

        // теперь есть userEntity — передаём ссылку
        List<AddressEntity> addresses = Optional.of(user.getAddresses())
                .orElseGet(List::of)
                .stream()
                .map(a -> toAddressEntity(a, userEntity))
                .toList();

        userEntity.setAddressEntities(addresses);

        return userEntity;
    }

    public static AddressEntity toAddressEntity(Address address, UserEntity userEntity) {
        return AddressEntity.builder()
                .id(address.getId().getId())
                .user(userEntity)       // ссылка на UserEntity вместо UUID
                .address(address.getAddress())
                .build();
    }
    public static User toDomain(UserEntity entity) {
        return User.restore(
                new UserId(entity.getId()),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAddressEntities().stream()
                        .map(UserEntityMapper::toDomain)
                        .toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Address toDomain(AddressEntity entity) {
        return Address.restore(
                new AddressId(entity.getId()),
                entity.getAddress()
        );
    }

    public static void updateEntity(User user, UserEntity entity) {

        // -------------------------
        // 1. Обновляем простые поля
        // -------------------------
        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());

        List<AddressEntity> existing = entity.getAddressEntities();

        // -------------------------
        // 2. Мапа существующих адресов
        // -------------------------
        Map<UUID, AddressEntity> existingMap = existing.stream()
                .collect(Collectors.toMap(AddressEntity::getId, a -> a));

        // -------------------------
        // 3. Множество входящих id
        // -------------------------
        Set<UUID> incomingIds = user.getAddresses().stream()
                .map(a -> a.getId().getId())
                .collect(Collectors.toSet());

        // -------------------------
        // 4. УДАЛЕНИЕ (orphanRemoval)
        // -------------------------
        existing.removeIf(e -> !incomingIds.contains(e.getId()));

        // -------------------------
        // 5. UPDATE + INSERT
        // -------------------------
        for (Address address : user.getAddresses()) {

            AddressEntity found = existingMap.get(address.getId().getId());

            if (found != null) {
                // ✅ UPDATE (createdAt сохраняется)
                found.setAddress(address.getAddress());

            } else {
                // ✅ INSERT
                AddressEntity newEntity = AddressEntity.builder()
                        .id(address.getId().getId())
                        .user(entity)
                        .address(address.getAddress())
//                        .createdAt(LocalDateTime.now())
                        .build();

                existing.add(newEntity);
            }
        }
    }

}
