package sample.GUI.Controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import sample.BE.Song;
import sample.GUI.Model.SongModel;

import java.net.URL;
import java.util.ResourceBundle;

public class MediaPlayerViewController implements Initializable {
    @FXML
    private Button btnPlay;
    @FXML
    private TableView<Song> tblSongs;
    @FXML
    public TableColumn<Song, String> colTitle, colArtist, colAlbum, colDuration;
    @FXML
    private Button btnNewSongWindow;
    private SongModel songModel;
    public MediaPlayerViewController(){
        try {
            songModel = new SongModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSongTableView();
        setupSongsData();
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

    public void setupSongTableView(){
        ObservableList<Song> songs = songModel.getObservableSongs();
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tblSongs.setItems(songs);
    }

    public void setupSongsData(){
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

    public void playSong(ActionEvent actionEvent) {
        if (tblSongs.getSelectionModel().getSelectedItem().getMediaPlayer().getStatus() != MediaPlayer.Status.PLAYING){
            tblSongs.getSelectionModel().getSelectedItem().getMediaPlayer().play();
        } else {
            tblSongs.getSelectionModel().getSelectedItem().getMediaPlayer().pause();
        }
    }
}
