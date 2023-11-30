package mytunes.GUI.Controller;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mytunes.BE.Song;
import mytunes.GUI.Model.SongModel;

;

public class NewSongViewController {
    @FXML
    private Button btnChoose;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private TextField txfTitle;
    @FXML
    private TextField txfArtist;
    @FXML
    private TextField txfAlbum;
    @FXML
    private TextField txfFile;

    private SongModel songModel;

    public NewSongViewController(){
        try {
            songModel = new SongModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void chooseNewSong(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null){
            String stringToSplit = selectedFile.getPath();
            String[] subStrings = stringToSplit.split("-");
            String title = subStrings[1];
            String artist = subStrings[2];
            String album = subStrings[3];
            txfTitle.setText(title);
            txfArtist.setText(artist);
            txfAlbum.setText(album);
            txfFile.setText(selectedFile.getPath());
        } else {
            System.out.println("file is not valid");
        }
    }

    public void cancelNewSong(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void saveNewSong(ActionEvent actionEvent) {
        if (txfTitle.getText().isEmpty() || txfArtist.getText().isEmpty()
                || txfAlbum.getText().isEmpty() || txfFile.getText().isEmpty()) {
            return;
        }
        String title = txfTitle.getText();
        String artist = txfArtist.getText();
        String album = txfAlbum.getText();
        String file = txfFile.getText();
        try {
            songModel.createNewSong(new Song(title,artist,album,file));
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}