package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Book;
import model.BookType;
import model.OnlineBook;
import model.PrintedBook;
import service.BookHandler;
import service.ImageManager;
import util.ErrorHandler;
import util.LogHelper;
import view.AddNewBookUI;

import java.io.File;

public class AddNewBookController {

    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String summary;
    private final AddNewBookUI ui;
    private final BookHandler bookHandler;
    private Stage stage;

    public AddNewBookController(AddNewBookUI ui, BookHandler bookHandler) {
        this.ui = ui;
        this.bookHandler = bookHandler;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String getTextSafe(javafx.scene.control.TextField tf) {
        return (tf == null) ? null : tf.getText();
    }

    private String safeTrim(String s) {
        return s == null ? null : s.trim();
    }

    private boolean validateMandatoryFields() {
        if (ui == null) {
            LogHelper.logException(new IllegalStateException("UI is null in AddNewBookController"));
            ErrorHandler.showErrorAlert("Error", "Error", "UI did not start.", true);
            return false;
        }

        if (bookHandler == null) {
            LogHelper.logException(new IllegalStateException("BookHandler is null in AddNewBookController"));
            ErrorHandler.showErrorAlert("Error", "Data Error", "BookHandler is not available.", true);
            return false;
        }

        if (isBlank(getTextSafe(ui.getTitleField())) ||
                isBlank(getTextSafe(ui.getAuthorField())) ||
                isBlank(getTextSafe(ui.getPublisherField())) ||
                isBlank(getTextSafe(ui.getIsbnField())) ||
                isBlank(getTextSafe(ui.getSummaryField()))) {

            ErrorHandler.showInfoAlert("Warning", "Missing Information", "Please fill all the required fields.");
            return false;
        }

        Toggle selected = ui.getGroup() == null ? null : ui.getGroup().getSelectedToggle();
        if (selected == null) {
            ErrorHandler.showInfoAlert("Warning", "Type not selected", "Please select a book type.");
            return false;
        }

        return true;
    }

    private boolean readCommonFields() {

        if (!validateMandatoryFields())
            return false;

        title = safeTrim(getTextSafe(ui.getTitleField()));
        author = safeTrim(getTextSafe(ui.getAuthorField()));
        publisher = safeTrim(getTextSafe(ui.getPublisherField()));
        isbn = safeTrim(getTextSafe(ui.getIsbnField()));
        summary = safeTrim(getTextSafe(ui.getSummaryField()));

        if (!isBlank(isbn)) {
            try {
                Book existingBook = bookHandler.getBookByIsbn(isbn);
                if (existingBook != null) {
                    ErrorHandler.showInfoAlert("Warning", "Book already exists", "There is a book with same ISBN number.");
                    return false;
                }
            } catch (Exception e) {
                LogHelper.logException(e);
                ErrorHandler.showErrorAlert("Error", "Check Error", "There was a problem checking the ISBN number.", false);
                return false;
            }
        }
        return true;
    }


    public boolean readPrintedBookFields() {
        if (!readCommonFields()) return false;

        String shelfLocation = safeTrim(getTextSafe(ui.getShelfLocationField()));
        String coverType = safeTrim(getTextSafe(ui.getCoverTypeField()));

        if (isBlank(shelfLocation) || isBlank(coverType)) {
            ErrorHandler.showInfoAlert("Warning", "Missing Information", "Shelf Location and Cover Type is required for Printed Books.");
            return false;
        }

        PrintedBook printedBook = new PrintedBook(title, author, publisher, isbn, summary, BookType.PRINTED, shelfLocation, coverType);

        try {
            if (bookHandler.getBookList() == null) {
                ErrorHandler.showErrorAlert("Error", "Data Error", "Can't get book list.", false);
                return false;
            }
            bookHandler.getBookList().add(printedBook);
            bookHandler.addBooksAtomically();
            return true;
        } catch (NullPointerException e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "Saving Error", "Book could not be saved. Please check the log file.", false);
            return false;
        }
    }

    public boolean readOnlineBookFields() {
        if (!readCommonFields()) return false;

        String sizeText = safeTrim(getTextSafe(ui.getFileSizeField()));
        String format = safeTrim(getTextSafe(ui.getFormatField()));
        String unit = ui.getFileSizeUnitBox() != null ? ui.getFileSizeUnitBox().getValue() : null;

        if (isBlank(sizeText)) {
            ErrorHandler.showInfoAlert("Warning", "Missing Information", "Please fill the file size field.");
            return false;
        }
        if (isBlank(format)) {
            ErrorHandler.showInfoAlert("Warning", "Missing Information", "Please fill the format field.");
            return false;
        }

        int fileSize;
        try {
            fileSize = Integer.parseInt(sizeText.trim());
            if ("MB".equalsIgnoreCase(unit)) fileSize *= 1024;
            if (fileSize < 0) {
                ErrorHandler.showInfoAlert("Warning", "Invalid input", "File size cannot be negative.");
                return false;
            }
        } catch (NumberFormatException nfe) {
            LogHelper.logException(nfe);
            ErrorHandler.showErrorAlert("Warning", "Invalid input", "File size must be a number.", false);
            return false;
        } catch (Exception e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Warning", "Process Error", "There was an error while converting size", false);
            return false;
        }

        OnlineBook onlineBook = new OnlineBook(title, author, publisher, isbn, summary, BookType.ONLINE, fileSize, format);

        try {
            if (bookHandler.getBookList() == null) {
                ErrorHandler.showErrorAlert("Error", "Data Error", "Can't get book list.", false);
                return false;
            }
            bookHandler.getBookList().add(onlineBook);
            bookHandler.addBooksAtomically();
            return true;
        } catch (Exception e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "Saving Error", "Book cannot saved. Please look log file.", false);
            return false;
        }
    }

    public void buttonListener(Pane pane) {
        if (ui == null || pane == null) return;

        ui.getGroup().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            try {
                if (newToggle == ui.getPrintedButton()) {
                    if (ui.getFileSizeField() != null) pane.getChildren().remove(ui.getFileSizeField());
                    if (ui.getFileSizeUnitBox() != null) pane.getChildren().remove(ui.getFileSizeUnitBox());
                    if (ui.getFormatField() != null) pane.getChildren().remove(ui.getFormatField());
                    ui.createPrintedFields(pane);
                } else if (newToggle == ui.getOnlineButton()) {
                    if (ui.getCoverTypeField() != null) pane.getChildren().remove(ui.getCoverTypeField());
                    if (ui.getShelfLocationField() != null) pane.getChildren().remove(ui.getShelfLocationField());
                    ui.createOnlineFields(pane);
                }
            } catch (NullPointerException e) {
                LogHelper.logException(e);
                ErrorHandler.showErrorAlert("Error", "UI Error", "There was an error while changing fields.", true);
            }
        });
    }

    public void addButtonListener(Button button) {
        if (button == null) return;

        button.setOnAction(event -> {
            boolean ok;
            try {
                if (ui.getPrintedButton().isSelected()) {
                    ok = readPrintedBookFields();
                } else {
                    ok = readOnlineBookFields();
                }
            } catch (Exception e) {
                LogHelper.logException(e);
                ErrorHandler.showErrorAlert("Error", "Unexpected Error", "There was an error while reading fields.", false);
                return;
            }

            if (!ok) return;

            File selectedImage = ImageManager.chooseAndSaveImage(stage, safeTrim(getTextSafe(ui.getIsbnField())));

            if (stage != null) stage.close();
        });
    }
}
