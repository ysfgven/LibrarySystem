package view;

import controller.DetailedBookController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Book;
import model.OnlineBook;
import model.PrintedBook;
import service.BookHandler;
import service.ImageManager;

public class DetailedBookUI {

    private final VBox detailPanel;
    private final DetailedBookController controller;
    private final Button deleteBook = new Button(" Delete Book ");

    public DetailedBookUI(DetailedBookController controller) {
        this.controller = controller;
        this.detailPanel = new VBox();
    }

    public VBox detailedBook(Book selectedBook) {
        detailPanel.getChildren().clear();
        detailPanel.setPadding(new Insets(12));
        detailPanel.setSpacing(8);

        HBox topRow = buildTopRow(selectedBook);
        Label summary = buildSummaryLabel(selectedBook);
        VBox typeFields = buildTypeFields(selectedBook);
        Region vSpacer = buildVerticalSpacer();
        HBox buttonRow = buildButtonRow(selectedBook);

        detailPanel.getChildren().addAll(topRow, summary, typeFields, vSpacer, buttonRow);
        return detailPanel;
    }

    private HBox buildTopRow(Book book) {
        Label title = new Label("Title: "     + book.getTitle());
        Label author = new Label("Author: "    + book.getAuthor());
        Label publisher = new Label("Publisher: " + book.getPublisher());
        Label isbn = new Label("ISBN: "      + book.getIsbn());

        VBox leftTextColumn = new VBox(6, title, author, publisher, isbn);
        leftTextColumn.setAlignment(Pos.TOP_LEFT);

        ImageView bookImageView = ImageManager.getBookImage(book.getIsbn());

        if (bookImageView != null) {
            bookImageView.setFitWidth(140);
            bookImageView.setPreserveRatio(true);
            bookImageView.setSmooth(true);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox topRow = new HBox(12, leftTextColumn, spacer, bookImageView);
            topRow.setAlignment(Pos.TOP_LEFT);
            return topRow;
        }

        HBox topRow = new HBox(leftTextColumn);
        topRow.setAlignment(Pos.TOP_LEFT);
        return topRow;
    }

    private Label buildSummaryLabel(Book book) {
        Label summary = new Label("Summary:\n" + book.getSummary());
        summary.setWrapText(true);
        summary.setMaxWidth(400);
        return summary;
    }

    private VBox buildTypeFields(Book book) {
        return switch (book.getType()) {
            case PRINTED -> buildPrintedFields((PrintedBook) book);
            case ONLINE  -> buildOnlineFields((OnlineBook) book);
        };
    }
    private VBox buildPrintedFields(PrintedBook book) {
        Label type = new Label("Book type: Printed");
        Label coverType = new Label("Cover type: "    + book.getCoverType());
        Label shelfLocation = new Label("Book Location: " + book.getShelfLocation());
        return new VBox(6, type, coverType, shelfLocation);
    }
    private VBox buildOnlineFields(OnlineBook book) {
        Label type= new Label("Book type: Online");
        int sizeKB = book.getFileSize();
        String display= sizeKB >= 1024 ? (sizeKB / 1024) + " MB" : sizeKB + " KB";
        Label fileSize = new Label("File Size: " + display);
        Label format = new Label("File Type: " + book.getFormat());
        return new VBox(6, type, fileSize, format);
    }

    private Region buildVerticalSpacer() {
        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);
        return vSpacer;
    }

    private HBox buildButtonRow(Book book) {
        deleteBook.setOnAction(event -> {
            if (controller.onDeleteBookClicked(book.getTitle(), book.getIsbn()))
                detailPanel.getChildren().clear();});

        Region hSpacer = new Region();
        HBox.setHgrow(hSpacer, Priority.ALWAYS);
        HBox buttonRow = new HBox(hSpacer, deleteBook);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);
        buttonRow.setPadding(new Insets(6, 12, 12, 12));
        return buttonRow;
    }
}