package mytunes.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/new/NewMainWindow.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/new/popup/ImportSong.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("MediaPlayer");
        Scene scene = new Scene(root, -1, -1, true, SceneAntialiasing.BALANCED);

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.sizeToScene();

    }
}
