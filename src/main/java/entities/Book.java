package entities;

public class Book {

    private int id;
    private String titel;
    private int releaseYear;
    private String isbn;
    private Author author;

    public Book(int id, String titel, int releaseYear, String isbn, Author author) {
        this.id = id;
        this.titel = titel;
        this.releaseYear = releaseYear;
        this.isbn = isbn;
        this.author = author;
    }

    public Book(String titel, int releaseYear, String isbn, Author author) {
        this.titel = titel;
        this.releaseYear = releaseYear;
        this.isbn = isbn;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
