package uz.jl.services;

import lombok.NonNull;
import uz.jl.db.TodoDAO;
import uz.jl.dto.todo.TodoCreateDTO;
import uz.jl.dto.todo.TodoDTO;
import uz.jl.dto.todo.TodoUpdateDTO;
import uz.jl.test.AppErrorDTO;
import uz.jl.test.AppResponse;
import uz.jl.ui.SessionUser;

import javax.lang.model.element.NestingKind;
import java.sql.SQLException;

public class TodoService {
    private TodoDAO todoDAO = new TodoDAO();

    public AppResponse<TodoDTO> create(@NonNull TodoCreateDTO todoCreateDTO) {
        todoCreateDTO.setUserId(SessionUser.getUserId());
        try {
            TodoDTO todoDTO = todoDAO.create(todoCreateDTO);
            return new AppResponse<>(todoDTO);
        } catch (SQLException e) {
            return new AppResponse<>(AppErrorDTO.builder()
                    .friendlyMessage(e.getLocalizedMessage())
                    .developerMessage(e.getLocalizedMessage())
                    .build());
        }
    }

    public AppResponse<String> delete(long todoId) {
        try {
            String message = todoDAO.delete(todoId, SessionUser.getUserId());
            return new AppResponse<>(message);
        } catch (SQLException e) {
            return new AppResponse<>(AppErrorDTO.builder()
                    .friendlyMessage(e.getLocalizedMessage())
                    .developerMessage(e.getLocalizedMessage())
                    .build());
        }
    }

    public AppResponse<Long> update(TodoUpdateDTO dto) {
        try {
            Long id = todoDAO.update(dto, SessionUser.getUserId());
            return new AppResponse<>(id);
        } catch (SQLException e) {
            return new AppResponse<>(AppErrorDTO.builder()
                    .friendlyMessage(e.getLocalizedMessage())
                    .developerMessage(e.getLocalizedMessage())
                    .build());
        }
    }
}
