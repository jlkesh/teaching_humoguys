package uz.jl.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDomain {
    private Long id;
    @Builder.Default
    private String uuid = UUID.randomUUID().toString();
    private String username;
    private String password;
    private String lastLoginTimeString;
    private String createdTime;
    @Builder.Default
    private String role = "USER";
}
