package kg.bee.productservice.infrastructure.security;

import java.util.UUID;

public record AuthenticatedUser(UUID userId, String email) {}