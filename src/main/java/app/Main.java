package app;

import dto.AuthorAndBorrowersDTO;
import entities.Author;
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
            Borrower borrower = borrowerMapper.getBorrowerById(1);
            System.out.println(borrower);

            // Opgave 2: Find alle lånere, og vis deres data inklusive postnummer og by.
            /* List<Borrower> borrowers = borrowerMapper.getAllBorrowers();
            System.out.println(borrowers); */

            //Opgave 3: Find alle bøger, og deres forfattere
            /* List<Book> books = bookMapper.getAllBookWithAuthors();
            System.out.println(books);*/

            // Opgave 4: Find alle lånere og de bøger de har lånt. Medtag også bogtitler og evt. forfatter
            /*List<Borrower> borrowersWithBooks = borrowerMapper.getAllBorrowerAndBooks();
            System.out.println(borrowersWithBooks);*/

            // DML Opgave 1: Indsæt en ny låner (insert)
            /*Borrower borrower = new Borrower("Signe", "Gladsaxevej 157 2860 Søborg");
            boolean success = borrowerMapper.insertBorrower(borrower);
            if (success)
                System.out.println(borrower);
            else
                System.out.println("insert failed");*/

            // DML Opgave 2: Opret et nyt udlån af en bog (insert)
            /*Book book = new Book(1, "Den lange rejse", 1977, new Author("Johannes V. Jensen", 2));
            Borrower borrower = new Borrower("Mattias Bruun", "Ellevang 12 7490 Aulum", 3);
            boolean success = borrowerMapper.insertLoan(borrower, book);
            if (success)
                System.out.println("insert succeded");
            else
                System.out.println("insert failed");*/

            // DML Opgave 3: Fjern et udlån (delete)
            /*Book book = new Book(1, "Den lange rejse", 1977, new Author("Johannes V. Jensen", 2));
            Borrower borrower = new Borrower("Mattias Bruun", "Ellevang 12 7490 Aulum", 3);
            boolean success = borrowerMapper.deleteLoan(borrower, book);
            if (success)
                System.out.println("delete succeded");
            else
                System.out.println("delete failed");*/

            // DML Opgave 4: Rediger en bogtitel (update)
            /*boolean success = bookMapper.updateBookTitle(2, "Sommerhistorier");
            if (success)
                System.out.println("update succeded");
            else
                System.out.println("update failed");*/

            // Fjern en låner (og tilhørende udlån)
            /*
            boolean deleted = borrowerMapper.deleteBorrower(1);
            if (deleted)
                System.out.println("Borrower with id " + 1 + " is succcesfully deleted");
            else
                System.out.println("delete failed");
            */

             // Et kald som returnere en DTO
            /*
            AuthorAndBorrowersDTO authorAndBorrowersDTO = borrowerMapper.getAuthorAndBorrowersByName("Johannes V. Jensen");
            System.out.println(authorAndBorrowersDTO);
            */


        }
        catch (DatabaseException e)
        {
            System.out.println(e.getMessage());

        }
    }
}