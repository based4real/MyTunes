package mytunes.GUI.Model;

import mytunes.BLL.SongImportManager;
import org.json.JSONException;

import java.util.List;

public class SongImportModel {

    private SongImportManager songImportManager;

    public SongImportModel() {
        songImportManager = new SongImportManager();
    }

    public void searchSong(String name) throws Exception {
        songImportManager.searchSong(name);
    }

    public boolean searchSongFromText(String artist, String title) throws Exception {
        return songImportManager.searchSongFromText(artist, title);
    }

    public boolean holdsData() throws Exception {
        return songImportManager.holdsData();
    }

    public String getArtist() throws Exception {
        return songImportManager.getArtist();
    }

    public String getTitle() throws Exception {
        return songImportManager.getTitle();
    }

    public List<String> getGenre() throws JSONException {
        return songImportManager.getGenre();
    }

        public String getPictureURL() throws Exception {
        return songImportManager.getPictureURL();
    }
}
