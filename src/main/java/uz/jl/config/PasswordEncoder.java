package uz.jl.config;

import lombok.NonNull;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {

    public static String encode(@NonNull String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean match(@NonNull String rawPassword,
                                @NonNull String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
