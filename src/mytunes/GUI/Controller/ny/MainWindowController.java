package mytunes.GUI.Controller.ny;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import mytunes.BE.Playlist;
import mytunes.GUI.Model.MediaPlayerModel;
import mytunes.GUI.Model.PlaylistModel;
import mytunes.GUI.Model.SongModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    public VBox librarySection;
    public BorderPane mainWindow;
    public HBox playlistContainer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadLibrarySection();
           // loadPlaylistSection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadLibrarySection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/Library.fxml"));
        VBox vbox = fxmlLoader.load();

        librarySection.getChildren().add(vbox);

        LibraryController libraryController = fxmlLoader.getController();
        libraryController.LoadPlaylistSongsView(mainWindow);
    }
}
