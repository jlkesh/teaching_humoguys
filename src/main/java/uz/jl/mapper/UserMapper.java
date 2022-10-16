package uz.jl.mapper;

import lombok.NonNull;
import uz.jl.domains.UserDomain;
import uz.jl.dto.user.UserCreateDTO;
import uz.jl.dto.user.UserDTO;

public class UserMapper {

    public static UserDTO toDto(UserDomain domain) {
        return new UserDTO(domain.getId(),
                domain.getUuid(),
                domain.getUsername(),
                domain.getLastLoginTimeString(),
                domain.getCreatedTime(),
                domain.getRole());
    }

    public static UserDomain fromCreateDTO(@NonNull UserCreateDTO dto) {
        return UserDomain
                .builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }
}
