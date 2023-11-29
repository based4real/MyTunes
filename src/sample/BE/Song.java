package sample.BE;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Song {
    private int id;
    private String title;
    private String artist;
    private String album;
    private String filePath;
    private MediaPlayer mediaPlayer;

    public Song(int id, String title, String artist, String album, String filePath){
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.filePath = filePath;
        this.mediaPlayer = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
    }
    public Song(String title, String artist, String album, String filePath){
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.filePath = filePath;
        this.mediaPlayer = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
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
}
