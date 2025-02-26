package entities;

public class Book {

    private int id;
    private String titel;
    private int releaseYear;

    private Author author;

    public Book(int id, String titel, int releaseYear,  Author author) {
        this.id = id;
        this.titel = titel;
        this.releaseYear = releaseYear;
        this.author = author;
    }

    public Book(String titel, int releaseYear, Author author) {
        this.titel = titel;
        this.releaseYear = releaseYear;
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


    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", titel='" + titel + '\'' +
                ", releaseYear=" + releaseYear +
                ", author=" + author.getName() +
                '}';
    }
}
