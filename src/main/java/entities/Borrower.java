package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Borrower {

    private String name;
    private String adress;
    private int id;
    private List<Book> books = new ArrayList<>();

    public Borrower(java.lang.String name, java.lang.String adress, int id) {
        this.name = name;
        this.adress = adress;
        this.id = id;
    }

    public Borrower(java.lang.String name, java.lang.String adress) {
        this.name = name;
        this.adress = adress;
    }

    public void addBook(Book book){
        books.add(book);
    }

    public void removeBook(Book book){
        books.remove(book);
    }

    public List getBooks(){
        return books;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getAdress() {
        return adress;
    }

    public void setAdress(java.lang.String adress) {
        this.adress = adress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Borrower{" +
                "name='" + name + '\'' +
                ", adress='" + adress + '\'' +
                ", id=" + id +
                ", books=" + books +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrower borrower = (Borrower) o;
        return id == borrower.id && Objects.equals(name, borrower.name) && Objects.equals(adress, borrower.adress) && Objects.equals(books, borrower.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, adress, id, books);
    }
}
