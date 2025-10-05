import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class LoginScreenUI {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Pane loginScreenPane;

    LoginScreenUI(){
        loginScreenUICreator();

    }

    private Pane loginScreenUICreator( ) {
        loginScreenPane = new Pane();

        usernameField = new TextField();
        passwordField = new PasswordField();

        //Properties
        usernameField.setPromptText("Username");
        usernameField.setLayoutX(500);
        usernameField.setLayoutY(400);
        usernameField.setMinWidth(200);

        passwordField.setPromptText("Password");
        passwordField.setLayoutX(500);
        passwordField.setLayoutY(450);
        passwordField.setMinWidth(200);

        loginButton = new Button("Login");
        loginButton.setLayoutX(550);
        loginButton.setLayoutY(500);
        loginButton.setMinWidth(100);

        ImageView image = AssetsHandler.getIconImageView();
        if (image != null && image.getImage() != null) {
            image.setFitWidth(200);
            image.setFitHeight(200);
            image.setLayoutX(500);
            image.setLayoutY(150);
            loginScreenPane.getChildren().add(image);
        } else {
            Label placeholder = new Label("No Image");
            placeholder.setLayoutX(500);
            placeholder.setLayoutY(150);
            loginScreenPane.getChildren().add(placeholder);
        }

        loginScreenPane.getChildren().addAll(usernameField,passwordField,loginButton);
        return loginScreenPane;
    }

    public Pane getLoginScreenUI(){
        return loginScreenPane;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }
}
