package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import service.AssetsHandler;
import service.BookHandler;
import util.ErrorHandler;

import java.util.Optional;

public class DetailedBookController {


    private final BookHandler bookHandler;

    public DetailedBookController( BookHandler bookHandler) {
        this.bookHandler = bookHandler;
    }

    public boolean onDeleteBookClicked(String title,String isbn) {

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Delete Book");
        Stage stage = (Stage) conf.getDialogPane().getScene().getWindow();
        stage.getIcons().add(AssetsHandler.getIconImageView().getImage());
        conf.setHeaderText("Are you sure deleting this book?");
        conf.setContentText("Title: " + title + "\nISBN: " + isbn);

        Optional<ButtonType> result = conf.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            boolean deleted = bookHandler.deleteBookByTitle(title, isbn);
            if (deleted) {
                ErrorHandler.showInfoAlert("Success", "Deleted", "Book successfully deleted.");
                return true;
            } else {
                ErrorHandler.showInfoAlert("Warning", "Unsuccessful", "Title/ISBN couldn't match.");
                return false;
            }
        }
        return false;
    }
}
