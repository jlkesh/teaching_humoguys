package uz.jl.ui;

import uz.jl.dto.user.UserDTO;
import uz.jl.test.AppResponse;

import java.util.Objects;

public class SessionUser {
    private static UserDTO session;

    public static boolean isLoggedIn() {
        return Objects.nonNull(session);
    }

    public static void doLogout() {
        session = null;
    }

    public static void setSessionUser(AppResponse<UserDTO> response) {
        if (response.isOk()) {
            session = response.getBody();
        }
    }

    public static Long getUserId() {
        return session.getId();
    }


}
