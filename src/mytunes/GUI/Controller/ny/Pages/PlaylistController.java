package mytunes.GUI.Controller.ny.Pages;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Controller.ny.Custom.TitleArtistCell;
import mytunes.GUI.Model.PlaylistModel;

import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {
    @FXML
    private TableView tblSongsPlaylist;

    @FXML
    private TableColumn<Playlist, String> colPlaylistNavn;

    @FXML
    private TableColumn columnID;

    @FXML
    private TableColumn<Song, String> columnTitle;

    public TableColumn columnGenre;
    public TableColumn columnAdded;
    public TableColumn columnAlbum;
    public TableColumn columnDuration;

    private PlaylistModel playlistModel;
    private ObservableList<Song> playlistSongs = null;

    public PlaylistController() {
        try {
            playlistModel = new PlaylistModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupPlistSongsTableView() {
        columnTitle.setCellFactory(col -> new TitleArtistCell());
    }


    public void tablePlaylistSongsClick(Playlist p) throws Exception {
        if (p == null)
            return;

        playlistSongs = playlistModel.getObservableSongs(p);
        tblSongsPlaylist.setItems(playlistSongs);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPlistSongsTableView();

    }
}
