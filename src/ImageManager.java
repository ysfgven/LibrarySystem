import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ImageManager {

    private static final String IMAGE_FOLDER = "images/";
    private static final String DEFAULT_IMAGE = "defaultBook.png";

    public static File chooseAndSaveImage(Stage stage, String isbn) {
        File selectedImage = chooseImageFile(stage);
        saveBookImage(selectedImage, isbn);
        return selectedImage;
    }

    private static File chooseImageFile(Stage stage) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Image (Optional)");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            return fileChooser.showOpenDialog(stage);
        } catch (Exception e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "File selecting error", "There was an error while selecting image.", false);
            return null;
        }
    }

    public static void saveBookImage(File file, String isbn) {
        if (file == null || isbn == null || isbn.isBlank()) {
            ErrorHandler.showInfoAlert("Warning", "ISBN is missing",
                    "Fill the ISBN field to save the image. Book saved, image not saved.");
            return;
        }

        try {
            String name = file.getName();
            int index = name.lastIndexOf('.');
            if (index <= 0 || index == name.length() - 1)
                throw new IllegalArgumentException("File extension is missing or not supported: " + name);

            String extension = name.substring(index + 1).toLowerCase();
            if (!List.of("png", "jpg", "jpeg", "gif").contains(extension))
                throw new IllegalArgumentException("Unsupported file extension: " + extension);

            File targetFile = new File(IMAGE_FOLDER + isbn + "." + extension);
            targetFile.getParentFile().mkdirs();
            Files.copy(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            ErrorHandler.showErrorAlert("Warning", "Copy Error", "File could not be copied", false);
            LogHelper.logException(e);
        } catch (IllegalArgumentException e) {
            ErrorHandler.showInfoAlert("Warning", "Invalid file", e.getMessage());
            LogHelper.logException(e);
        } catch (Exception e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "Image Error", "Unexpected error while saving image", false);
        }
    }

    public static ImageView getBookImage(String ISBN) {
        String[] extensions = {".jpg", ".jpeg", ".png"};

        try {
            for (String ext : extensions) {
                File file = new File(IMAGE_FOLDER + ISBN + ext);
                if (file.exists()) return createImageView(file);
            }

            File defaultFile = new File(IMAGE_FOLDER + DEFAULT_IMAGE);
            if (defaultFile.exists()) return createImageView(defaultFile);

        } catch (RuntimeException e) {
            LogHelper.logException(e);
        }
        return null;
    }

    private static ImageView createImageView(File file) {
        Image img = new Image(file.toURI().toString());
        ImageView iv = new ImageView(img);
        iv.setFitWidth(120);
        iv.setPreserveRatio(true);
        return iv;
    }
    public static void deleteBookImage(String isbn) {
        if (isbn == null || isbn.isBlank()) return;

        String[] extensions = {".jpg", ".jpeg", ".png", ".gif"};

        for (String ext : extensions) {
            File file = new File(IMAGE_FOLDER + isbn + ext);
            if (file.exists()) {
                try {
                    if (!file.delete()) {
                        LogHelper.logException(new Exception("Failed to delete image: " + file.getAbsolutePath()));
                    }
                } catch (SecurityException e) {
                    LogHelper.logException(e);
                    ErrorHandler.showErrorAlert("Error", "Delete Error", "No permission to delete image file: " + file.getName(), false);
                }
            }
        }
    }

}
