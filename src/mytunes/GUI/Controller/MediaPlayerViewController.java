package mytunes.GUI.Controller;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.GUI.Model.MediaPlayerModel;
import mytunes.GUI.Model.PlaylistModel;
import mytunes.GUI.Model.SongModel;

import javax.swing.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

public class MediaPlayerViewController implements Initializable {

    public TableView<Song> tblPlaylistSongs;
    public TableColumn colPlistSongID;
    public TableColumn colPlistSongTitle;

    @FXML
    private TableColumn<Playlist, String> colPlaylistNavn;

    @FXML
    private TableColumn<Playlist, Integer> colPlaylistID;

    @FXML
    private TableView<Playlist> tblPlaylists;

    @FXML
    private TextField txtSongFilter;
    @FXML
    private Slider sliderPlayTime, sliderVolume;

    @FXML
    private Label lblSongDuration, lblSongCurrentTime, lblVolume;

    @FXML
    private Button btnPlay;

    @FXML
    private TableView<Song> tblSongs;

    @FXML
    private TableColumn<Song, String> colTitle, colArtist, colAlbum, colDuration;

    @FXML
    private Button btnNewSongWindow;
    private SongModel songModel;
    private MediaPlayerModel mediaPlayerModel;
    private PlaylistModel playlistModel;
    private ObservableList<Song> playlistSongs = null;

    private boolean wasDragged = false;
    private boolean wasClicked = false;

    public MediaPlayerViewController(){
        try {
            songModel = new SongModel();
            mediaPlayerModel = new MediaPlayerModel();
            playlistModel = new PlaylistModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSongTableView();
        setupSongsData();

        setupPlaylistTableView();
        setupPlistSongsTableView();

        initalizeVolumeControl();
        checkTableClick();
        filterSongs();
        createContextMenu();
    }

    private Optional<ButtonType> alertPlaylist() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Allerede tilføjet");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Tilføj alligevel");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Tilføj ikke");

        alert.setHeaderText("Denne sang ?");
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    private void createContextMenu() {
        tblSongs.setRowFactory(tableView -> {
            final TableRow row = new TableRow();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem removeMenuItem = new MenuItem("Remove");

            final Menu addToPlaylistSubMenu = new Menu("Add to playlist");

            try {
                for (Playlist p : playlistModel.getPlaylists()) {
                    MenuItem playlistMenuItem = new MenuItem(p.getName());
                    addToPlaylistSubMenu.getItems().add(playlistMenuItem);

                    playlistMenuItem.setOnAction(event -> {
                        try {
                            Song song = (Song) row.getItem();

                            if (playlistModel.isSongInPlaylist(song, p)) {
                                Optional<ButtonType> result = alertPlaylist();
                                if (result.get().equals(ButtonType.OK)) {
                                    if (playlistModel.addSongToPlaylist(p, song))
                                        if (tblPlaylists.getSelectionModel().getSelectedItem() == p)
                                            updatePlaylistSongs(p);
                                }
                            } else {
                                if (playlistModel.addSongToPlaylist(p, song))
                                    if (tblPlaylists.getSelectionModel().getSelectedItem() == p)
                                        updatePlaylistSongs(p);
                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            removeMenuItem.setOnAction(event -> {
                System.out.println(row.getItem()); // HERE

                this.tblSongs.getItems().remove(row.getItem());
            });

            contextMenu.getItems().add(removeMenuItem);
            contextMenu.getItems().add(addToPlaylistSubMenu);


            // Set context menu on row, but use a binding to make it only show for non-empty rows:
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });
    }


    public void openNewSongWindow(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewSongWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("New/Edit Song");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e){
            System.out.println("Cant load NewSongWindow");
        }
    }

    private void setupSongTableView() {
        ObservableList<Song> songs = songModel.getObservableSongs();
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tblSongs.setItems(songs);
    }

    private void setupPlaylistTableView() {
        ObservableList<Playlist> playlists = playlistModel.getObservablePlaylists();
        colPlaylistID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPlaylistNavn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblPlaylists.setItems(playlists);
    }

    private void setupPlistSongsTableView() {
        colPlistSongID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPlistSongTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        tablePlaylistSongsClick();
    }

    private void setupSongsData(){
        ObservableList<Song> songs = songModel.getObservableSongs();
        for (Song s : songs) {
            MediaPlayer m = s.getMediaPlayer();
            if (m.getStatus() == MediaPlayer.Status.UNKNOWN) {
                m.statusProperty().addListener((obs, oldStatus, newStatus) -> {
                    if (newStatus == MediaPlayer.Status.READY) {
                        tblSongs.getColumns().setAll(colTitle, colArtist, colAlbum, colDuration);
                    }
                });
            } else {
                System.out.println("NOT READY");
            }
        }
    }

    private void tablePlaylistSongsClick() {
        tblPlaylists.setRowFactory( tv -> {
            TableRow<Playlist> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Playlist plist = (Playlist) row.getItem();
                try {
                    updatePlaylistSongs(plist);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return row;
        });
    }

    private void updatePlaylistSongs(Playlist playlist) throws Exception {
        playlistSongs = playlistModel.getObservableSongs(playlist);
        tblPlaylistSongs.setItems(playlistSongs);
    }


    private void checkTableClick() {
        // Tjek for doubbelt klik i table songs
        tblSongs.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
                playSelectedSong(tblSongs.getSelectionModel().getSelectedItem());
        });
    }

    private void initalizeVolumeControl() {
        sliderVolume.setMax(100);
        sliderVolume.setValue(100);
        lblVolume.setText(sliderVolume.getValue() + "%");
    }

    private void updateVolumeControl(MediaPlayer mediaPlayer) {
        // Når man dragger
        mediaPlayer.setVolume(sliderVolume.getValue() / 100);
        lblVolume.setText((int)sliderVolume.getValue() + "%");

        sliderVolume.valueProperty().addListener((observableValue, oldTime, newTime) -> {
            mediaPlayer.setVolume(newTime.doubleValue() / 100);
            lblVolume.setText(newTime.intValue() + "%");
        });
    }

    private void updatePlayerControls(MediaPlayer mediaPlayer, Song song) {
        double songLength = mediaPlayer.getTotalDuration().toMillis();
        sliderPlayTime.setMax(songLength);
        lblSongDuration.setText(song.getDuration());

        updateVolumeControl(mediaPlayer);

        //Når man klikker en enkelt gang
        sliderPlayTime.valueChangingProperty().addListener((observableValue, someBoolean, isChanging) -> {
            wasDragged = isChanging;
            if (!wasDragged) { // User action has just ended
                mediaPlayer.seek(new Duration(sliderPlayTime.getValue()));
                lblSongCurrentTime.setText(mediaPlayerModel.getTimeFromDouble(sliderPlayTime.getValue() / 1000));
            }
        });

        // Når man dragger
        sliderPlayTime.valueProperty().addListener((observableValue, oldTime, newTime) -> {
            if (wasDragged && sliderPlayTime.isValueChanging())
                lblSongCurrentTime.setText(mediaPlayerModel.getTimeFromDouble(newTime.doubleValue() / 1000));

            // wasClicked er VIGTIG, ellers kommer der "buggy" lyd
            wasClicked = Math.abs(oldTime.doubleValue() - newTime.doubleValue()) / 100 > 10;
            if (!sliderPlayTime.isValueChanging() && !wasDragged && wasClicked)
                mediaPlayer.seek(new Duration(sliderPlayTime.getValue()));
        });


        mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
            if (wasDragged && wasClicked)
                return;

            lblSongCurrentTime.setText(song.getCurrentDuration());
            sliderPlayTime.setValue(newTime.toMillis());
        });
    }

    public void playSelectedSong(Song song) {
        if (song == null)
            return;

        MediaPlayer mediaPlayer = song.getMediaPlayer();

        mediaPlayerModel.playSong(mediaPlayer);
        updatePlayerControls(mediaPlayer, song);
    }

    public void playSong(ActionEvent actionEvent) {
        playSelectedSong(tblSongs.getSelectionModel().getSelectedItem());
    }

    private void playNextOrLast(int idx, boolean next) {
        int selectedIdx = tblSongs.getSelectionModel().getSelectedIndex();
        if (selectedIdx == -1)
            return;

        int songPos = next ? selectedIdx + 1 : selectedIdx - 1;

        MediaPlayer selectedMediaPlayer = tblSongs.getSelectionModel().getSelectedItem().getMediaPlayer();
        if (selectedMediaPlayer.getCurrentTime().toSeconds() > 3 && !next) {
            mediaPlayerModel.restartSong();
            return;
        }

        if (selectedIdx == -1 || songPos < 0 || tblSongs.getItems().size() <= songPos)
            return;

        playSelectedSong(tblSongs.getItems().get(songPos));
        tblSongs.getSelectionModel().select(songPos);

    }

    public void btnLastSong(ActionEvent actionEvent) {
        playNextOrLast(tblSongs.getSelectionModel().getSelectedIndex(), false);
    }

    public void btnNextSong(ActionEvent actionEvent) {
        playNextOrLast(tblSongs.getSelectionModel().getSelectedIndex(), true);
    }

    private void filterSongs(){
        txtSongFilter.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                songModel.filterSong(newValue);
            } catch (Exception e) {
                System.out.println("Error");
            }
        });
    }

    public void deleteSong(ActionEvent actionEvent) {
        Song selectedSong = tblSongs.getSelectionModel().getSelectedItem();
        if (selectedSong != null){
            try {
                songModel.deleteSong(selectedSong);
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    }

    public void openNewPlaylistWindow(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewPlaylistWindow.fxml"));
            Parent root = loader.load();

            NewPlaylistWindowController newPlaylistWindowController = loader.getController();
            newPlaylistWindowController.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("New Playlist");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e){
            System.out.println("Cant load NewPlaylistWindow");
        }
    }
}