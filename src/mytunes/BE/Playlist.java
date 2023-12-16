package mytunes.BE;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import mytunes.BLL.PlaylistManager;
import mytunes.BLL.util.CacheSystem;
import mytunes.BLL.util.ConfigSystem;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Playlist {

    private int id, orderID;

    private String name, pictureURL;

    private PlaylistManager playlistManager;
    private List<Song> playlistSongs = new ArrayList<>();
    private GridPane playlistImage;

    public Playlist(int id, String name, int orderID, String pictureURL, List<Song> songs) {
        this.id = id;
        this.name = name;
        this.orderID = orderID;
        this.pictureURL = pictureURL;
        this.playlistSongs = songs;
    }

    public Playlist(int id, String name, int orderID, String pictureURL) {
        this.id = id;
        this.name = name;
        this.orderID = orderID;
        this.pictureURL = pictureURL;
    }

    public Playlist(String name) throws Exception {
        this.name = name;

        CacheSystem cacheSystem = new CacheSystem();
        this.pictureURL = cacheSystem.storeImage(ConfigSystem.getPlaylistDefault());
        setOrderID();
    }

    private void setManager() throws IOException {
        if (playlistManager == null)
            playlistManager = new PlaylistManager();
    }

    private void setOrderID() throws Exception {
        setManager();

        orderID = playlistManager.getNextOrderID();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getOrderID() {
        return orderID;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setOrderID(int newOrder) {
        orderID = newOrder;
    }

    public boolean addToPlaylist(Song s)  {
        return playlistSongs.add(s);
    }

    public void setPlaylistSongList(List<Song> list)  {
        playlistSongs.addAll(list);
    }

    public void removeSongFromPlaylist(Song song)  {
        playlistSongs.remove(song);
    }

    public List<Song> getPlaylistSongs() {
        return playlistSongs;
    }

}
