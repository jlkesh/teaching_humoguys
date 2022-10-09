package uz.jl.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uz.jl.dto.user.UserDTO;
import uz.jl.test.AppResponse;

import java.util.Scanner;

public class Utils {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private static final Scanner SCANNER = new Scanner(System.in);

    public static String input_text(String message) {
        System.out.print(message + " : ");
        return SCANNER.next();
    }

    public static String input_text() {
        return input_text("");
    }

    public static void showResponse(AppResponse<?> response) {
        String responseJSON = GSON.toJson(response);
        if (response.isOk()) {
            System.out.println(ANSI_GREEN + responseJSON + ANSI_RESET);
        } else System.out.println(ANSI_RED + responseJSON + ANSI_RESET);

    }
}
