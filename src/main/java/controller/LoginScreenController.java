package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import service.IBookHandler;
import service.IStaffHandler;
import util.ErrorHandler;
import view.BookListUI;
import view.DetailedBookUI;
import view.LoginScreenUI;


    public class LoginScreenController {

        private final LoginScreenUI ui;
        private String userName;
        private String password;
        private final Stage stage;
        private final IBookHandler bookHandler;
        private final IStaffHandler staffHandler;

        public LoginScreenController(LoginScreenUI ui,Stage stage,IBookHandler bookHandler,IStaffHandler staffHandler) {
            this.ui = ui;
            this.stage = stage;
            this.bookHandler = bookHandler;
            this.staffHandler = staffHandler;
        }

        private void loginScreenControllerReader(){
             userName = ui.getUsernameField().getText();
             password = ui.getPasswordField().getText();
        }

        private boolean loginScreenControllerChecker() {
            if (staffHandler.verify(userName,password)) {
                return true;
            }
            ErrorHandler.showInfoAlert("Login Failed", "Login Failed", "Username or Password Incorrect");
            return false;
        }

        public void loginScreenButtonAction(){
            ui.getLoginButton().setOnAction(e -> {
                loginScreenControllerReader();
                if (loginScreenControllerChecker()) {
                    BookListUI bookListUI = new BookListUI();
                    DetailedBookController detailedBookController = new DetailedBookController(bookHandler);
                    DetailedBookUI detailedBookUI = new DetailedBookUI(detailedBookController);
                    BookListController controller = new BookListController(bookListUI, bookHandler,detailedBookUI);
                    bookListUI.bookListUIinit(controller);
                    controller.init();
                    Scene bookScene = new Scene(bookListUI.getBookList(), 1200, 800);
                    stage.setScene(bookScene);
                    stage.show();
                }
            });
        }
    }
