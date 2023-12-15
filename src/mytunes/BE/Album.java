package mytunes.BE;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private String title, type, releaseDate, pictureURL, artistName;
    private int id, artistID;
    private List<Song> albumSongs = new ArrayList<>();

    public Album(int id, String title, String releaseDate, String type, int artistID, String pictureURL, String artistName) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.type = type;
        this.artistID = artistID;
        this.artistName = artistName;
        this.pictureURL = pictureURL;
    }

    public int getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getType() {
        return type;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setAlbumSongs(List<Song> list) {
        albumSongs.addAll(list);
    }

    public void addToAlbumSongs(Song song) {
        albumSongs.add(song);
    }


    public List<Song> getAlbumSongs() {
        return albumSongs;
    }

    public String getArtist() {
        return artistName;
    }
}
