package mytunes.GUI.Controller.ny.Pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import mytunes.BE.Album;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Controller.ny.Custom.CustomPlaylistPicture;
import mytunes.GUI.Controller.ny.Custom.TableContextMenu;
import mytunes.GUI.Controller.ny.Custom.TitleArtistCell;
import mytunes.GUI.Model.MediaPlayerModel;
import mytunes.GUI.Model.PlaylistModel;
import mytunes.GUI.Model.SongModel;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {
    public Label lblPlaylistName;
    public Label lblType;
    public ImageView imgCover;
    public Label lblUsername;
    public Label lblTotalSongs;
    public GridPane imagePane;
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

        // No time to finish this... prioritize
        //enableDragAndDrop(tblSongsPlaylist);
    }

    private void updatePicture(Playlist playlist) throws Exception {
        imagePane.getChildren().clear();

        CustomPlaylistPicture customPlaylistPicture = new CustomPlaylistPicture(playlistModel);
        if (customPlaylistPicture.setCustomPicture(imagePane, playlist, 110))
            return;

        Image newImage = new Image(new File(playlist.getPictureURL()).toURI().toString());
        imgCover.setFitWidth(220);
        imgCover.setFitHeight(220);

        imgCover.setImage(newImage);
        imagePane.add(imgCover,0 ,0);
    }

    private void updateLabels(Playlist playlist) {
        lblUsername.setText("Bruger");
        lblPlaylistName.setText(playlist.getName());
        lblType.setText("Playliste");

        String songs = Integer.toString(playlist.getPlaylistSongs().size());

        lblTotalSongs.setText(songs);
    }


    private void updateUI(Playlist playlist) throws Exception {
        updateLabels(playlist);
        updatePicture(playlist);
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    private void setupPlistSongsTableView() {
        columnTitle.setCellFactory(col -> new TitleArtistCell());
        columnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        columnDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
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
        mediaPlayerModel.wasClickedTable(tblSongsPlaylist);

        updateUI(p);
    }

    public void btnPageBack(ActionEvent actionEvent) {
    }

    public void btnNextPage(ActionEvent actionEvent) {
    }

    public void btnPlayPlaylist(ActionEvent actionEvent) {
    }

    public void btnShuffle(ActionEvent actionEvent) {
    }

    public void btnSettings(ActionEvent actionEvent) {
    }
}
