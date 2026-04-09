package petproject.userservice.interfaces.api.dto;

import petproject.userservice.domain.model.Address;

import java.util.List;

public record ProfileDto(
        String email,
        String firstName,
        String lastName,
        List<AddressDto> addresses
) {
}
