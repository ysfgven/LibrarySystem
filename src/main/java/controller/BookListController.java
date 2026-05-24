package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Book;
import service.AssetsHandler;
import service.IBookHandler;
import util.ErrorHandler;
import view.AddNewBookUI;
import view.BookListUI;
import view.DetailedBookUI;


public class BookListController {
    private final IBookHandler bookHandler;
    private final BookListUI bookListUI;
    private final DetailedBookUI detailedBookUI;

    public BookListController(BookListUI bookListUI, IBookHandler bookHandler, DetailedBookUI detailedBookUI) {
        this.bookHandler = bookHandler;
        this.bookListUI = bookListUI;
        this.detailedBookUI = detailedBookUI;
    }

    public void init() {
        setupListViewEvent(bookListUI.getListView());
        setupRefreshButton();
        listBooks();
    }

    public void listBooks(){

        ObservableList<String> bookNames = FXCollections.observableArrayList();
        bookHandler.getBookList().forEach(b -> bookNames.add(b.getTitle()));
        bookListUI.getListView().setItems(bookNames);;

    }

    public void onAddButtonClicked() {
        AddNewBookUI addUI = new AddNewBookUI();
        AddNewBookController addController = new AddNewBookController(addUI,bookHandler);

        addUI.setAddNewBookController(addController);
        Pane addPane = addUI.createCommonFields();
        Stage stage = new Stage();
        Scene scene = new Scene(addPane,800,600);
        ImageView iv = AssetsHandler.getIconImageView();
        if (iv != null && iv.getImage() != null) {
            stage.getIcons().add(iv.getImage());
        }
        addController.setStage(stage); //Passing stage to controller
        stage.setScene(scene);
        stage.setTitle("Add Book");
        stage.show();
    }

    private void setupListViewEvent(ListView<String> listView){

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedTitle = listView.getSelectionModel().getSelectedItem();
                if (selectedTitle != null) {
                    Book selectedBook = bookHandler.getBookByTitle(selectedTitle);
                    if (selectedBook == null) {
                        ErrorHandler.showInfoAlert("Warning", "Not Found", "Book could not be found.");
                        return;
                    }
                    VBox detailedVBox = detailedBookUI.detailedBook(selectedBook);
                    bookListUI.getBookList().getItems().set(1, detailedVBox);
                }
            }
        });
    }

    private void setupRefreshButton() {
        bookListUI.getRefreshButton().setOnAction(event -> listBooks());
    }
}
