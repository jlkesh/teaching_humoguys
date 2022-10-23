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
    public static final String DELETE_TODO = "delete from humoguystodo.todo.todos where id = ? and user_id = ?;";
    public static final String UPDATE_TODO = "update humoguystodo.todo.todos t " +
            "set title = ?, " +
            "done = ? " +
            "where t.id = ? and " +
            "t.user_id = ?";

    public TodoDomain create(TodoCreateDTO todoCreateDTO) throws SQLException {
        Connection connection = getPostgresConnection();

        PreparedStatement pstm = connection.prepareStatement(INSERT_TODO_QUERY);
        pstm.setString(1, todoCreateDTO.getTitle());
        pstm.setLong(2, todoCreateDTO.getUserId());

        ResultSet resultSet = pstm.executeQuery();
        return mapTo(resultSet, TodoRowMapper.class);
    }

    public boolean delete(long todoId, Long userId) throws SQLException {
        var connection = getPostgresConnection();
        var prsm = connection.prepareCall(DELETE_TODO);
        prsm.setLong(1, todoId);
        prsm.setLong(2, userId);
        prsm.execute();
        return true;
    }

    public Long update(TodoUpdateDTO dto, Long userId) throws SQLException {
        var connection = getPostgresConnection();
        var prsm = connection.prepareStatement(UPDATE_TODO);
        prsm.setString(1, dto.getTitle());
        prsm.setBoolean(2, dto.getDone());
        prsm.setLong(3, dto.getTodoId());
        prsm.setLong(4, userId);
        prsm.execute();
        return dto.getTodoId();
    }
}
