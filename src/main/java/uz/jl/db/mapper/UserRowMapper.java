package uz.jl.db.mapper;

import uz.jl.domains.UserDomain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserDomain> {
    @Override
    public UserDomain map(ResultSet resultSet) throws SQLException {
        return new UserDomain(
                resultSet.getLong("id"),
                resultSet.getString("uuid"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("last_login_at"),
                resultSet.getString("created_at"),
                resultSet.getString("role"));
    }
}
