package mytunes.BLL.util;

import mytunes.BE.Song;

import java.util.ArrayList;
import java.util.List;

public class SongSearcher {

    public List<Song> searchSong(List<Song> allSongs, String searchWord) {
        List<Song> searchResult = new ArrayList<>();
        for (Song song : allSongs) {
            if(compareToSongTitle(searchWord, song))
            {
                searchResult.add(song);
            }
        }

        return searchResult;
    }

    private boolean compareToSongTitle(String searchWord, Song song) {
        return song.getTitle().toLowerCase().contains(searchWord.toLowerCase());
    }
}
