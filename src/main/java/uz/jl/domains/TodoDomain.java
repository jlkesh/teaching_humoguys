package uz.jl.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TodoDomain implements Domain {
    private Long id;
    private String title;
    private String uuid;
    private Boolean done;
    private String createdAt;
    private Long userId;
}
