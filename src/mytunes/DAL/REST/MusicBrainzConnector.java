package mytunes.DAL.REST;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MusicBrainzConnector {

    private static final String API = "http://musicbrainz.org/ws/2";

    public MusicBrainzConnector() {

    }

    private String spacesToJson(String str) {
        return str.replaceAll("\\s","%20");
    }

    private JSONArray getArrayFromURL(HttpResponse<String> bodyParam, String option) throws Exception {
        JSONObject json = new JSONObject(bodyParam.body());
        return json.getJSONArray(option);
    }

    public HttpResponse<String> searchSong(String song) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        //http://musicbrainz.org/ws/2/release-group?query=hips dont lie&fmt=json&limit=1
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(String.format("%s/release-group?query=%s&limit=1&fmt=json", API, spacesToJson(song))))
                .build();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        return getResponse;
    }

    public void connectHttp() throws Exception {
        HttpResponse<String> getResponse = searchSong("liqwyd feel so good");
        JSONArray jsonArray = getArrayFromURL(getResponse, "release-groups");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String id = object.getString("id");
            String title = object.getString("title");

            JSONArray artist_arr = object.getJSONArray("artist-credit");
            JSONObject artist_object = (JSONObject) artist_arr.get(0);
            String artist = artist_object.getString("name");

            JSONArray releases_arr = object.getJSONArray("releases");
            JSONObject releases_object = (JSONObject) releases_arr.get(0);
            String releases = releases_object.getString("id");

            System.out.println(id);
            System.out.println("release id: " + releases);
            System.out.println(title);
            System.out.println(artist);
        }
    }

    public static void main(String[] args) throws Exception {
            MusicBrainzConnector musicBrainzConnector = new MusicBrainzConnector();
            musicBrainzConnector.connectHttp();
    }
}