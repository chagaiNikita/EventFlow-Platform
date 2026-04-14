package petproject.userservice.interfaces.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import petproject.userservice.application.service.UserService;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.infrastructure.security.AuthenticatedUser;
import petproject.userservice.interfaces.api.dto.ChangeNameRequestDto;
import petproject.userservice.interfaces.api.dto.ProfileDto;
import petproject.userservice.interfaces.api.mapper.UserMapper;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;


    @GetMapping("me")
    public ResponseEntity<ProfileDto> getProfile(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toProfileDtoFromUserModel(userService.findUserById(new UserId(user.userId()))));
    }

    @PutMapping("me")
    public ResponseEntity<ProfileDto> updateProfile(@AuthenticationPrincipal AuthenticatedUser user, @RequestBody @Valid ChangeNameRequestDto changeNameRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(
                userMapper.toProfileDtoFromUserModel(
                        userService.changeNames(
                                new UserId(user.userId()),
                                changeNameRequestDto.firstName(),
                                changeNameRequestDto.lastName()
                        )
                )
        );
    }
}
