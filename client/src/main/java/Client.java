import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("main.fxml"));
        Parent p =  fxmlLoader.load();

        Scene scene = new Scene(p);
        primaryStage.setScene( scene );

        primaryStage.setTitle("Client");
        primaryStage.setWidth( 320 );
        primaryStage.setHeight(568);
        primaryStage.show();

    }
}