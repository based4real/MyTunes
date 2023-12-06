package mytunes.DAL.REST;

import mytunes.BLL.util.CacheSystem;
import mytunes.DAL.REST.types.MBRelease;
import mytunes.DAL.REST.utils.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class MusicBrainzConnector {

    private static final String API = "http://musicbrainz.org/ws/2";

    private JSONArray data;

    private MBRelease mbRelease;

    public MusicBrainzConnector() {

    }

    public JSONArray searchSong(String artist, String title) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        String artistEnc = URLEncoder.encode(artist, "UTF-8");
        String titleEnc = URLEncoder.encode(title, "UTF-8");

        // A bit stupid, but for some reason the uri was annoying me with the %2A (asterisk)
        URI uri = new URI("https://musicbrainz.org/ws/2/release-group/?query=recording:" + titleEnc + "%20AND%20artist:" + artistEnc + "%2A&fmt=json&limit=1&inc=");

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        if (JsonUtil.isValidJson(getResponse.body())) {
            data = JsonUtil.getArrayFromURL(getResponse, "release-groups");
            mbRelease = new MBRelease(data);
            return data;
        } else
            return new JSONArray();
    }

    public boolean searchSongFromText(String artist, String title) throws Exception {
        JSONArray results = searchSong(artist, title);
        return (results.length() != 0);
    }

    public boolean holdsData() throws Exception {
        return (data.length() != 0);
    }


    public String getArtist() throws Exception {
        return mbRelease.getArtist();
    }

    public String getTitle() throws Exception {
        return mbRelease.getTitle();
    }

    public String getPictureID() throws Exception {
        return mbRelease.getPictureID();
    }

    public String getFeatures() throws JSONException {
        return mbRelease.getFeatures();
    }

    public List<String> getGenre() throws JSONException {
        return mbRelease.getGenre();
    }

    public String getArtistID() throws JSONException {
        return mbRelease.getArtistID();
    }

    public String getArtistAlias() throws JSONException {
        return mbRelease.getArtistAlias();
    }


    public String getSongID() throws JSONException {
        return mbRelease.getReleaseID();
    }

    public static void main(String[] args) throws Exception {
        MusicBrainzConnector musicBrainzConnector = new MusicBrainzConnector();
        musicBrainzConnector.searchSong("eminem", "the real slim shady");

        musicBrainzConnector.getArtistAlias();
        String id = musicBrainzConnector.getPictureID();
        System.out.println(id);
        CoverArt coverArt = new CoverArt(id);
        String thumb = coverArt.getFrontThumbnail();

        CacheSystem cacheSystem = new CacheSystem();

        cacheSystem.storeImage(thumb);
        System.out.println(thumb);

    }

}