package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Borrower {

    private String name;
    private String address;
    private int id;
    private List<Book> books = new ArrayList<>();

    public Borrower(String name, String address, int id) {
        this.name = name;
        this.address = address;
        this.id = id;
    }

    public Borrower(String name, String address) {
        this.name = name;
        this.address = address;
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

    public java.lang.String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
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
                ", adress='" + address + '\'' +
                ", id=" + id +
                ", books=" + books +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrower borrower = (Borrower) o;
        return id == borrower.id && Objects.equals(name, borrower.name) && Objects.equals(address, borrower.address) && Objects.equals(books, borrower.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, id, books);
    }
}
