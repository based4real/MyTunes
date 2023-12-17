package mytunes.GUI.Controller.Containers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import mytunes.BE.Song;
import mytunes.GUI.Controller.Elements.ControlView;
import mytunes.GUI.Model.MediaPlayerModel;
import mytunes.GUI.Model.SongModel;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MediaPlayerContainer implements Initializable {

    @FXML
    private Button fxBtnPlay, fxBtnPrevious, fxBtnNext;

    @FXML
    private Slider sliderVolume, sliderTime;

    @FXML
    private ImageView imgCover;

    @FXML
    private Label lblTitle, lblArtist;

    @FXML
    private Label lblCurrentDuration, lblMaxDuration;

    private boolean wasDragged;
    private boolean wasClicked;

    private Song lastPlayedSong;

    private MediaPlayerModel mediaPlayerModel;
    private SongModel songModel;

    public MediaPlayerContainer() throws Exception {
        this.mediaPlayerModel = MediaPlayerModel.getInstance();
        this.songModel = SongModel.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderDesign();

        initalizeVolumeControl();
        hoverArtistLabel();
        mediaPlayerModel.setMediaPlayerContainer(this);
    }

    private void setDisable(boolean b) {
        fxBtnPlay.setDisable(b);
        fxBtnNext.setDisable(b);
        fxBtnPrevious.setDisable(b);
        sliderTime.setDisable(b);
        sliderVolume.setDisable(b);
    }

    private void updateImage(Song song) {
        Image newImage = new Image(new File(song.getPictureURL()).toURI().toString());
        imgCover.setFitHeight(55);
        imgCover.setFitWidth(55);

        imgCover.setPreserveRatio(false);

        imgCover.setImage(newImage);
    }

    private void updateArtistTitleLabel(Song song) {
        lblTitle.setText(song.getTitle());
        lblArtist.setText(song.getArtistName());
    }

    private void updateBtnPlayBtn(MediaPlayer mediaPlayer) {
        boolean STATUS_STOPPED = mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED;
        boolean STATUS_READY = mediaPlayer.getStatus() == MediaPlayer.Status.READY;
        boolean STATUS_PAUSED = mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED;

        if (STATUS_READY || STATUS_PAUSED || STATUS_STOPPED) {
            fxBtnPlay.getStyleClass().clear();
            fxBtnPlay.getStyleClass().add("media_pause");
        } else {
            fxBtnPlay.getStyleClass().clear();
            fxBtnPlay.getStyleClass().add("media_play");
        }
    }

    public void playSelectedSong(Song song) {
        MediaPlayer mediaPlayer = song.getMediaPlayer();

        mediaPlayerModel.playSong(mediaPlayer);
        updatePlayerControls(mediaPlayer, song);

        this.lastPlayedSong = song;

        // UI update
        setDisable(false);
        updateArtistTitleLabel(song);
        updateImage(song);
        updateBtnPlayBtn(mediaPlayer);
    }

    public void sliderDesign() {
        sliderTime.valueProperty().addListener((obs, oldValue, newValue) -> {
            double percentage = 100.0 * newValue.doubleValue() / sliderTime.getMax();
            String style = String.format(Locale.US,
                    // in the String format,
                    // %1$.1f%% gives the first format argument ("1$"),
                    // i.e. percentage, formatted to 1 decimal place (".1f").
                    // Note literal % signs must be escaped ("%%")

                    "-track-color: linear-gradient(to right, " +
                            "-fx-accent 0%%, " +
                            "-fx-accent %1$.1f%%, " +
                            "-default-track-color %1$.1f%%, " +
                            "-default-track-color 100%%);",
                    percentage);
            sliderTime.setStyle(style);
        });

    }
    private void initalizeVolumeControl() {
        sliderVolume.setMax(100);
        sliderVolume.setValue(100);
    }

    private void updateVolumeControl(MediaPlayer mediaPlayer) {
        // Når man dragger
        mediaPlayer.setVolume(sliderVolume.getValue() / 100);
      //  lblVolume.setText((int)sliderVolume.getValue() + "%");

        sliderVolume.valueProperty().addListener((observableValue, oldTime, newTime) -> {
            mediaPlayer.setVolume(newTime.doubleValue() / 100);
       //     lblVolume.setText(newTime.intValue() + "%");
        });
    }

    private void updatePlayerControls(MediaPlayer mediaPlayer, Song song) {
        double songLength = mediaPlayer.getTotalDuration().toMillis();
        sliderTime.setMax(songLength);
        lblMaxDuration.setText(song.getDuration());

        updateVolumeControl(mediaPlayer);

        //Når man klikker en enkelt gang
        sliderTime.valueChangingProperty().addListener((observableValue, someBoolean, isChanging) -> {
            wasDragged = isChanging;
            if (!wasDragged) { // User action has just ended
                mediaPlayer.seek(new Duration(sliderTime.getValue()));
                lblCurrentDuration.setText(mediaPlayerModel.getTimeFromDouble(sliderTime.getValue() / 1000));
            }
        });

        // Når man dragger
        sliderTime.valueProperty().addListener((observableValue, oldTime, newTime) -> {
            if (wasDragged && sliderTime.isValueChanging())
                lblCurrentDuration.setText(mediaPlayerModel.getTimeFromDouble(newTime.doubleValue() / 1000));

            // wasClicked er VIGTIG, ellers kommer der "buggy" lyd
            wasClicked = Math.abs(oldTime.doubleValue() - newTime.doubleValue()) / 100 > 10;
            if (!sliderTime.isValueChanging() && !wasDragged && wasClicked)
                mediaPlayer.seek(new Duration(sliderTime.getValue()));
        });


        mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
            if (wasDragged && wasClicked)
                return;

            boolean playNextSong = sliderTime.getMax() / 100 - 5 < newTime.toMillis() / 100;

            if (playNextSong)
                System.out.println("song finished");

            lblCurrentDuration.setText(song.getCurrentDuration());
            sliderTime.setValue(newTime.toMillis());
        });
    }

    private void setHoverStyle(boolean isHovered, Label label) {
        label.setUnderline(isHovered);
        label.setStyle(isHovered ? "-fx-cursor: hand; -fx-fill: rgb(255, 255, 255)" : "-fx-cursor: default; -fx-fill: rgb(150, 150, 150)");
    }

    private void hoverArtistLabel() {
        // If artistLabel has been clicked on
        lblArtist.setOnMouseClicked(event -> {
            // Go to artist page or soemthing ??

            ControlView.switchToArtist();
            try {
                ControlView.getArtistController().updatePage(songModel.getArtistFromSong(lastPlayedSong));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    public void btnPlay(ActionEvent actionEvent) {
        Song song = mediaPlayerModel.getSelectedSong();

        if (song == null)
            return;

        playSelectedSong(song);
    }

    public void btnNext(ActionEvent actionEvent) {
    }

    public void btnPrevious(ActionEvent actionEvent) {
    }
}
