package uz.jl.services;

import lombok.NonNull;
import uz.jl.config.PasswordEncoder;
import uz.jl.db.UserDAO;
import uz.jl.domains.UserDomain;
import uz.jl.dto.todo.TodoDTO;
import uz.jl.dto.user.UserCreateDTO;
import uz.jl.dto.user.UserDTO;
import uz.jl.dto.user.UserLoginDTO;
import uz.jl.mapper.UserMapper;
import uz.jl.test.AppErrorDTO;
import uz.jl.test.AppResponse;

import javax.swing.text.html.Option;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public AppResponse<Long> createUser(@NonNull UserCreateDTO dto) {
        try {
            if (Objects.isNull(dto.getUsername()) || dto.getUsername().isEmpty())
                return new AppResponse<>(AppErrorDTO.builder()
                        .friendlyMessage("Username can not be null")
                        .developerMessage("Username can not be null")
                        .build());

            if (Objects.isNull(dto.getPassword()) || dto.getPassword().isEmpty())
                return new AppResponse<>(AppErrorDTO.builder()
                        .friendlyMessage("Password can not be null")
                        .developerMessage("Password can not be null")
                        .build());
            dto.setPassword(PasswordEncoder.encode(dto.getPassword()));
            UserDomain userDomain = UserMapper.fromCreateDTO(dto);
            Long userId = userDAO.save(userDomain);
            return new AppResponse<>(userId, 201);
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
            Optional<UserDomain> userOptional = userDAO.findUserByUsername(dto.getUsername());
            if (userOptional.isEmpty())
                return new AppResponse<>(AppErrorDTO.builder()
                        .friendlyMessage("Bad credentials")
                        .developerMessage("Bad credentials")
                        .build());

            UserDomain userDomain = userOptional.get();

            if (!PasswordEncoder.match(dto.getPassword(), userDomain.getPassword())) {
                return new AppResponse<>(AppErrorDTO.builder()
                        .friendlyMessage("Bad credentials")
                        .developerMessage("Bad credentials")
                        .build());
            }

            UserDTO userDTO = UserMapper.toDto(userDomain);
            return new AppResponse<>(userDTO);
        } catch (SQLException e) {
            return new AppResponse<>(AppErrorDTO.builder()
                    .friendlyMessage(e.getLocalizedMessage())
                    .developerMessage(e.getLocalizedMessage())
                    .build());
        }
    }

    public AppResponse<List<TodoDTO>> getAllTodo(@NonNull Long userId) {
        try {
            List<TodoDTO> todoDTOList = userDAO.getAllTodoByUserId(userId);
            return new AppResponse<>(todoDTOList, (long) todoDTOList.size());
        } catch (SQLException e) {
            return new AppResponse<>(AppErrorDTO.builder()
                    .friendlyMessage(e.getLocalizedMessage())
                    .developerMessage(e.getLocalizedMessage())
                    .build());
        }
    }
}
