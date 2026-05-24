package service;

import db.DatabaseManager;
import model.*;
import util.ErrorHandler;
import util.LogHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookHandler implements IBookHandler {

    private static final String BASE_QUERY = """
        SELECT b.*,
               p.shelf_location, p.cover_type,
               o.file_size, o.format
        FROM books b
        LEFT JOIN printed_books p ON b.id = p.id
        LEFT JOIN online_books  o ON b.id = o.id
    """;

    public List<Book> getBookList() {
        List<Book> result = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(BASE_QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                if (book != null) result.add(book);
            }

        } catch (SQLException e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "DB Error", "Books could not be loaded.", false);
        }
        return result;
    }
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        String typeStr = rs.getString("type");
        String title = rs.getString("title");
        String author = rs.getString("author");
        String publisher = rs.getString("publisher");
        String isbn = rs.getString("isbn");
        String summary = rs.getString("summary");

        BookType type;
        try {
            type = BookType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            LogHelper.logException(e);
            return null;
        }

        return switch(type) {
            case PRINTED -> new PrintedBook(
                    title, author, publisher, isbn, summary, type,
                    rs.getString("shelf_location"),
                    rs.getString("cover_type")
            );
            case ONLINE -> new OnlineBook(
                    title, author, publisher, isbn, summary, type,
                    rs.getInt("file_size"),
                    rs.getString("format")
            );
        };
    }
    public boolean addBook(Book book) {
        String insertBook = """
            INSERT INTO books (type, title, author, publisher, isbn, summary)VALUES (?, ?, ?, ?, ?, ?)
        """;
        String insertPrinted = """
            INSERT INTO printed_books (id, shelf_location, cover_type) VALUES (?, ?, ?)
        """;
        String insertOnline = """
            INSERT INTO online_books (id, file_size, format) VALUES (?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(insertBook, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,book.getType().name());
                ps.setString(2,book.getTitle());
                ps.setString(3,book.getAuthor());
                ps.setString(4,book.getPublisher());
                ps.setString(5,book.getIsbn());
                ps.setString(6,book.getSummary());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                if (!keys.next()) throw new SQLException("No generated key returned.");
                int generatedId = keys.getInt(1);

                switch (book.getType()) {
                    case PRINTED -> {
                        PrintedBook pb = (PrintedBook) book;
                        try (PreparedStatement ps2 = conn.prepareStatement(insertPrinted)) {
                            ps2.setInt(1, generatedId);
                            ps2.setString(2, pb.getShelfLocation());
                            ps2.setString(3, pb.getCoverType());
                            ps2.executeUpdate();
                        }
                    }
                    case ONLINE -> {
                        OnlineBook ob = (OnlineBook) book;
                        try (PreparedStatement ps2 = conn.prepareStatement(insertOnline)) {
                            ps2.setInt(1, generatedId);
                            ps2.setInt(2, ob.getFileSize());
                            ps2.setString(3, ob.getFormat());
                            ps2.executeUpdate();
                        }
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "DB Error", "Book could not be saved.", false);
            return false;
        }
    }
    public boolean deleteBookByTitle(String title, String isbn) {
        if (title == null || isbn == null)
            return false;
        String query = "DELETE FROM books WHERE title = ? AND isbn = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, title.trim());
            ps.setString(2, isbn.trim());

            int affected = ps.executeUpdate();
            if (affected == 0)
                return false;

            ImageManager.deleteBookImage(isbn);
            return true;

        } catch (SQLException e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "DB Error", "Book could not be deleted.", false);
            return false;
        }
    }
    public Book getBookByIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) return null;
        return querySingleBook(BASE_QUERY + " WHERE b.isbn = ?", isbn.trim());
    }
    public Book getBookByTitle(String title) {
        if (title == null || title.isBlank()) return null;
        return querySingleBook(BASE_QUERY + " WHERE b.title = ?", title.trim());
    }
    private Book querySingleBook(String query, String param) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, param);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSetToBook(rs);
            }

        } catch (SQLException e) {
            LogHelper.logException(e);
        }
        return null;
    }
}