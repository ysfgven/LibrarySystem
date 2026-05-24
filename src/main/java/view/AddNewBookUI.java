package view;

import controller.AddNewBookController;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class AddNewBookUI {

    private AddNewBookController addNewBookController;

    private TextField titleField;
    private TextField authorField;
    private TextField publisherField;
    private TextField isbnField;
    private TextField summaryField;
    private TextField coverTypeField;
    private TextField shelfLocationField;
    private TextField fileSizeField;
    private TextField formatField;
    private final RadioButton printedButton = new RadioButton("PRINTED");
    private final RadioButton onlineButton = new RadioButton("ONLINE");
    private final ToggleGroup group = new ToggleGroup();
    private ComboBox<String> fileSizeUnitBox;
    private Pane addNewBookUIPane;
    private static final String SIZE_UNIT_MB = "MB";
    private static final String SIZE_UNIT_KB = "KB";

    private final static int LAYOUT_X = 100;
    private final static int LAYOUT_Y = 100;


    public Pane createCommonFields() {
        addNewBookUIPane = new Pane();

        titleField = new TextField();
        authorField = new TextField();
        publisherField = new TextField();
        isbnField = new TextField();
        summaryField = new TextField();

        printedButton.setToggleGroup(group);
        onlineButton.setToggleGroup(group);

        addNewBookController.buttonListener();
        Button addButton = new Button("Add");

        titleField.setPromptText("Title");
        titleField.setLayoutX(LAYOUT_X);
        titleField.setLayoutY(LAYOUT_Y);

        authorField.setPromptText("Author");
        authorField.setLayoutX(LAYOUT_X);
        authorField.setLayoutY(LAYOUT_Y+30);

        publisherField.setPromptText("Publisher");
        publisherField.setLayoutX(LAYOUT_X);
        publisherField.setLayoutY(LAYOUT_Y+60);

        isbnField.setPromptText("ISBN");
        isbnField.setLayoutX(LAYOUT_X);
        isbnField.setLayoutY(LAYOUT_Y+90);

        summaryField.setPromptText("Summary");
        summaryField.setLayoutX(400);
        summaryField.setLayoutY(LAYOUT_Y);
        summaryField.setPrefSize(300,275);

        onlineButton.setLayoutX(LAYOUT_X);
        onlineButton.setLayoutY(LAYOUT_Y+200);

        printedButton.setLayoutX(LAYOUT_X);
        printedButton.setLayoutY(LAYOUT_Y+225);

        addButton.setLayoutX(LAYOUT_X);
        addButton.setLayoutY(LAYOUT_Y+250);
        addButton.setPrefSize(100,25);

        addNewBookController.addButtonListener(addButton);

        addNewBookUIPane.getChildren().addAll(titleField,authorField,publisherField,isbnField,summaryField,onlineButton,printedButton,addButton);
        return addNewBookUIPane;
    }

    public void showPrintedFields() {
        if (fileSizeField != null)   addNewBookUIPane.getChildren().remove(fileSizeField);
        if (fileSizeUnitBox != null) addNewBookUIPane.getChildren().remove(fileSizeUnitBox);
        if (formatField != null)     addNewBookUIPane.getChildren().remove(formatField);
        createPrintedFields(addNewBookUIPane);
    }

    public void showOnlineFields() {
        if (coverTypeField != null)     addNewBookUIPane.getChildren().remove(coverTypeField);
        if (shelfLocationField != null) addNewBookUIPane.getChildren().remove(shelfLocationField);
        createOnlineFields(addNewBookUIPane);
    }

    public void createOnlineFields(Pane pane) {
        fileSizeField = new TextField();
        formatField = new TextField();
        fileSizeUnitBox = new ComboBox<>();

        fileSizeField.setPromptText("File Size");
        fileSizeField.setLayoutX(LAYOUT_X);
        fileSizeField.setLayoutY(LAYOUT_Y+130);

        fileSizeUnitBox.getItems().addAll(SIZE_UNIT_KB, SIZE_UNIT_MB);
        fileSizeUnitBox.setValue(SIZE_UNIT_MB);
        fileSizeUnitBox.setLayoutX(LAYOUT_X+160);
        fileSizeUnitBox.setLayoutY(LAYOUT_Y+130);

        formatField.setPromptText("Format");
        formatField.setLayoutX(LAYOUT_X);
        formatField.setLayoutY(LAYOUT_Y+160);

        pane.getChildren().addAll(fileSizeField,fileSizeUnitBox,formatField);
    }

    public void createPrintedFields(Pane pane) {
        coverTypeField = new TextField();
        shelfLocationField = new TextField();

        coverTypeField.setPromptText("Cover Type: ");
        coverTypeField.setLayoutX(LAYOUT_X);
        coverTypeField.setLayoutY(LAYOUT_Y+130);

        shelfLocationField.setPromptText("Shelf Location: ");
        shelfLocationField.setLayoutX(LAYOUT_X);
        shelfLocationField.setLayoutY(LAYOUT_Y+160);

        pane.getChildren().addAll(coverTypeField,shelfLocationField);
    }

    public TextField getTitleField() {
        return titleField;
    }
    public TextField getAuthorField() {
        return authorField;
    }
    public TextField getPublisherField() {
        return publisherField;
    }
    public TextField getIsbnField() {
        return isbnField;
    }
    public TextField getSummaryField() {
        return summaryField;
    }
    public TextField getCoverTypeField() {
        return coverTypeField;
    }
    public TextField getShelfLocationField() {
        return shelfLocationField;
    }
    public TextField getFileSizeField() {
        return fileSizeField;
    }
    public TextField getFormatField() {
        return formatField;
    }
    public RadioButton getPrintedButton() {
        return printedButton;
    }
    public RadioButton getOnlineButton() {
        return onlineButton;
    }
    public ToggleGroup getGroup() {
        return group;
    }
    public ComboBox<String> getFileSizeUnitBox() {
        return fileSizeUnitBox;
    }
    public void setAddNewBookController(AddNewBookController controller) {
        this.addNewBookController = controller;
    }
}