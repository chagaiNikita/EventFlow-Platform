package petproject.userservice.infrastructure.persistence.mapper;

import petproject.userservice.domain.model.Address;
import petproject.userservice.domain.model.AddressId;
import petproject.userservice.domain.model.User;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.infrastructure.persistence.model.AddressEntity;
import petproject.userservice.infrastructure.persistence.model.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserEntityMapper {

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId().getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .addressEntities(
                        user.getAddresses().stream()
                                .map(a -> UserEntityMapper.toEntity(a, user.getId().getId()))
                                .toList()
                )
                .build();
    }

    public static AddressEntity toEntity(Address address, UUID userId) {
        return AddressEntity.builder()
                .id(address.getId().getId()) // 🔥 ВАЖНО
                .userId(userId)
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
        entity.setUpdatedAt(user.getUpdatedAt());

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
                        .userId(entity.getId())
                        .address(address.getAddress())
//                        .createdAt(LocalDateTime.now())
                        .build();

                existing.add(newEntity);
            }
        }
    }

}
