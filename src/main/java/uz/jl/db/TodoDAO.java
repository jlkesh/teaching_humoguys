package uz.jl.db;

import uz.jl.dto.todo.TodoCreateDTO;
import uz.jl.dto.todo.TodoDTO;
import uz.jl.dto.todo.TodoUpdateDTO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TodoDAO extends BaseDAO {
    public TodoDTO create(TodoCreateDTO todoCreateDTO) throws SQLException {
        Connection connection = getPostgresConnection();
        CallableStatement callableStatement = connection.prepareCall("select todo_create(?,?)");

        callableStatement.setString(1, todoCreateDTO.getTitle());
        callableStatement.setLong(2, todoCreateDTO.getUserId());
        ResultSet resultSet = callableStatement.executeQuery();
        if (resultSet.next()) {
            String jsonDATA = resultSet.getString(1);
            return gson.fromJson(jsonDATA, TodoDTO.class);
        }
        throw new SQLException("QUERY did not return id");
    }

    public String delete(long todoId, Long userId) throws SQLException {
        var connection = getPostgresConnection();
        var callableStatement = connection.prepareCall("select todo_delete(?,?)");

        callableStatement.setLong(1, todoId);
        callableStatement.setLong(2, userId);
        ResultSet resultSet = callableStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        throw new SQLException("QUERY did not work");
    }

    public Long update(TodoUpdateDTO dto, Long userId) throws SQLException {
        var connection = getPostgresConnection();
        var callableStatement = connection.prepareCall("select todo_update(?,?)");

        String toJson = gson.toJson(dto);
        System.out.println(toJson);
        callableStatement.setString(1, toJson);
        callableStatement.setLong(2, userId);
        ResultSet resultSet = callableStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getLong(1);
        }
        throw new SQLException("QUERY did not work");
    }
}
