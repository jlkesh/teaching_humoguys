package uz.jl.db;

import uz.jl.db.mapper.RowMapper;
import uz.jl.db.mapper.TodoRowMapper;
import uz.jl.domains.TodoDomain;
import uz.jl.dto.todo.TodoCreateDTO;
import uz.jl.dto.todo.TodoDTO;
import uz.jl.dto.todo.TodoUpdateDTO;

import java.sql.*;

public class TodoDAO extends BaseDAO {

    public static final String INSERT_TODO_QUERY = "insert into humoguystodo.todo.todos (title, user_id) values (?,?) returning *;";

    public TodoDomain create(TodoCreateDTO todoCreateDTO) throws SQLException {
        Connection connection = getPostgresConnection();

        PreparedStatement pstm = connection.prepareStatement(INSERT_TODO_QUERY);
        pstm.setString(1, todoCreateDTO.getTitle());
        pstm.setLong(2, todoCreateDTO.getUserId());

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return map(resultSet, TodoRowMapper.class);
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
