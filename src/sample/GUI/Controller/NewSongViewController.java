package sample.GUI.Controller;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

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

    public void chooseNewSong(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null){
            txfTitle.setText(selectedFile.getName());
            txfArtist.setText(selectedFile.getName());
            txfAlbum.setText(selectedFile.getName());
            txfFile.setText(selectedFile.getPath());
        } else {
            System.out.println("file is not valid");
        }
    }

    public void cancelNewSong(ActionEvent actionEvent) {
    }

    public void saveNewSong(ActionEvent actionEvent) {
    }
}
