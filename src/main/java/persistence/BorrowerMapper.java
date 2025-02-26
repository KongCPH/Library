package persistence;

import dto.AuthorAndBorrowersDTO;
import entities.Author;
import entities.Book;
import entities.Borrower;
import exceptions.DatabaseException;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // Opgave 2: Find alle lånere, og vis deres data inklusive postnummer og by.
    public List<Borrower> getAllBorrowers() throws DatabaseException {
        String sql = "SELECT laaner.*, by FROM laaner JOIN postnummer ON laaner.postnr = postnummer.postnr";
        try (Connection connection = connector.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                ResultSet resultSet = prepareStatement.executeQuery();

                List<Borrower> borrowers = new ArrayList<>();
                while (resultSet.next())
                {
                    int borrowerId = resultSet.getInt("laaner_id");
                    String name = resultSet.getString("navn");
                    String address = resultSet.getString("adresse");
                    int zip = resultSet.getInt("postnr");
                    String city = resultSet.getString("by");
                    borrowers.add(new Borrower(name, address + " " + zip + " " + city, borrowerId));
                }
                return borrowers;
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e);
        }
    }


    // Opgave 1: Find en låner ud fra et specifikt laaner_id.
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

    // Opgave 4: Find alle lånere og de bøger de har lånt. Medtag også bogtitler og evt. forfatter
    public List<Borrower> getAllBorrowerAndBooks() throws DatabaseException{
        String sql = "SELECT laaner.*, by, bog.*, forfatter.navn AS forfatter FROM laaner JOIN postnummer ON laaner.postnr = postnummer.postnr JOIN\n" +
                "udlaan ON laaner.laaner_id = udlaan.laaner_id\n" +
                "JOIN bog ON bog.bog_id = udlaan.bog_id JOIN forfatter ON forfatter.forfatter_id = bog.bog_id\n" +
                "ORDER By laaner_id";

        try (Connection connection = connector.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                ResultSet resultSet = prepareStatement.executeQuery();
                List<Borrower> borrowers = new ArrayList<>();
                int borrowerIdPrevious = 0;
                Borrower borrower = null;
                while (resultSet.next())
                {
                    int borrowerId = resultSet.getInt("laaner_id");
                    if (borrowerId != borrowerIdPrevious){
                        String name = resultSet.getString("navn");
                        String address = resultSet.getString("adresse");
                        int zip = resultSet.getInt("postnr");
                        String city = resultSet.getString("by");
                        borrower = new Borrower(name, address + " " + zip + " " + city, borrowerId);
                        borrowers.add(borrower);
                        borrowerIdPrevious = borrowerId;
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
                return borrowers;
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e);
        }


    }
    // DML Opgave 1: Indsæt en ny låner (insert)
    public boolean insertBorrower(Borrower borrower) throws DatabaseException{
        boolean result = false;
        int newId = 0;
        String sql = "insert into laaner (navn, adresse, postnr) values (?,?,?)";
        try (Connection connection = connector.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )) {
                ps.setString(1, borrower.getName());
                ps.setString(2, getStreetAndNo(borrower.getAddress()));
                ps.setInt(3, getZip(borrower.getAddress()));

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1){
                    result = true;
                } else {
                    throw new DatabaseException("Borrower with name = " + borrower.getName() + " could not be inserted into database");
                }
                ResultSet idResultset = ps.getGeneratedKeys();
                if (idResultset.next()){
                    newId = idResultset.getInt(1);
                    borrower.setId(newId);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new DatabaseException("Could not insert member in database");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Could not establish connection to database");
        }
        return result;
    }

    // DML Opgave 2: Opret et nyt udlån af en bog (insert)
    public boolean insertLoan(Borrower borrower, Book book) throws DatabaseException{
        boolean result = false;
        String sql = "insert into udlaan  (bog_id, laaner_id, dato) values (?,?,?)";

        try (Connection connection = connector.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS )) {
                ps.setInt(1, book.getId());
                ps.setInt(2, borrower.getId());
                ps.setDate(3, Date.valueOf(LocalDate.now()));

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1){
                    result = true;
                } else {
                    throw new DatabaseException("The loan could not be inserted");
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new DatabaseException("Could not insert member in database");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Could not establish connection to database");
        }
        return result;
    }

    public boolean deleteLoan(Borrower borrower, Book book) throws DatabaseException{
        String sql = "DELETE FROM udlaan WHERE laaner_id = ? AND bog_id = ?;";
        try (Connection connection = connector.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, borrower.getId());
                ps.setInt(2, book.getId());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1){
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException throwables) {
                throw new DatabaseException("Could not delete loan for the borrowner " + borrower.getName() + " and the book " + book.getTitel() + " in database");
            }
        } catch (SQLException throwables) {
            throw new DatabaseException("Could not establish connection to database");
        }

    }


    // Slet en låner
    public boolean deleteBorrower(int borrowerId) throws DatabaseException{
        boolean result = false;

        String sql = "DELETE FROM udlaan WHERE laaner_id = ?;\n" +
                "DELETE FROM laaner WHERE laaner_id = ?;";
        try (Connection connection = connector.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, borrowerId);
                ps.setInt(2, borrowerId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1){
                    result = true;
                } else {
                    return false;
                }
            } catch (SQLException throwables) {
                throw new DatabaseException("Could not delete borrower with id = " + borrowerId + " in database");
            }
        } catch (SQLException throwables) {
            throw new DatabaseException("Could not establish connection to database");
        }
        return result;
    }



    private int getZip(String address){
        // Regex for at finde et firecifret tal (postnummer)
        Pattern pattern = Pattern.compile("\\b\\d{4}\\b");
        Matcher matcher = pattern.matcher(address);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            return 0;
        }
    }

    private String getStreetAndNo(String address){
        // Regex for at finde gade og nummer (alt før postnummer)
        Pattern pattern = Pattern.compile("^(.*?)\\b\\d{4}\\b");
        Matcher matcher = pattern.matcher(address);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }





}
