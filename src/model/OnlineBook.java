package model;

public class OnlineBook extends Book {

    private final int fileSize;
    private final String format;

    public OnlineBook(String title, String author, String publisher, String isbn, String summary,BookType type, int fileSize, String format) {
        super(title, author, publisher, isbn, summary,type);
        this.fileSize = fileSize;
        this.format = format;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getFormat() {
        return format;
    }
}
