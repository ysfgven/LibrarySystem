package service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.ErrorHandler;
import util.LogHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AssetsHandler {
    private static final String ICON_PATH = "images/icon.png";
    private static final String DEFAULT_BOOK_PATH = "images/defaultBook.png";

    public static ImageView getIconImageView() {


        File file = new File(ICON_PATH);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        try {
            Image img = new Image(file.toURI().toString());
            return new ImageView(img);

        } catch (Exception e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error","Could not find the icon file ","Could not find icon.png ",true);
            return null;
        }

    }

    public static void checkAssets() {
        List<String> missing = new ArrayList<>();
        if (!new File(ICON_PATH).exists()) missing.add("Icon");
        if (!new File(DEFAULT_BOOK_PATH).exists()) missing.add("Default Book Image");

        if (!missing.isEmpty()) {
            String msg = "Found missing files: " + String.join(", ", missing) + "\nProgram will shut down.";
            ErrorHandler.showErrorAlert("Missing Files", "File Error", msg,true);
        }
    }
}
