package dto;

import entities.*;

import java.util.List;

public class AuthorAndBorrowersDTO {

    private Author author;
    private List<Borrower> borrowerList;

    public AuthorAndBorrowersDTO(Author author, List<Borrower> borrowerList) {
        this.author = author;
        this.borrowerList = borrowerList;
    }

    public Author getAuthor() {
        return author;
    }


    public List<Borrower> getBorrowers(){
        return borrowerList;
    }

    @Override
    public String toString() {
        return "AuthorAndBorrowersDTO{" +
                "author=" + author +
                ", borrowerList=" + borrowerList +
                '}';
    }
}
