package mytunes.GUI.Controller.ny.PopUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import mytunes.GUI.Model.MediaPlayerModel;
import mytunes.GUI.Model.SongImportModel;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class ImportSongController {

    public ImageView imgPreview;
    @FXML
    private TextField txtTitel, txtArtist, txtTime, txtFile, txtPicture;

    @FXML
    private ComboBox dropCategory;

    private SongImportModel songImportModel;
    private MediaPlayerModel mediaPlayerModel;

    public ImportSongController() throws IOException {
        songImportModel = new SongImportModel();
        mediaPlayerModel = new MediaPlayerModel();
    }


    private void setDisabled(boolean disabled) {
        txtArtist.setDisable(disabled);
        txtTitel.setDisable(disabled);
        txtFile.setDisable(disabled);
        txtTime.setDisable(disabled);
        dropCategory.setDisable(disabled);
        txtPicture.setDisable(disabled);
    }

    private void setPreviewImg(String url) {
        if (url == null)
            return;

        Image image = new Image(url);
        imgPreview.setImage(image);
    }

    public void btnChoose(ActionEvent actionEvent) throws Exception {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files (*.mp3, *.wav)", "*.mp3", "*.wav");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Select Music File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            songImportModel.searchSong(selectedFile.getName());

            txtArtist.setText(songImportModel.getArtist());
            txtTitel.setText(songImportModel.getTitle());
            txtFile.setText(selectedFile.getAbsolutePath());

            dropCategory.getItems().addAll(songImportModel.getGenre());
            dropCategory.getSelectionModel().selectFirst();

            String imageURL = songImportModel.getPictureURL();

            setPreviewImg(imageURL);
            txtPicture.setText(imageURL);

            Media media = new Media(selectedFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnReady(() -> {
                Duration duration = media.getDuration();
                int millis = (int) duration.toSeconds();

                txtTime.setText(mediaPlayerModel.getTimeFromDouble(millis));
            });

            setDisabled(false);

        } else {
            System.out.println("file is not valid");
        }

    }

    public void btnChoosePicture(ActionEvent actionEvent) {
    }

    public void btnSearch(ActionEvent actionEvent) throws Exception {
        songImportModel.searchSongFromText(txtArtist.getText(), txtTitel.getText());

        if (songImportModel.holdsData()) {
            txtTitel.setText(songImportModel.getTitle());
            String pic = songImportModel.getPictureURL();
            dropCategory.getItems().addAll(songImportModel.getGenre());
            txtPicture.setText(pic);
            txtArtist.setText(songImportModel.getArtist());
            setPreviewImg(pic);

            setDisabled(false);
        }
    }

    public void btnImport(ActionEvent actionEvent) {
    }
}
