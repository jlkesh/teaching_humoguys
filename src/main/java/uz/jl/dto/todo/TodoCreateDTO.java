package uz.jl.dto.todo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoCreateDTO {
    private String title;
    private Long userId;
}
