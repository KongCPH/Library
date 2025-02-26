package app;

import dto.AuthorAndBorrowersDTO;
import entities.Book;
import entities.Borrower;
import exceptions.DatabaseException;
import persistence.BookMapper;
import persistence.BorrowerMapper;
import persistence.DatabaseConnector;

import java.util.List;

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

    public static  void main(String[] args)
    {

        BorrowerMapper borrowerMapper = new BorrowerMapper(databaseConnector);
        BookMapper bookMapper = new BookMapper(databaseConnector);
        try
        {
            // Opgave 1: Find en låner ud fra et specifikt laaner_id.
            /* Borrower borrower = borrowerMapper.getBorrowerById(1);
            System.out.println(borrower); */

            // Opgave 2: Find alle lånere, og vis deres data inklusive postnummer og by.
            /* List<Borrower> borrowers = borrowerMapper.getAllBorrowers();
            System.out.println(borrowers); */

            //Opgave 3: Find alle bøger, og deres forfattere
            /* List<Book> books = bookMapper.getAllBookWithAuthors();
            System.out.println(books);*/


/*          // Et kald som returnere en DTO
            AuthorAndBorrowersDTO authorAndBorrowersDTO = borrowerMapper.getAuthorAndBorrowersByName("Johannes V. Jensen");
            System.out.println(authorAndBorrowersDTO);
            */

        }
        catch (DatabaseException e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getCause().getMessage());
        }
    }
}