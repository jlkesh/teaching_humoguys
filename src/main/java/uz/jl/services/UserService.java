package uz.jl.services;

import lombok.NonNull;
import uz.jl.db.UserDAO;
import uz.jl.dto.UserCreateDTO;
import uz.jl.dto.UserDTO;
import uz.jl.dto.UserLoginDTO;
import uz.jl.test.AppErrorDTO;
import uz.jl.test.AppResponse;

import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public AppResponse<Long> createUser(@NonNull UserCreateDTO dto) {
        try {
            return new AppResponse<>(userDAO.createUser(dto), 201);
        } catch (SQLException e) {
            e.printStackTrace();
            return new AppResponse<>(AppErrorDTO.builder()
                    .friendlyMessage(e.getLocalizedMessage())
                    .developerMessage(e.getLocalizedMessage())
                    .build());
        }
    }

    public AppResponse<UserDTO> login(@NonNull UserLoginDTO dto) {
        try {
            return new AppResponse<>(userDAO.login(dto));
        } catch (SQLException e) {
            return new AppResponse<>(AppErrorDTO.builder()
                    .friendlyMessage(e.getLocalizedMessage())
                    .developerMessage(e.getLocalizedMessage())
                    .build());
        }
    }

}
