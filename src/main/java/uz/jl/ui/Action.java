package uz.jl.ui;

import jdk.jshell.execution.Util;
import uz.jl.dto.todo.TodoCreateDTO;
import uz.jl.dto.todo.TodoDTO;
import uz.jl.dto.todo.TodoUpdateDTO;
import uz.jl.dto.user.UserCreateDTO;
import uz.jl.dto.user.UserDTO;
import uz.jl.dto.user.UserLoginDTO;
import uz.jl.services.TodoService;
import uz.jl.services.UserService;
import uz.jl.test.AppResponse;

import java.util.Scanner;

public class Action {
    public static final Scanner SCANNER = new Scanner(System.in);
    public static final Scanner SCANNER_STR = new Scanner(System.in);
    public static final UserService USER_SERVICE = new UserService();
    public static final TodoService TODO_SERVICE = new TodoService();

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
            System.out.print("Please enter todo id : ");
            long todoId = SCANNER.nextLong();
            Utils.showResponse(TODO_SERVICE.delete(todoId));
        } else System.out.println("Sign in please");
    }

    public static void updateAction() {
        if (SessionUser.isLoggedIn()) {
            System.out.print("Enter todo id : ");
            Long todoId = SCANNER.nextLong();
            System.out.print("Enter todo title : ");
            String title = SCANNER_STR.nextLine();
            System.out.print("Enter todo done(true/false) : ");
            String done = SCANNER_STR.nextLine();
            TodoUpdateDTO dto = TodoUpdateDTO.builder()
                    .todoId(todoId)
                    .title(title.length() == 0 ? null : title)
                    .done(done.length() == 0 ? null : Boolean.valueOf(done))
                    .build();
            Utils.showResponse(TODO_SERVICE.update(dto));
        } else System.out.println("Sign in please");
    }

    public static void createTodoAction() {
        if (SessionUser.isLoggedIn()) {
            System.out.print("Enter todo title : ");
            String title = SCANNER_STR.nextLine();
            TodoCreateDTO todoCreateDTO = TodoCreateDTO.builder()
                    .title(title)
                    .build();
            Utils.showResponse(TODO_SERVICE.create(todoCreateDTO));
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
