package mytunes.GUI.Controller.ny;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mytunes.BE.Playlist;
import mytunes.GUI.Model.PlaylistModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {

    @FXML
    private VBox boxPlaylists;

    private PlaylistModel playlistModel;

    private PlaylistContainer playlistContainer;

    public LibraryController() {
        try {
            playlistModel = new PlaylistModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkPlaylistClick(Button btn, Playlist p) throws IOException {
        btn.setOnAction(e -> {
            try {
                playlistContainer.tablePlaylistSongsClick(p);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });
    }

    private void addToPlaylist() throws Exception {
        for (Playlist p : playlistModel.getPlaylists()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/new/Playlists.fxml"));

            Button button = fxmlLoader.load();
            PlaylistController playlistController = fxmlLoader.getController();
            playlistController.setPlaylist(p);

            boxPlaylists.getChildren().add(button);

            checkPlaylistClick(button, p);
        }
    }

    private AnchorPane rootLayout;

    public void LoadPlaylistSongsView(BorderPane mainWindow) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/new/PlaylistContainer.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        playlistContainer = fxmlLoader.getController();

        mainWindow.setCenter(anchorPane);
       // hbox.getChildren().add(anchorPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            addToPlaylist();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
