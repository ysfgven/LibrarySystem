import db.DatabaseManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import service.AssetsHandler;
import service.BookHandler;
import service.StaffHandler;
import view.LoginScreenUI;
import controller.LoginScreenController;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) {
        AssetsHandler.checkAssets();

        LoginScreenUI loginScreenUI = new LoginScreenUI();
        StaffHandler staffHandler = new StaffHandler();
        BookHandler bookHandler = new BookHandler();

        LoginScreenController loginScreenController = new LoginScreenController(loginScreenUI, stage, bookHandler, staffHandler);
        loginScreenController.loginScreenButtonAction();

        ImageView iv = AssetsHandler.getIconImageView();
        if (iv != null && iv.getImage() != null) {
            stage.getIcons().add(iv.getImage());
        }

        Scene scene = new Scene(loginScreenUI.getLoginScreenUI(), 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Library Management System");
        stage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}