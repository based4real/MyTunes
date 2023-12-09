package mytunes.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.GUI.Model.MediaPlayerModel;
import mytunes.GUI.Model.PlaylistModel;
import mytunes.GUI.Model.SongModel;

public class NewPlaylistWindowController {

    @FXML
    private TextField txtPlaylistName;

    private PlaylistModel playlistModel;

    private MediaPlayerViewController controller;
    private Stage stage;

    public void setParentController(MediaPlayerViewController controller) {
        this.controller = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public NewPlaylistWindowController() throws Exception {
        playlistModel = PlaylistModel.getInstance();
    }
    public void btnCreatePlaylist(ActionEvent actionEvent) throws Exception {
        playlistModel.createPlaylist(txtPlaylistName.getText());
    }
}
