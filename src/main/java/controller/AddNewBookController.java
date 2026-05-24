package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;
import model.Book;
import model.BookType;
import model.OnlineBook;
import model.PrintedBook;
import service.IBookHandler;
import service.ImageManager;
import util.ErrorHandler;
import util.LogHelper;
import view.AddNewBookUI;


public class AddNewBookController {
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String summary;
    private final AddNewBookUI ui;
    private final IBookHandler bookHandler;
    private Stage stage;
    private static final String SIZE_UNIT_MB = "MB";

    public AddNewBookController(AddNewBookUI ui, IBookHandler bookHandler) {
        this.ui = ui;
        this.bookHandler = bookHandler;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean validateMandatoryFields() {

        if (isBlank(ui.getTitleField().getText()) || isBlank(ui.getAuthorField().getText()) ||
                isBlank(ui.getPublisherField().getText()) ||
                isBlank(ui.getIsbnField().getText()) ||
                isBlank(ui.getSummaryField().getText())) {

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

            title = ui.getTitleField().getText().trim();
            author = ui.getAuthorField().getText().trim();
            publisher = ui.getPublisherField().getText().trim();
            isbn = ui.getIsbnField().getText().trim();
            summary = ui.getSummaryField().getText().trim();

            if (!isBlank(isbn)){
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

        String shelfLocation = ui.getShelfLocationField().getText().trim();
        String coverType= ui.getCoverTypeField().getText().trim();

        if (isBlank(shelfLocation) || isBlank(coverType)) {
            ErrorHandler.showInfoAlert("Warning", "Missing Information", "Shelf Location and Cover Type is required for Printed Books.");
            return false;
        }
        PrintedBook printedBook = new PrintedBook(title, author, publisher, isbn, summary, BookType.PRINTED, shelfLocation, coverType);

        try {
            bookHandler.addBook(printedBook);
            return true;
        } catch (NullPointerException e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "Saving Error", "Book could not be saved. Please check the log file.", false);
            return false;
        }
    }

    public boolean readOnlineBookFields() {
        if (!readCommonFields()) return false;

            String sizeText = ui.getFileSizeField().getText().trim();
            String format = ui.getFormatField().getText().trim();
            String unit = ui.getFileSizeUnitBox().getValue();

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
            if (SIZE_UNIT_MB.equalsIgnoreCase(unit))
                fileSize *= 1024;
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
            bookHandler.addBook(onlineBook);
            return true;
        } catch (Exception e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "Saving Error", "Book cannot saved. Please look log file.", false);
            return false;
        }
    }

    public void buttonListener() {
        if (ui == null)
            return;

        ui.getGroup().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            try {
                if (newToggle == ui.getPrintedButton()) {
                    ui.showPrintedFields();
                } else if (newToggle == ui.getOnlineButton()) {
                    ui.showOnlineFields();
                }
            } catch (NullPointerException e) {
                LogHelper.logException(e);
                ErrorHandler.showErrorAlert("Error", "UI Error", "There was an error while changing fields.", true);
            }
        });
    }
        public void addButtonListener(Button button) {
            if(button == null)
                return;

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

                if (!ok)
                    return;

                ErrorHandler.showInfoAlert("Image (Optional)", "Book saved successfully.", "You can now choose a cover image for the book. This step is optional."
                );

                ImageManager.chooseAndSaveImage(stage, isbn);

                if (stage != null)
                    stage.close();
            });

        }
}