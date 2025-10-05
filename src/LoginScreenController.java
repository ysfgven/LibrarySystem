    import javafx.scene.Scene;
    import javafx.stage.Stage;
    import java.util.List;

    public class LoginScreenController {

        private final LoginScreenUI ui;
        private String userName;
        private String password;
        private final Stage stage;
        private final BookHandler bookHandler;

        LoginScreenController(LoginScreenUI ui,Stage stage,BookHandler bookHandler) {
            this.ui = ui;
            this.stage = stage;
            this.bookHandler = bookHandler;
        }

        private void loginScreenControllerReader(){
             userName = ui.getUsernameField().getText();
             password = ui.getPasswordField().getText();
        }

        private boolean loginScreenControllerChecker(){

            List<Staff> staffList = Staff.getList();
            boolean loginSuccess = false;
            for(Staff staff : staffList){
                if(staff.getStaffUserName().equals(userName) && staff.getStaffPassword().equals(password)){
                    loginSuccess = true;
                    return loginSuccess;

                }
            }
            if(!loginSuccess){
                ErrorHandler.showInfoAlert("Login Failed", "Login Failed", "Username or Password Incorrect");
                return false;
            }
            return loginSuccess;
        }

        public void loginScreenButtonAction(){
            ui.getLoginButton().setOnAction(e -> {
                loginScreenControllerReader();
                if(loginScreenControllerChecker()){
                    BookListUI bookListUI = new BookListUI();
                    BookListController controller = new BookListController(bookListUI,bookHandler);
                    bookListUI.bookListUIinit(controller);
                    Scene bookScene = new Scene(bookListUI.getBookList(), 1200, 800);
                    stage.setScene(bookScene);
                    stage.show();
                }
            });
        }
    }
