package entities;

import java.util.ArrayList;
import java.util.List;

public class Author {

    private String name;
    private int id;

    private List<Book> books = new ArrayList<>();

    public Author(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Author(String name) {
        this.name = name;
    }

    public void addBook(Book book){
        books.add(book);
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", books=" + books +
                '}';
    }

    public void removeBook(Book book){
        books.remove(book);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
