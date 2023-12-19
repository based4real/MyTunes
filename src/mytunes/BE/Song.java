package mytunes.BE;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import mytunes.BLL.util.DateFormat;

import java.awt.*;
import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;

public class Song {
    private int id, artistID, orderID;
    private String title, songID, filePath, artistName, pictureURL, albumName;
    private Genre genre;
    private Timestamp addedDate;

    private MediaPlayer mediaPlayer;

    private Album inAlbum;

    public Song(String songID, int id, String title, String artistName, String genre, String filePath, String pictureURL, String albumName, int artistID) {
        this.songID = songID;
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.genre = new Genre(genre);
        this.filePath = filePath;
        createMediaPlayer(new File(filePath));
        this.pictureURL = pictureURL;
        this.albumName = albumName;
        this.artistID = artistID;
    }

    // This is used for album songs
    public Song(String songID, int id, String title, String artistName, String genre, String filePath, String pictureURL, String albumName, int artistID, int orderID) {
        this.songID = songID;
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.genre = new Genre(genre);
        this.filePath = filePath;
        createMediaPlayer(new File(filePath));
        this.pictureURL = pictureURL;
        this.albumName = albumName;
        this.artistID = artistID;
        this.orderID = orderID;
    }

    // This used for playlists.
    public Song(String songID, int id, String title, String artistName, String genre, String filePath, String pictureURL, String albumName, int artistID, Timestamp added) {
        this.songID = songID;
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.genre = new Genre(genre);
        this.filePath = filePath;
        createMediaPlayer(new File(filePath));
        this.pictureURL = pictureURL;
        this.albumName = albumName;
        this.artistID = artistID;
        this.addedDate = added;
    }

    public Song(String songID, int id, String title, String artistName, String genre, String filePath, String pictureURL) {
        this.songID = songID;
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.genre = new Genre(genre);
        this.filePath = filePath;
        createMediaPlayer(new File(filePath));
        this.pictureURL = pictureURL;
    }

    public Song(String songID, String title, int artistID, String genre, String filePath, String pictureURL){
        this.songID = songID;
        this.title = title;
        this.artistID = artistID;
        this.genre = new Genre(genre);
        this.filePath = filePath;
        createMediaPlayer(new File(filePath));
        this.pictureURL = pictureURL;
    }

    private void createMediaPlayer(File f) {
        if(f.exists() && !f.isDirectory())
            this.mediaPlayer = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
        else
            this.title = "[INVALID] " + this.title;
    }

    public double getDoubleTime() {
        if (mediaPlayer == null)
            return 0.0;

        return mediaPlayer.getTotalDuration().toSeconds();
    }

    public String getDuration() {
        if (mediaPlayer == null)
            return "0:00";

        double seconds = mediaPlayer.getTotalDuration().toSeconds();

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
        return title;
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

    public Genre getGenre() {
        return genre;
    }

    public String getGenreName() {
        return genre.getName();
    }

    public String getAlbum() {
        return albumName;
    }

    public String getDate() {
        DateFormat dateFormat = new DateFormat(addedDate);

        return dateFormat.getDate();
    }

    public void setDate(Timestamp date) {
        this.addedDate = date;
    }

    public void setAlbumObject(Album album) {
        this.inAlbum = album;
    }

    public Album getAlbumObject() {
        return this.inAlbum;
    }
}
