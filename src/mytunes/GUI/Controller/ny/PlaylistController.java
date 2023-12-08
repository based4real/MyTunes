package mytunes.GUI.Controller.ny;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mytunes.BE.Playlist;

public class PlaylistController {

    @FXML
    private Label playlistName;

    @FXML
    private ImageView cover;

    private Playlist playlist;

    public void setPlaylist(Playlist playlist) {
        playlistName.setText(playlist.getName());
        setPicture(playlist);
        this.playlist = playlist;
    }

    public String getplaylistLabel() {
        return playlistName.getText();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    private void setPicture(Playlist playlist) {
        Image image = new Image("https://user-images.githubusercontent.com/24848110/33519396-7e56363c-d79d-11e7-969b-09782f5ccbab.png");
        cover.setImage(image);
    }
}
