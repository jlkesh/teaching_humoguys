package uz.jl.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String uuid;
    private String username;
    private String lastLoginTimeString;
    private String createdTime;
    private String role;
}
