package mytunes.GUI.Controller.ny.PopUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mytunes.BE.Playlist;
import mytunes.GUI.Controller.ny.PlaylistController;
import mytunes.GUI.Model.PlaylistModel;

import java.net.URL;
import java.util.ResourceBundle;

public class EditPlaylistController implements Initializable {

    @FXML
    private TextField txtPlaylistName;

    @FXML
    private Button fxBtnEditPlaylist;

    private PlaylistModel playlistModel;
    private PlaylistController playlistController;

    public EditPlaylistController() throws Exception {
        playlistModel = new PlaylistModel();
    }

    private void enableTextField() {
        txtPlaylistName.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                fxBtnEditPlaylist.setDisable(newValue.trim().isEmpty() || newValue.trim().length() == 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void setParentController(PlaylistController controller) {
        playlistController = controller;
    }

    public void btnEditPlaylist(ActionEvent actionEvent) throws Exception {
        Playlist editedPlaylist = playlistModel.editPlaylist(playlistController.getPlaylist(), txtPlaylistName.getText());
        playlistController.setPlaylist(editedPlaylist);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enableTextField();
    }
}
