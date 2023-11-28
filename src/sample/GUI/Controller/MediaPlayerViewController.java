package sample.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.BE.Song;
import sample.GUI.Model.SongModel;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MediaPlayerViewController implements Initializable {
    @FXML
    private Label lblSongLength;
    @FXML
    private Button btnPlay;
    @FXML
    private TableView<Song> tblSongs;
    @FXML
    public TableColumn<Song, String> colTitle, colArtist, colAlbum;
    @FXML
    private TableColumn<Song, Double> colDuration;
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
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));


        tblSongs.setItems(songModel.getObservableSongs());
        /*
        songs = new ArrayList<File>();
        directory = new File("data");
        files = directory.listFiles();
        if (files != null){
            songs.addAll(Arrays.asList(files));
        }
        media = new Media(songs.get(1).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        */
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

    public void playSong(ActionEvent actionEvent) {
        tblSongs.getSelectionModel().getSelectedItem().getMediaPlayer().play();
    }
}
