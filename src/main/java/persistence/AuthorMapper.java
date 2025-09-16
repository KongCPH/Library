package persistence;

import entities.Author;
import exceptions.DatabaseException;

import java.sql.*;

public class AuthorMapper {

    private DatabaseConnector connector;

    public AuthorMapper(DatabaseConnector connector) {
        this.connector = connector;
    }

    public Author insertAuthor(String name) throws DatabaseException {
        String sql = "INSERT INTO forfatter (navn) VALUES (?)";
        try (Connection connection = connector.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            {
                prepareStatement.setString(1, name);
                prepareStatement.executeUpdate();
                ResultSet keySet = prepareStatement.getGeneratedKeys();
                if (keySet.next())
                {
                    return new Author(name, keySet.getInt(1));
                } else return null;
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Could not create user in the database", e);
        }
    }
}
