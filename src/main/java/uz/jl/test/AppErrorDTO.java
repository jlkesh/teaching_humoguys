package uz.jl.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AppErrorDTO {
    private Map<String, Object> params = new HashMap<>();
    private String friendlyMessage;
    private String developerMessage;
}
