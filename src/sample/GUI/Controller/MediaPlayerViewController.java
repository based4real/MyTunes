package sample.GUI.Controller;

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
import javafx.stage.Stage;
import sample.BE.Song;
import sample.GUI.Model.SongModel;

import java.net.URL;
import java.util.ResourceBundle;

public class MediaPlayerViewController implements Initializable {
    @FXML
    private TableView<Song> tblSongs;
    @FXML
    public TableColumn<Song, String> colTitle, colArtist, colAlbum;
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

        tblSongs.setItems(songModel.getObservableSongs());
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

}
