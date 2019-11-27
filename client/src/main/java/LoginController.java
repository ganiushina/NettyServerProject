import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
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
    Label lbName;

    boolean auth = false;


    @FXML
    public void auth() {
        Network.sendMsg(new AuthRequest(login.getText(), password.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        Thread t = new Thread(() -> {
            try {
                while (!auth) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof AuthRequest) {
                        AuthRequest fm = (AuthRequest) am;
                        if (fm.isAuth()){
                            updateScene();
                            auth = true;
                        }
                        else {
                            updateLoginPass();
                        }
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void updateScene(){
        updateUI(() -> {
            Parent root = null;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("main.fxml"));
                root = fxmlLoader.load();
                Scene scene = new Scene(root);
                ((Stage)globParent.getScene().getWindow()).setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateLoginPass(){
        updateUI(() -> {
            lbName.setText("Неверный логин или пароль!");
            login.setText("");
            password.setText("");
        });
    }

    public static void updateUI(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }


}
