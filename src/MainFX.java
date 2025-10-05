import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        AssetsHandler.checkAssets();

        StaffHandler staffHandler = new StaffHandler();
        staffHandler.createCSVFile();
        staffHandler.readCSVFile();


        final BookHandler bookHandler = new BookHandler();

        LoginScreenUI ui = new LoginScreenUI();
        LoginScreenController controller = new LoginScreenController(ui,primaryStage,bookHandler);
        controller.loginScreenButtonAction();

        Scene scene = new Scene(ui.getLoginScreenUI(), 1200, 800);
        ImageView iconView = AssetsHandler.getIconImageView();
        if (iconView != null && iconView.getImage() != null) {
            primaryStage.getIcons().add(iconView.getImage());
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


