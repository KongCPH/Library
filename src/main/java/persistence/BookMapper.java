package persistence;

import entities.Author;
import entities.Book;
import entities.Borrower;
import exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookMapper {
    private DatabaseConnector connector;

    public BookMapper(DatabaseConnector connector) {
        this.connector = connector;
    }

    //Opgave 3: Find alle bøger, og deres forfattere
    public List<Book> getAllBookWithAuthors() throws DatabaseException{
        String sql = "SELECT bog.*, navn FROM bog JOIN forfatter ON bog.forfatter_id = forfatter.forfatter_id";
        try (Connection connection = connector.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                ResultSet resultSet = prepareStatement.executeQuery();

                List<Book> books = new ArrayList<>();
                while (resultSet.next())
                {
                    int bookId = resultSet.getInt("bog_id");
                    String title = resultSet.getString("titel");
                    int releaseYear = resultSet.getInt("udgivelsesaar");
                    int authorId = resultSet.getInt("forfatter_id");
                    String name = resultSet.getString("navn");
                    Author author = new Author(name, authorId);
                    Book book = new Book(title, releaseYear, author);
                    author.addBook(book);
                    books.add(book);
                }
                return books;
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e);
        }

    }

    // DML Opgave 4: Rediger en bogtitel (update)

    public boolean updateBookTitle(int id, String newTitle) throws DatabaseException{
        String sql = "UPDATE bog SET titel = ? WHERE bog_id = ?;";
        try (Connection connection = connector.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                prepareStatement.setString(1, newTitle);
                prepareStatement.setInt(2, id);
                int rowsAffected = prepareStatement.executeUpdate();
                if (rowsAffected == 1){
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException throwables) {
                throw new DatabaseException("Could not update book with id " + id + " in database");
            }

        }
        catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e);
        }

    }

}
