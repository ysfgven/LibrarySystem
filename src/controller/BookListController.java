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
import service.BookHandler;
import view.AddNewBookUI;
import view.BookListUI;
import view.DetailedBookUI;

    import java.util.List;

    public class BookListController {
        private final BookHandler bookHandler;
        private final BookListUI bookListUI;
        private final DetailedBookUI detailedBookUI;

        public BookListController(BookListUI bookListUI, BookHandler bookHandler) {
            this.bookHandler = bookHandler;
            this.bookListUI = bookListUI;
            this.detailedBookUI = new DetailedBookUI(bookHandler);

        }

        public void listBooks(){

            List<Book> allBooks = bookHandler.getBookList();

            ObservableList<String> bookNames = FXCollections.observableArrayList();

            for (Book book : allBooks) {
                bookNames.add(book.getTitle());
            }
            bookListUI.getListView().setItems(bookNames);
            setupListViewEvent(bookListUI.getListView());
            setupRefreshButton();

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
                        VBox detailedVBox = detailedBookUI.detailedBook(selectedBook);
                        bookListUI.getBookList().getItems().set(1, detailedVBox);
                    }
                }
            });
        }

        private void setupRefreshButton() {
            bookListUI.getRefreshButton().setOnAction(event -> {
                bookHandler.refreshFromDisk();
                listBooks();
            });
        }
    }
