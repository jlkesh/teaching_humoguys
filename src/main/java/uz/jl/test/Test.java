package uz.jl.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

public class Test {
    public static AppResponse<String> testBoyd1() {
        return new AppResponse<>("Successfully");
    }

    public static AppResponse<String> testBoyd2() {
        return new AppResponse<>("Successfully created", 201);
    }

    public static AppResponse<List<String>> testBoyd3() {
        List<String> langs = List.of("Java", "Scala", "Groovy", "Python");
        return new AppResponse<>(langs, (long) langs.size());
    }

    public static AppResponse<List<String>> testBoyd4() {
        List<String> langs = List.of("Java", "Scala", "Groovy", "Python");
        return new AppResponse<>(langs, 205, (long) langs.size());
    }

    public static AppResponse<String> testError1() {
        return new AppResponse<>(AppErrorDTO.builder()
                .friendlyMessage("Problem with Database")
                .developerMessage("Yo shut***** you got a error in blah blah procedure in line 5")
                .params(Map.of("username", "username already taken",
                        "password", "password should be strong enough at least 8 characters"))
                .build());
    }


    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String test1 = gson.toJson(testError1());
        System.out.println(test1);
    }

}
