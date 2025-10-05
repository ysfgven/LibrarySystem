import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ErrorHandler {
    public static Alert showInfoAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        try {
            ImageView iv = AssetsHandler.getIconImageView();
            Scene scene = alert.getDialogPane().getScene();
            if (iv != null && iv.getImage() != null && scene != null && scene.getWindow() instanceof Stage) {
                ((Stage) scene.getWindow()).getIcons().add(iv.getImage());
            }
        } catch (Exception  e) {
            LogHelper.logException(e);
        }

        alert.showAndWait();
        return alert;
    }

    public static Alert showErrorAlert(String title, String header, String content, boolean risk) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        try {
            ImageView iv = AssetsHandler.getIconImageView();
            Scene scene = alert.getDialogPane().getScene();
            if (iv != null && iv.getImage() != null && scene != null && scene.getWindow() instanceof Stage) {
                ((Stage) scene.getWindow()).getIcons().add(iv.getImage());
            }
        } catch (Exception e) {
            LogHelper.logException(e);
        }

        alert.showAndWait();
        if (risk) Platform.exit();
        return alert;
    }

}
