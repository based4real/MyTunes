package mytunes.GUI.Controller.ny.PopUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.BLL.util.ConfigSystem;
import mytunes.GUI.Model.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ImportSongController implements Initializable {

    @FXML
    private ImageView imgPreview;

    @FXML
    private Button btnImportFX;

    @FXML
    private TextField txtTitel, txtArtist, txtTime, txtFile, txtPicture, txtFeature;

    @FXML
    private ComboBox<String> dropGenre;

    private GenreModel genreModel;
    private SongImportModel songImportModel;
    private MediaPlayerModel mediaPlayerModel;
    private ArtistModel artistModel;
    private SongModel songModel;
    private AlbumModel albumModel;

    private String artistID, artistName, artistAlias;
    private String songID, songTitle, songArtist, songGenre, songfilePath;
    private String songAlbum;

    private File selectedFile;

    public ImportSongController() throws Exception {
        songImportModel = new SongImportModel();
        genreModel = new GenreModel();
        mediaPlayerModel = MediaPlayerModel.getInstance();
        artistModel = ArtistModel.getInstance();
        songModel = SongModel.getInstance();
        albumModel = AlbumModel.getInstance();
    }

    private void setDisabled(boolean disabled) {
        txtArtist.setDisable(disabled);
        txtTitel.setDisable(disabled);
        txtFeature.setDisable(disabled);
        txtFile.setDisable(disabled);
        txtTime.setDisable(disabled);
        dropGenre.setDisable(disabled);
        txtPicture.setDisable(disabled);
        btnImportFX.setDisable(disabled);
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

        fileChooser.setTitle("Vælg musik fil");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            songImportModel.searchSong(selectedFile.getName());

            txtFeature.setText(songImportModel.getFeatures());
            txtArtist.setText(songImportModel.getArtist());
            txtTitel.setText(songImportModel.getTitle());
            txtFile.setText(selectedFile.getAbsolutePath());


            String imageURL = songImportModel.getPictureURL() == null ? ConfigSystem.getSongDefault() : songImportModel.getPictureURL();

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
            txtArtist.setText(songImportModel.getArtist());

            txtFeature.setText(songImportModel.getFeatures());
            String pic = songImportModel.getPictureURL();
            txtPicture.setText(pic);

            setPreviewImg(pic);

            setDisabled(false);
         }
    }

    public void btnImport(ActionEvent actionEvent) throws Exception {
        artistID = songImportModel.getArtistID();
        artistName = songImportModel.getArtist();
        artistAlias = songImportModel.getAlias();

        songID = songImportModel.getSongID();
        songTitle = songImportModel.getTitle();
        songArtist = songImportModel.getArtist();

        songGenre = dropGenre.getSelectionModel().getSelectedItem().toString();
        songfilePath = selectedFile.getPath();


        Artist artist = artistModel.createArtist(new Artist(artistID, artistName, artistAlias));
        Song song = songModel.createNewSong(new Song(songID, songTitle, artist.getPrimaryID(), songGenre ,songfilePath, txtPicture.getText()));
        albumModel.createAlbum(songImportModel.getAlbums(), song, artist);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            dropGenre.getItems().addAll(genreModel.getAllGenreNames());
            dropGenre.getSelectionModel().selectFirst();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
