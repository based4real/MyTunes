package mytunes.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;
import mytunes.BE.Album;
import mytunes.BLL.AlbumManager;
import mytunes.GUI.Model.AlbumModel;

import java.util.List;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewMainWindow.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/MediaPlayerWindow.fxml"));


        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/new/popup/ImportSong.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("MediaPlayer");
        Scene scene = new Scene(root, -1, -1, true, SceneAntialiasing.BALANCED);

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.sizeToScene();
    }
}
