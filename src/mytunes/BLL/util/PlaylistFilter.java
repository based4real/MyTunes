package mytunes.BLL.util;

import mytunes.BE.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFilter {
    public List<Playlist> filter(List<Playlist> searchBase, String query) {
        List<Playlist> searchResult = new ArrayList<>();
        for (Playlist playlist : searchBase) {
            if(comparePlaylistName(query, playlist))
            {
                searchResult.add(playlist);
            }
        }

        return searchResult;
    }

    private boolean comparePlaylistName(String query, Playlist playlist) {
        return playlist.getName().toLowerCase().contains(query.toLowerCase());
    }
}