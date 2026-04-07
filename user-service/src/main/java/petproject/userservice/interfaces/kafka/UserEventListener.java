package petproject.userservice.interfaces.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import petproject.userservice.application.service.UserService;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.interfaces.kafka.dto.UserRegisteredEvent;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final UserService userService;


    @KafkaListener(topics = "user.registered")
    public void onUserRegistered(UserRegisteredEvent message) {
//        System.out.println(string);
        userService.createUser(new UserId(message.getUserId()), message.getEmail());
    }

}
