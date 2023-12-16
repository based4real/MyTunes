package mytunes.GUI.Controller.ny.PopUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mytunes.BE.Playlist;
import mytunes.GUI.Controller.ny.Containers.PlaylistContainer;
import mytunes.GUI.Controller.ny.Custom.Notification;
import mytunes.GUI.Model.PlaylistModel;

import java.net.URL;
import java.util.ResourceBundle;

public class EditPlaylistController implements Initializable {

    @FXML
    private TextField txtPlaylistName;

    @FXML
    private Button fxBtnEditPlaylist;

    private PlaylistModel playlistModel;
    private PlaylistContainer playlistContainer;

    public EditPlaylistController() throws Exception {
        playlistModel = PlaylistModel.getInstance();
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

    public void setParentController(PlaylistContainer controller) {
        playlistContainer = controller;
    }

    public void btnEditPlaylist(ActionEvent actionEvent) throws Exception {
        Playlist editedPlaylist = playlistModel.editPlaylist(playlistContainer.getPlaylist(), txtPlaylistName.getText());
        playlistContainer.setPlaylist(editedPlaylist);
        Notification.playlistEdited(editedPlaylist);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enableTextField();
    }
}
