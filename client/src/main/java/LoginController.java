import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    TextField login;

    @FXML
    PasswordField password;

    @FXML
    VBox globParent;


    @FXML
    public void auth() {
        Network.sendMsg(new AuthRequest(login.getText(), password.getText()));
     //   globParent.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        Network.start();
//        Thread t = new Thread(() -> {
//            try {
//                while (true) {
//                    AbstractMessage am = Network.readObject();
//                    if (am instanceof AuthRequest) {
//                        AuthRequest fm = (AuthRequest) am;
//                        if (fm.isAuth()){
//                            updateScene();
//                        }
//                    }
//                }
//            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
//            } finally {
//                Network.stop();
//            }
//        });
//        t.setDaemon(true);
//        t.start();
//    }
//
//    public void updateScene(){
//        updateUI(() -> {
//            Parent root = null;
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("main.fxml"));
//                root = fxmlLoader.load();
//                Scene scene = new Scene(root);
//                ClientController clientController = fxmlLoader.getController();
//                clientController.initialize(fxmlLoader.getLocation(), null);
//                ((Stage)globParent.getScene().getWindow()).setScene(scene);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//    }
//
//    public static void updateUI(Runnable r) {
//        if (Platform.isFxApplicationThread()) {
//            r.run();
//        } else {
//            Platform.runLater(r);
//        }
//    }


}
