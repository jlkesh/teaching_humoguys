package uz.jl.dto.todo;


import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDTO {
    private Long id;
    private String title;
    @SerializedName("uuid")
    private String uniqueCode;
    private Boolean done;
    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("user_id")
    private Long userId;
}
