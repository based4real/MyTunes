package mytunes.DAL.REST.types;

import mytunes.DAL.REST.utils.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MBRelease {

    private JSONArray data;
    JSONObject object;

    public MBRelease(JSONArray data) throws JSONException {
        this.data = data;
        this.object = JsonUtil.arrayToObject(data);
    }

    private JSONArray getArtistArray() throws JSONException {
        return object.getJSONArray("artist-credit");
    }

    private JSONObject getArtistObject() throws JSONException {
        return (JSONObject) getArtistArray().get(0);
    }

    private String getArtist(JSONObject object) throws JSONException {
        return object.getString("name");
    }

    public String getFeatures() throws JSONException {
        JSONArray features = getArtistArray();

        StringBuilder featureStr = new StringBuilder();

        for (int i = 1; i < features.length(); i++) {
            JSONObject test = (JSONObject) getArtistArray().get(i);
            featureStr.append(getArtist(test));
        }
        return featureStr.toString();
    }

    public String getReleaseID() throws JSONException {
        return object.getString("id");
    }

    public String getArtistID() throws JSONException {
        JSONObject artist = getArtistObject().getJSONObject("artist");
        return artist.getString("id");
    }

    public String getArtistAlias() throws JSONException {
        JSONObject artist = getArtistObject().getJSONObject("artist");
        JSONArray alias = artist.getJSONArray("aliases");
        JSONObject toObject = JsonUtil.arrayToObject(alias);
        return toObject.getString("name");

    }

    public String getPictureID() throws JSONException {
        JSONObject object = JsonUtil.getArrayFromObject(data, "releases");
        return object.getString("id");
    }

    public String getTitle() throws JSONException {
        return object.getString("title");
    }

    public List<String> getGenre() throws JSONException {
        JSONObject releaseGroupObject = data.getJSONObject(0);
        List<String> genres = new ArrayList<>();

        JSONArray tagsArray = releaseGroupObject.getJSONArray("tags");

        for (int j = 0; j < tagsArray.length(); j++) {
            JSONObject tagObject = tagsArray.getJSONObject(j);
            String val = tagObject.getString("name");
            genres.add(val);
        }

        return genres;
    }

    public String getFullSongTitle(JSONArray data) throws JSONException {

        return "";
    }

    public String getArtist() throws JSONException {
        return getArtist(getArtistObject());
    }
}
