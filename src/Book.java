public class Book {

    private final String title;
    private final String author;
    private final String publisher;
    private final String isbn;
    private final String summary;
    private final BookType type;


    public Book(String title, String author, String publisher, String isbn, String summary, BookType type) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.summary = summary;
        this.type = type;

    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getSummary() {
        return summary;
    }

    public BookType getType() {
        return type;
    }

}
