package app;

import dto.AuthorAndBorrowersDTO;
import entities.Borrower;
import exceptions.DatabaseException;
import persistence.BorrowerMapper;
import persistence.DatabaseConnector;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/bibliotek?currentSchema=public";
    private static DatabaseConnector databaseConnector;

    static
    {
        try
        {
            databaseConnector = new DatabaseConnector(USERNAME, PASSWORD, URL);
        }
        catch (DatabaseException e)
        {
            System.out.println(e.getMessage());

        }
    }

    public static void main(String[] args)
    {

        BorrowerMapper borrowerMapper = new BorrowerMapper(databaseConnector);
        try
        {
            // Get a user by name
            Borrower borrower = borrowerMapper.getBorrowerById(1);
            System.out.println(borrower);

            AuthorAndBorrowersDTO authorAndBorrowersDTO = borrowerMapper.getAuthorAndBorrowersByName("Johannes V. Jensen");
            System.out.println(authorAndBorrowersDTO);
        }
        catch (DatabaseException e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getCause().getMessage());
        }
    }
}