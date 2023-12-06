package mytunes.DAL.REST;

import mytunes.DAL.REST.utils.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLHandshakeException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CoverArt {

    String id;
    private static final String URL = "https://coverartarchive.org/release";
    private JSONArray data;

    private String frontImage, frontThumbnail;
    private String backImage, backThumbnail;

    private JSONObject object;

    public CoverArt(String id) throws Exception {
        this.id = id;
        this.data = getCovers(id);
        this.object = JsonUtil.arrayToObject(this.data);
    }

    private JSONArray getCovers(String id) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(String.format("%s/%s", URL, id)))
                .header("Accept", "application/json")  // Set the Accept header for JSON
                .build();

        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        int statusCode = getResponse.statusCode();
        int redirectCount = 0;
        while ((statusCode == 302 || statusCode == 307) && redirectCount < 3) {
            try {
                // Handle redirect manually
                String redirectUrl = getResponse.headers().firstValue("Location").orElse(null);
                if (redirectUrl != null) {
                    HttpRequest redirectRequest = HttpRequest.newBuilder()
                            .uri(new URI(redirectUrl))
                            .build();

                    getResponse = httpClient.send(redirectRequest, HttpResponse.BodyHandlers.ofString());
                    statusCode = getResponse.statusCode();
                    redirectCount++;
                }
            } catch (SSLHandshakeException e) {
                System.out.println(e);
                break;
            }
        }

        // Check if the response is a valid JSON
        if (JsonUtil.isValidJson(getResponse.body())) {
            return JsonUtil.getArrayFromURL(getResponse, "images");
        } else {
            // Handle the case where the response is not a valid JSON
            return new JSONArray(); // or handle as appropriate for your application
        }
    }

    private JSONObject getThumbnails(JSONObject ob) throws JSONException {
        return ob.getJSONObject("thumbnails");
    }

    private boolean hasFront() throws JSONException {
        if (object != null && object.has("front"))
            return object.getBoolean("front");

        return false;
    }

    public String getFrontCover() throws Exception {
        if (hasFront())
            if (object.has("image"))
                frontImage = object.getString("image");

        return frontImage;
    }

    public String getFrontThumbnail() throws JSONException {
        if (hasFront()) {
            JSONObject thumbs = getThumbnails(object);

            if (thumbs.has("250"))
                frontThumbnail = thumbs.getString("250");
        }
        return frontThumbnail;
    }
}
