import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BookListUI {

    private SplitPane bookList;
    private ListView<String> listView;
    private BookListController bookListController;
    private Button refreshButton;
    private final Button addBookButton = new Button("Add New Book ");


    BookListUI() {
    }

    public void bookListUIinit(BookListController controller) {
        this.bookListController = controller;
        createBookListPane();
        controller.listBooks();
    }

    public SplitPane createBookListPane() {
        bookList = new SplitPane();
        listView = new ListView<>();
        VBox detailPanel = new VBox();
        refreshButton = new Button("Refresh");

        VBox leftPanel = new VBox(5);

        VBox.setVgrow(listView, Priority.ALWAYS);


        addBookButton.setMaxHeight(30);
        addBookButton.setPrefHeight(30);
        addBookButton.setMinHeight(30);
        addBookButton.setPrefWidth(250);

        refreshButton.setMaxHeight(30);
        refreshButton.setPrefHeight(30);
        refreshButton.setMinHeight(30);
        refreshButton.setPrefWidth(250);

        addBookButton.setOnAction(event -> {bookListController.onAddButtonClicked();});

        leftPanel.getChildren().addAll(listView, addBookButton, refreshButton);

        bookList.setDividerPositions(0.20);

        bookList.getItems().addAll(leftPanel, detailPanel);

        return bookList;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public SplitPane getBookList() {
        return bookList;
    }

    public ListView<String> getListView() {
        return listView;
    }
}
