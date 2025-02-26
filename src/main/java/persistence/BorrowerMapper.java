package persistence;

import dto.AuthorAndBorrowersDTO;
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

public class BorrowerMapper {
    private DatabaseConnector connector;

    public BorrowerMapper(DatabaseConnector connector) {
        this.connector = connector;
    }

    public AuthorAndBorrowersDTO getAuthorAndBorrowersByName(String authorName) throws DatabaseException {
        String sql = "SELECT laaner.laaner_id, laaner.navn AS laaner_navn, adresse, postnr, forfatter.forfatter_id, forfatter.navn AS forfatter_navn        FROM forfatter JOIN bog ON forfatter.forfatter_id = bog.forfatter_id\n" +
                "        JOIN udlaan ON bog.bog_id = udlaan.bog_id\n" +
                "        JOIN laaner ON laaner.laaner_id = udlaan.laaner_id\n" +
                "        WHERE forfatter.navn = ?";
        try(Connection connection = connector.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, authorName);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Borrower> borrowers = new ArrayList<>();
                Author author = null;
                if(resultSet.next()) {
                    author = new Author(resultSet.getString("forfatter_navn"), resultSet.getInt("forfatter_id"));
                    borrowers.add(new Borrower(resultSet.getString("laaner_navn"), resultSet.getString("adresse"), resultSet.getInt("laaner_id")));
                }
                while(resultSet.next()){
                    borrowers.add(new Borrower(resultSet.getString("laaner_navn"), resultSet.getString("adresse"), resultSet.getInt("laaner_id")));

                }
                return new AuthorAndBorrowersDTO(author, borrowers);

            }
        }
        catch(SQLException e){
            throw new DatabaseException("Kunne ikke hente data fra databasen", e);
        }

    }

    public Borrower getBorrowerAndBooksById(int id) throws DatabaseException{
        String sql = "SELECT laaner.*, bog.*, forfatter.navn AS forfatter FROM laaner JOIN udlaan ON laaner.laaner_id = udlaan.laaner_id \n" +
                "JOIN bog ON bog.bog_id = udlaan.bog_id JOIN forfatter ON forfatter.forfatter_id = bog.bog_id WHERE laaner.laaner_id = ?";

        try (Connection connection = connector.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                prepareStatement.setInt(1, id);
                ResultSet resultSet = prepareStatement.executeQuery();

                Borrower borrower = null;
                while (resultSet.next())
                {
                    if(borrower == null) {
                        String name = resultSet.getString("navn");
                        String adresse = resultSet.getString("adresse");
                        String zip = resultSet.getString("postnr");
                        borrower = new Borrower(name, adresse + " " + zip, id);
                    }
                    int bookId = resultSet.getInt("bog_id");
                    String title = resultSet.getString("titel");
                    int releaseYear = resultSet.getInt("udgivelsesaar");
                    int authorId = resultSet.getInt("forfatter_id");
                    String authorName = resultSet.getString("forfatter");
                    Author author = new Author(authorName, authorId);
                    Book book = new Book(bookId, title, releaseYear, author);
                    borrower.addBook(book);
                }
                return borrower;
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e);
        }
    }

    public Borrower getBorrowerById(int id) throws DatabaseException
    {
        String sql = "SELECT * FROM laaner WHERE laaner_id = ?";



        try (Connection connection = connector.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                prepareStatement.setInt(1, id);
                ResultSet resultSet = prepareStatement.executeQuery();

                if (resultSet.next())
                {
                    String name = resultSet.getString("navn");
                    String adresse = resultSet.getString("adresse");
                    String zip = resultSet.getString("postnr");
                    return new Borrower(name, adresse + " " + zip, id);
                } else
                {
                    return null;
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e);
        }
    }







}
