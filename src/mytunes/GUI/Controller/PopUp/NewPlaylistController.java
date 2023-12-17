package mytunes.GUI.Controller.PopUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mytunes.BE.Playlist;
import mytunes.GUI.Controller.Containers.LibraryContainer;
import mytunes.GUI.Model.PlaylistModel;

import java.net.URL;
import java.util.ResourceBundle;

public class NewPlaylistController implements Initializable {

    @FXML
    private TextField txtPlaylistName;

    @FXML
    private Button fxBtnNewPlaylist;

    private PlaylistModel playlistModel;
    private LibraryContainer libraryContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enableTextField();
    }

    private void enableTextField() {
        txtPlaylistName.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                fxBtnNewPlaylist.setDisable(newValue.trim().isEmpty() || newValue.trim().length() == 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void setParentController(LibraryContainer controller) {
        this.libraryContainer = controller;
    }

    private void addNewButton(Playlist playlist) throws Exception {
        this.libraryContainer.addNewPlaylist(playlist);
    }

    public void btnNewPlaylist(ActionEvent actionEvent) throws Exception {
        String playlistName = txtPlaylistName.getText();
        Playlist newPlaylist = playlistModel.createPlaylist(playlistName);
        addNewButton(newPlaylist);
    }

    public void setPlaylistModel(PlaylistModel model) {
        playlistModel = model;
    }
}
