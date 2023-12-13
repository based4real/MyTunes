package mytunes.BE;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Song {
    private int id, artistID, orderID;
    private String title, songID, filePath, artistName, genre, pictureURL;
    private String album;

    private MediaPlayer mediaPlayer;

    public Song(String songID, int id, String title, String artistName, String album, String genre, String filePath, String pictureURL){
        this.songID = songID;
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.album = album;
        this.genre = genre;
        this.filePath = filePath;
        this.mediaPlayer = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
        this.pictureURL = pictureURL;
    }
    public Song(String songID, String title, int artistID, String album, String genre, String filePath, String pictureURL){
        this.songID = songID;
        this.title = title;
        this.artistID = artistID;
        this.album = album;
        this.genre = genre;
        this.filePath = filePath;
        this.mediaPlayer = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
        this.pictureURL = pictureURL;
    }

    public Song(String songID, int id, String title, String artistName, String album, String genre, String filePath, String pictureURL, int orderID){
        this.songID = songID;
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.album = album;
        this.genre = genre;
        this.filePath = filePath;
        this.mediaPlayer = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
        this.pictureURL = pictureURL;
        this.orderID = orderID;
    }

    public String getDuration(){
        double seconds = mediaPlayer.getTotalDuration().toSeconds();

        double secs = seconds % 60;
        double minutes = (seconds / 60) % 60;

        return String.format("%d:%02d", (int)minutes, (int)secs);
    }

    public String getCurrentDuration() {
        double seconds = mediaPlayer.getCurrentTime().toSeconds();

        double secs = seconds % 60;
        double minutes = (seconds / 60) % 60;

        return String.format("%d:%02d", (int)minutes, (int)secs);
    }

    public String getArtistName() {
        return artistName;
    }

    public String getMusicBrainzID() {
        return songID;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getArtistID() {
        return artistID;
    }

    public void setArtistID(int id) {
        this.artistID = id;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public MediaPlayer getMediaPlayer(){
        return this.mediaPlayer;
    }

    public boolean isSongReady() {
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.READY);
    }

    @Override
    public String toString(){
        return title + album;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }
}
