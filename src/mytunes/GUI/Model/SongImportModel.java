package mytunes.GUI.Model;

import mytunes.BLL.SongImportManager;
import mytunes.BE.REST.Release;
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

    public String getFeatures() throws JSONException {
        return songImportManager.getFeatures();
    }

    public String getAlias() throws JSONException {
        return songImportManager.getAlias();
    }

    public String getArtistID() throws JSONException {
        return songImportManager.getArtistID();
    }

    public String getSongID() throws JSONException {
        return songImportManager.getSongID();
    }

    public List<Release> getAlbums() throws JSONException {
        return songImportManager.getAlbums();
    }
}
