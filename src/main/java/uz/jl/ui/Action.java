package uz.jl.ui;

import uz.jl.dto.user.UserCreateDTO;
import uz.jl.dto.user.UserDTO;
import uz.jl.dto.user.UserLoginDTO;
import uz.jl.services.UserService;
import uz.jl.test.AppResponse;

import java.util.Scanner;

public class Action {
    public static final Scanner SCANNER = new Scanner(System.in);
    public static final UserService USER_SERVICE = new UserService();

    public static void registerAction() {
        if (!SessionUser.isLoggedIn()) {
            UserCreateDTO dto = UserCreateDTO.builder()
                    .username(Utils.input_text("username"))
                    .password(Utils.input_text("password"))
                    .build();
            Utils.showResponse(USER_SERVICE.createUser(dto));
        } else System.out.println("Forbidden");
    }

    public static void loginAction() {
        if (!SessionUser.isLoggedIn()) {
            UserLoginDTO dto = UserLoginDTO.builder()
                    .username(Utils.input_text("username"))
                    .password(Utils.input_text("password"))
                    .build();
            AppResponse<UserDTO> response = USER_SERVICE.login(dto);
            SessionUser.setSessionUser(response);
            Utils.showResponse(response);
        } else System.out.println("Forbidden");
    }

    public static void allTodoAction() {
        if (SessionUser.isLoggedIn()) {
            Utils.showResponse(USER_SERVICE.getAllTodo(SessionUser.getUserId()));
        } else System.out.println("Sign in please");
    }

    public static void deleteAction() {
        if (SessionUser.isLoggedIn()) {
            System.out.println("delete todo");
        } else System.out.println("Sign in please");
    }

    public static void updateAction() {
        if (SessionUser.isLoggedIn()) {
            System.out.println("Update todo");
        } else System.out.println("Sign in please");
    }

    public static void createTodoAction() {
        if (SessionUser.isLoggedIn()) {
            System.out.println("Create Todo");
        } else System.out.println("Sign in please");
    }

    public static void logoutAction() {
        if (SessionUser.isLoggedIn()) {
            SessionUser.doLogout();
            quitAction();
        } else System.out.println("Sign in please");
    }

    public static void quitAction() {
        System.out.println("Bye");
    }

    public static void getWrongChoiceAction() {
        quitAction();
    }
}
