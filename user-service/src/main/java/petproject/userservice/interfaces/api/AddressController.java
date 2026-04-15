package petproject.userservice.interfaces.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import petproject.userservice.application.service.UserService;
import petproject.userservice.domain.model.AddressId;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.infrastructure.security.AuthenticatedUser;
import petproject.userservice.interfaces.api.dto.AddAddressRequestDto;
import petproject.userservice.interfaces.api.dto.AddressDto;
import petproject.userservice.interfaces.api.dto.ProfileDto;
import petproject.userservice.interfaces.api.mapper.UserMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users/me/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<List<AddressDto>> addAddress(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestBody @Valid AddAddressRequestDto addAddressRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                        userService.addAddress(
                                new UserId(user.userId()),
                                addAddressRequestDto.address()
                        ).stream()
                                .map(userMapper::toAddressDto)
                                .toList()

        );
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAddresses(
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.getAddress(
                        new UserId(user.userId())
                ).stream()
                        .map(userMapper::toAddressDto)
                        .toList()
        );
    }

    @DeleteMapping("{addressId}")
    public ResponseEntity<List<AddressDto>> deleteAddress(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable("addressId") UUID addressId
    ) {
        userService.deleteAddress(new UserId(user.userId()), new AddressId(addressId));
        return ResponseEntity.noContent().build();
    }
}
