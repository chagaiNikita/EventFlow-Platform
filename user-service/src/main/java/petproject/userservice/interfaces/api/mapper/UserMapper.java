package petproject.userservice.interfaces.api.mapper;

import org.mapstruct.Mapper;
import petproject.userservice.domain.model.Address;
import petproject.userservice.domain.model.User;
import petproject.userservice.interfaces.api.dto.AddressDto;
import petproject.userservice.interfaces.api.dto.ProfileDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ProfileDto toProfileDtoFromUserModel(User user);


    default AddressDto toAddressDto(Address address) {
        return new AddressDto(address.getId().getId(), address.getAddress());
    }
}
