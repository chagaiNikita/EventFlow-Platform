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
import petproject.userservice.interfaces.api.dto.AddAddressRequestDto;
import petproject.userservice.interfaces.api.dto.ProfileDto;
import petproject.userservice.interfaces.api.mapper.UserMapper;

@RestController
@RequestMapping("api/users/me/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<ProfileDto> addAddress(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody @Valid AddAddressRequestDto addAddressRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userMapper.toProfileDtoFromUserModel(
                        userService.addAddress(
                                new UserId(user.userId()),
                                addAddressRequestDto.address()
                        )
                )
        );


    }
}
