package uz.jl.ui;

import uz.jl.dto.UserCreateDTO;
import uz.jl.dto.UserDTO;
import uz.jl.dto.UserLoginDTO;
import uz.jl.services.UserService;
import uz.jl.test.AppResponse;

import java.util.Scanner;

public class UIAndroid {
    private static final Scanner SCANNER = new Scanner(System.in);

    private static final UserService USER_SERVICE = new UserService();

    public static void main(String[] args) {
        String next;
        do {
            System.out.println("Register User -> 1");
            System.out.println("Login User    -> 2");
            System.out.println("Quit          -> q");
            next = SCANNER.next();
            if (next.equals("1")) {
                UserCreateDTO dto = UserCreateDTO.builder()
                        .username(Utils.input_text("username"))
                        .password(Utils.input_text("password"))
                        .build();
                AppResponse<Long> response = USER_SERVICE.createUser(dto);
                Utils.showResponse(response);

            } else if (next.equals("2")) {
                UserLoginDTO dto = UserLoginDTO.builder()
                        .username(Utils.input_text("username"))
                        .password(Utils.input_text("password"))
                        .build();
                AppResponse<UserDTO> response = USER_SERVICE.login(dto);
                Utils.showResponse(response);
            }
        } while (!next.equals("q"));
        System.out.println("Bye");


    }
}
