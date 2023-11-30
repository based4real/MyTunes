package mytunes.GUI.Model;

import mytunes.BE.Playlist;
import mytunes.BLL.PlaylistManager;

import java.io.IOException;

public class PlaylistModel {

    private PlaylistManager playlistManager;

    public PlaylistModel() throws IOException {
        playlistManager = new PlaylistManager();
    }

    public void createPlaylist(String name) throws Exception {
        playlistManager.createPlaylist(new Playlist(name));
    }
}
