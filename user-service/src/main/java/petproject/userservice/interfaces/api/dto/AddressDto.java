package petproject.userservice.interfaces.api.dto;

import java.util.UUID;

public record AddressDto (
        UUID id,
        String address
)
{}
