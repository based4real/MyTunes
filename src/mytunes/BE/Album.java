package mytunes.BE;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private String releaseID, releaseGroupID, title, type, releaseDate, pictureURL;
    private int id, artistID;
    private List<Song> albumSongs = new ArrayList<>();

    public Album(int id, String title, String releaseDate, String type, int artistID, String pictureURL) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.type = type;
        this.artistID = artistID;
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

    public List<Song> getAlbumSongs() {
        return albumSongs;
    }
}
