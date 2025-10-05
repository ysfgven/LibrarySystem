import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class DetailedBookUI {
    private final VBox detailPanel;
    private final DetailedBookController controller ;
    private final Button deleteBook = new Button(" Delete Book ");

    public DetailedBookUI(BookHandler bookHandler) {
        this.controller = new DetailedBookController(bookHandler);
        detailPanel = new VBox();
    }

    public VBox detailedBook(Book selectedBook) {
        detailPanel.getChildren().clear();
        detailPanel.setPadding(new Insets(12));
        detailPanel.setSpacing(8);

        Label title = new Label("Title: " + selectedBook.getTitle());
        Label author = new Label("Author: " + selectedBook.getAuthor());
        Label publisher = new Label("Publisher: " + selectedBook.getPublisher());
        Label ISBN = new Label("ISBN: " + selectedBook.getIsbn());

        Label summary = new Label("Summary :\n" + selectedBook.getSummary());
        summary.setWrapText(true);
        summary.setMaxWidth(400);

        Label type = new Label("Book Type: " + selectedBook.getType());

        VBox leftTextColumn = new VBox(6, title, author, publisher, ISBN);
        leftTextColumn.setAlignment(Pos.TOP_LEFT);

        ImageView bookImageView = ImageManager.getBookImage(selectedBook.getIsbn());

        HBox topRow;
        if (bookImageView != null) {

            bookImageView.setFitWidth(140);
            bookImageView.setPreserveRatio(true);
            bookImageView.setSmooth(true);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            topRow = new HBox(12, leftTextColumn, spacer, bookImageView);
            topRow.setAlignment(Pos.TOP_LEFT);
        } else {
            topRow = new HBox(leftTextColumn);
            topRow.setAlignment(Pos.TOP_LEFT);
        }

        if (selectedBook instanceof PrintedBook) {
            type.setText("Book type: Printed");
            Label coverType = new Label("Cover type: " + ((PrintedBook) selectedBook).getCoverType());
            Label shelfLocation = new Label("Book Location:  " + ((PrintedBook) selectedBook).getShelfLocation());
            detailPanel.getChildren().addAll(topRow, summary, type, coverType, shelfLocation);
        } else if (selectedBook instanceof OnlineBook) {
            type.setText("Book type: Online");
            int sizeKB = ((OnlineBook) selectedBook).getFileSize();
            String displaySize = sizeKB >= 1024 ? (sizeKB / 1024) + " MB" : sizeKB + " KB";
            Label fileSize = new Label("File Size:  " + displaySize);
            Label format = new Label("File Type:  " + ((OnlineBook) selectedBook).getFormat());
            detailPanel.getChildren().addAll(topRow, summary, type, fileSize, format);
        } else {
            detailPanel.getChildren().addAll(topRow, summary, type);
        }

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS);

        Region hSpacer = new Region();
        HBox.setHgrow(hSpacer, Priority.ALWAYS);
        HBox buttonRow = new HBox(hSpacer, deleteBook);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);
        buttonRow.setPadding(new Insets(6, 12, 12, 12));

        deleteBook.setOnAction(event -> {
            if(controller.onDeleteBookClicked(selectedBook.getTitle(), selectedBook.getIsbn()))
                detailPanel.getChildren().clear();
        });

        detailPanel.getChildren().addAll(vSpacer, buttonRow);

        return detailPanel;
    }




}
