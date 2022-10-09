package uz.jl.ui;

import static uz.jl.ui.Action.*;

public class UIAndroid {
    ;

    public static void main(String[] args) {
        String choice;
        do {
            showMenu();
            choice = SCANNER.next();
            switch (choice) {
                case "1" -> registerAction();
                case "2" -> loginAction();
                case "3" -> allTodoAction();
                case "4" -> deleteAction();
                case "5" -> updateAction();
                case "6" -> createTodoAction();
                case "7" -> logoutAction();
                case "q" -> quitAction();
                default -> getWrongChoiceAction();
            }
        } while (!choice.equals("q"));
    }


    public static void showMenu() {
        if (!SessionUser.isLoggedIn()) {
            System.out.println("Register User -> 1");
            System.out.println("Login User    -> 2");
            System.out.println("Quit          -> q");
        } else {
            System.out.println("Show All Todo -> 3");
            System.out.println("Delete Todo   -> 4");
            System.out.println("Update Todo   -> 5");
            System.out.println("Create Todo   -> 6");
            System.out.println("Logout        -> 7");
        }

    }
}
