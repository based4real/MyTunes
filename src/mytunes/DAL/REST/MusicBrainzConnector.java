package mytunes.DAL.REST;

import mytunes.BLL.util.CacheSystem;
import mytunes.BE.REST.Release;
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

    public boolean searchSong(String artist, String title) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        String artistEnc = URLEncoder.encode(artist, "UTF-8");
        String titleEnc = URLEncoder.encode(title, "UTF-8");

        // A bit stupid, but for some reason the uri was annoying me with the %2A (asterisk)
        URI uri = new URI(API + "/recording?query=artist:" + artistEnc + "%20AND%20release:" + titleEnc + "%20AND%20NOT%20title:instrumental%20AND%20status:official&fmt=json&limit=6");

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        JSONObject responseJson = new JSONObject(getResponse.body());

        if (JsonUtil.isValidJson(getResponse.body()) && responseJson.getInt("count") > 0) {
            data = JsonUtil.getArrayFromURL(getResponse, "recordings");
            mbRelease = new MBRelease(pickMostReleases(data));
            return true;
        } else
            return false;
    }

    private JSONObject pickMostReleases(JSONArray data) throws JSONException {
        int maxReleases = -1;
        JSONObject jsonBestResult = null;
        String maxTitle = "";

        for (int i = 0; i < data.length(); i++) {
            JSONObject recording = data.getJSONObject(i);
            String title = recording.getString("title");

            // Count releases for the recording
            int releasesCount = 0;
            if (recording.has("releases")) {
                releasesCount = recording.getJSONArray("releases").length();
                if (releasesCount > maxReleases) {
                    jsonBestResult = recording;
                    maxTitle = title;
                    maxReleases = releasesCount;
                }
            }


            System.out.println("Releases: " + title + " | Releases Count: " + releasesCount);
        }

        System.out.println("Recording with most releases picked: " + maxReleases + " | Title: " + maxTitle);
        return jsonBestResult;
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

    public List<Release> getAlbums() throws JSONException {
        return mbRelease.getAlbums();
    }

    public static void main(String[] args) throws Exception {
        MusicBrainzConnector musicBrainzConnector = new MusicBrainzConnector();
        boolean found = musicBrainzConnector.searchSong("Eminem", "love the way you lie");

        if (!found)
            return;

        musicBrainzConnector.getArtistAlias();
        String sid = musicBrainzConnector.getSongID();

        String artist = musicBrainzConnector.getArtist();
        String features = musicBrainzConnector.getFeatures();
        String title = musicBrainzConnector.getTitle();

        String id = musicBrainzConnector.getPictureID();

        musicBrainzConnector.getAlbums();

        System.out.println(musicBrainzConnector.getArtistID());

        System.out.println(sid + " " + artist + " - " + title + " ft. " + features);
        CoverArt coverArt = new CoverArt(id);
        String thumb = coverArt.getFrontThumbnail();

        CacheSystem cacheSystem = new CacheSystem();

        cacheSystem.storeImage(thumb);
        System.out.println(thumb);
    }

}