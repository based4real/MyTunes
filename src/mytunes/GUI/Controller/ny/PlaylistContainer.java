package mytunes.GUI.Controller.ny;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Model.MediaPlayerModel;
import mytunes.GUI.Model.PlaylistModel;
import mytunes.GUI.Model.SongModel;

import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistContainer implements Initializable {
    @FXML
    private TableView tblSongsPlaylist;

    @FXML
    private TableColumn<Playlist, String> colPlaylistNavn;

    @FXML
    private TableColumn columnID;

    @FXML
    private TableColumn columnTitle;
    public TableColumn columnArtist;
    public TableColumn columnGenre;
    public TableColumn columnAdded;
    public TableColumn columnAlbum;
    public TableColumn columnDuration;

    private PlaylistModel playlistModel;
    private ObservableList<Song> playlistSongs = null;


    public PlaylistContainer() {
        try {
            playlistModel = new PlaylistModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupPlistSongsTableView() {
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));

        tblSongsPlaylist.setColumnResizePolicy((param) -> true );

    }

    public void tablePlaylistSongsClick(Playlist p) throws Exception {
        if (p == null)
            return;

        playlistSongs = playlistModel.getObservableSongs(p);
        tblSongsPlaylist.setItems(playlistSongs);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("CALLED");
        setupPlistSongsTableView();
    }
}
