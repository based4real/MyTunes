package mytunes.GUI.Controller.Custom;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Model.PlaylistModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomPlaylistPicture {

    private PlaylistModel playlistModel;
    public CustomPlaylistPicture(PlaylistModel playlistModel) throws Exception {
        this.playlistModel = playlistModel;
    }

    public boolean setCustomPicture(GridPane imagePane, Playlist playlist, int size) throws Exception {
        List<Song> playlistSongs = playlistModel.getSongs(playlist);
        boolean customPic = playlistSongs.size() > 3;

        if (customPic) {
            List<String> pictures = new ArrayList<>();

            pictures.add(playlistSongs.get(0).getPictureURL());
            pictures.add(playlistSongs.get(1).getPictureURL());
            pictures.add(playlistSongs.get(2).getPictureURL());
            pictures.add(playlistSongs.get(3).getPictureURL());

            int columns = 2;
            int rowIndex = 0;
            int colIndex = 0;

            for (String picture : pictures) {
                if (picture == null)
                    break;

                Image image = new Image(new File(picture).toURI().toString());

                // Create ImageView
                ImageView imageView = new ImageView(image);

                // Set the size of ImageView (optional)
                imageView.setFitWidth(size);
                imageView.setFitHeight(size);
                imageView.setPreserveRatio(false);

                // Add ImageView to the GridPane
                imagePane.add(imageView, colIndex, rowIndex);

                // Update column and row indices
                colIndex++;
                if (colIndex >= columns) {
                    colIndex = 0;
                    rowIndex++;
                }
            }
        }
        return customPic;
    }
}
