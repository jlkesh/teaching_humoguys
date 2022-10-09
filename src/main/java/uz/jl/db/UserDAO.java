package uz.jl.db;

import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import uz.jl.dto.todo.TodoDTO;
import uz.jl.dto.user.UserCreateDTO;
import uz.jl.dto.user.UserDTO;
import uz.jl.dto.user.UserLoginDTO;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDAO extends BaseDAO {


    public Long createUser(@NonNull UserCreateDTO dto) throws SQLException {
        Connection connection = getPostgresConnection();
        CallableStatement callableStatement = connection.prepareCall("select todo.user_register(?)");
        String jsonDATA = gson.toJson(dto);
        callableStatement.setString(1, jsonDATA);
        ResultSet resultSet = callableStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getLong(1);
        }
        throw new SQLException("QUERY did not return id");
    }

    public UserDTO login(@NonNull UserLoginDTO dto) throws SQLException {
        Connection connection = getPostgresConnection();
        CallableStatement callableStatement = connection.prepareCall("select user_login(?, ?)");
        callableStatement.setString(1, dto.getUsername());
        callableStatement.setString(2, dto.getPassword());
        ResultSet resultSet = callableStatement.executeQuery();
        if (resultSet.next()) {
            return gson.fromJson(resultSet.getString(1), UserDTO.class);
        }
        throw new SQLException("QUERY did not return user details");
    }

    public List<TodoDTO> getAllTodoByUserId(Long userId) throws SQLException {
        Connection connection = getPostgresConnection();
        CallableStatement callableStatement = connection.prepareCall("select user_todos(?)");
        callableStatement.setLong(1, userId);
        ResultSet resultSet = callableStatement.executeQuery();
        if (resultSet.next()) {
            String jsonDATA = resultSet.getString(1);
            Type type = new TypeToken<List<TodoDTO>>() {
            }.getType();
            return Objects.requireNonNullElse(gson.fromJson(jsonDATA, type), new ArrayList<>());

        }
        throw new SQLException("QUERY did not return todo list");
    }
}
