package petproject.authservice.event;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredEvent {
    private UUID userId;
    private String username;
    private String surname;
}
