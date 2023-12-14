package mytunes.GUI.Controller.ny.Pages;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Controller.ny.Custom.TableContextMenu;
import mytunes.GUI.Controller.ny.Custom.TitleArtistCell;
import mytunes.GUI.Model.MediaPlayerModel;
import mytunes.GUI.Model.PlaylistModel;
import mytunes.GUI.Model.SongModel;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {
    @FXML
    private TableView<Song> tblSongsPlaylist;

    @FXML
    private TableColumn<Playlist, String> colPlaylistNavn;

    @FXML
    private TableColumn<Song, Integer> columnID;

    @FXML
    private TableColumn<Song, String> columnTitle,columnGenre,columnDuration;

    public TableColumn columnAdded;
    public TableColumn columnAlbum;

    private PlaylistModel playlistModel;
    private Playlist playlist;

    private SongModel songModel;
    private MediaPlayerModel mediaPlayerModel;

    public PlaylistController() throws Exception {
        playlistModel = PlaylistModel.getInstance();
        songModel = SongModel.getInstance();
        mediaPlayerModel = MediaPlayerModel.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPlistSongsTableView();
        enableDragAndDrop(tblSongsPlaylist);

        mediaPlayerModel.setPlaylistSongs(tblSongsPlaylist);
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    private void setupPlistSongsTableView() {
        columnTitle.setCellFactory(col -> new TitleArtistCell());
        columnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        columnDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        columnID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
    }

    private Optional<ButtonType> alertPlaylist() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Allerede tilføjet");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Tilføj alligevel");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Tilføj ikke");

        alert.setHeaderText("Denne sang er allerede i playlisten");
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    private void enableDragAndDrop(TableView<Song> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<Song> row = new TableRow<>();

            // Right click on song element
            try {
                TableContextMenu tableContextMenu = new TableContextMenu(tblSongsPlaylist, playlist);
                tableContextMenu.createContextMenu(row);
            } catch (Exception e) {
                System.out.println("Cannot create ContextMenu for Playlists");
                throw new RuntimeException(e);
            }

            row.setOnDragDetected(e -> {
                if (!row.isEmpty()) {
                    int index = row.getIndex();
                    Dragboard dragboard = row.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(String.valueOf(index));
                    dragboard.setContent(content);
                    e.consume();
                }
            });

            row.setOnDragOver(e -> {
                Dragboard dragboard = e.getDragboard();
                if (dragboard.hasString()) {
                    int draggedIndex = Integer.parseInt(dragboard.getString());

                    if (row.isEmpty() || row.getIndex() != draggedIndex) {
                        e.acceptTransferModes(TransferMode.MOVE);
                    }
                }

                e.consume();
            });

            row.setOnDragDropped(e -> {
                Dragboard dragboard = e.getDragboard();
                boolean success = false;

                if (dragboard.hasString()) {
                    int draggedIndex = Integer.parseInt(dragboard.getString());
                    int targetIndex = row.isEmpty() ? tableView.getItems().size() : row.getIndex();

                    // Get the dragged song
                    Song draggedSong = tableView.getItems().get(draggedIndex);

                    // Adjust the target index if needed
                    targetIndex = (targetIndex > draggedIndex) ? targetIndex - 1 : targetIndex;

                    // Get the dropped song before the reordering
                    Song droppedSong = (targetIndex >= 0 && targetIndex < tableView.getItems().size())
                            ? tableView.getItems().get(targetIndex)
                            : null;

                    // Remove the dragged item from its previous position
                    tableView.getItems().remove(draggedIndex);

                    // Add the dragged item to the new position
                    tableView.getItems().add(targetIndex, draggedSong);

                    try {
                        songModel.updateOrderID(playlist, draggedSong, droppedSong);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                    success = true;
                }
                e.setDropCompleted(success);
                e.consume();
            });

            return row;
        });
    }

    public void tablePlaylistSongsClick(Playlist p) throws Exception {
        if (p == null)
            return;

        tblSongsPlaylist.refresh();
        tblSongsPlaylist.setItems(playlistModel.getObservableSongs(p));
        mediaPlayerModel.setPlaylistSongs(tblSongsPlaylist);
    }

}
