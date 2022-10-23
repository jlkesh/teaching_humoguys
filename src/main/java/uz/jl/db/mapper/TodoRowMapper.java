package uz.jl.db.mapper;

import uz.jl.domains.TodoDomain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TodoRowMapper implements RowMapper<TodoDomain> {
    @Override
    public TodoDomain map(ResultSet resultSet) throws SQLException {
        return TodoDomain.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .uuid(resultSet.getString("uuid"))
                .done(resultSet.getBoolean("done"))
                .createdAt(resultSet.getString("created_at"))
                .userId(resultSet.getLong("user_id"))
                .build();
    }
}
