package mytunes.DAL.REST;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DiscogsConnector {

    private static final String API = "https://api.discogs.com/database";
    private static final String KEY = "KEY";
    private static final String SECRET = "SECRET";

    public DiscogsConnector() {

    }

    private String spacesToJson(String str) {
        return str.replaceAll("\\s","%20");
    }

    private JSONArray getArrayFromURL(HttpResponse<String> bodyParam) throws Exception {
        JSONObject json = new JSONObject(bodyParam.body());
        return json.getJSONArray("results");
    }

    public HttpResponse<String> searchSong(String artist, String title) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(String.format("%s/search?per_page=1&artist=%s&title=%s", API, spacesToJson(artist), spacesToJson(title))))
                .header("Authorization", String.format("Discogs key=%s, secret=%s", KEY, SECRET))
                .build();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        return getResponse;
    }

    public void connectHttp() throws Exception {
        HttpResponse<String> getResponse = searchSong("shakira", "hips don't lie");
        JSONArray jsonArray = getArrayFromURL(getResponse);

        for (int i = 0; i < jsonArray.length(); i++) {
            System.out.println(jsonArray.getJSONObject(i).getString("thumb"));
        }
    }

    public static void main(String[] args) throws Exception {
        DiscogsConnector discogsConnector = new DiscogsConnector();
        discogsConnector.connectHttp();
    }
}
