package uz.jl.dto.todo;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoUpdateDTO {
    @SerializedName("todo_id")
    private Long todoId;
    private String title;
    private Boolean done;
}
