package model;

public class PrintedBook extends Book {

    private final String shelfLocation;
    private final String coverType;

    public PrintedBook(String title, String author, String publisher, String isbn, String summary,BookType type, String shelfLocation, String coverType) {
        super(title, author, publisher, isbn, summary,type);
        this.shelfLocation = shelfLocation;
        this.coverType = coverType;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public String getCoverType() {
        return coverType;
    }
}
