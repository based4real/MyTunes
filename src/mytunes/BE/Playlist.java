package mytunes.BE;

import mytunes.BLL.PlaylistManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private int id, orderID;
    private String name;

    private PlaylistManager playlistManager;
    private List<Song> playlistSongs = new ArrayList<>();

    public Playlist(int id, String name, int orderID) {
        this.id = id;
        this.name = name;
        this.orderID = orderID;
    }

    public Playlist(String name) throws Exception {
        this.name = name;
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

    public int getOrderID() {
        return orderID;
    }

    public void addToPlaylist(Song s)  {
        playlistSongs.add(s);
    }

    public void setPlaylistSongList(List<Song> list)  {
        playlistSongs.addAll(list);
    }

    public List<Song> getPlaylistSongs() {
        return playlistSongs;
    }

}
