package service;

import model.Book;
import java.util.List;

public interface IBookHandler {
    List<Book> getBookList();
    boolean addBook(Book book);
    boolean deleteBookByTitle(String title, String isbn);
    Book getBookByIsbn(String isbn);
    Book getBookByTitle(String title);
}