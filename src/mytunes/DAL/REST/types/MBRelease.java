package mytunes.DAL.REST.types;

import mytunes.BE.REST.Release;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MBRelease {

    private JSONArray data;
    JSONObject object;
    private List<Release> albumsList = new ArrayList<>();

    public MBRelease(JSONObject data) throws JSONException {
        this.object = data;
    }

    private JSONArray getArtistArray() throws JSONException {
        return object.getJSONArray("artist-credit");
    }

    private JSONArray getReleasesArray() throws JSONException {
        return object.getJSONArray("releases");
    }

    public List<Release> getAlbums() {
        // Create a list to store Release objects
        if (!albumsList.isEmpty())
            return albumsList;

        try {
            for (int i = 0; i < getReleasesArray().length(); i++) {
                JSONObject releaseObject = getReleasesArray().getJSONObject(i);
                String releaseId = releaseObject.getString("id");
                String title = releaseObject.getString("title");
                String status = releaseObject.getString("status");

                String releaseGroupId = releaseObject.getJSONObject("release-group").getString("id");
                String primaryType = releaseObject.getJSONObject("release-group").getString("primary-type");

                JSONArray artistCreditArr = releaseObject.getJSONArray("artist-credit");
                String artistName = artistCreditArr.getJSONObject(0).getString("name");

                JSONArray mediaArray = releaseObject.getJSONArray("media");
                JSONObject firstMediaObject = mediaArray.getJSONObject(0);
                JSONArray trackArray = firstMediaObject.getJSONArray("track");
                int trackNumber = trackArray.getJSONObject(0).getInt("number");


                String releaseDate = releaseObject.has("date") ? releaseObject.getString("date") : "N/A";

                boolean albumType = primaryType.equals("Single");

                Release newRelease = new Release(releaseId, releaseGroupId, title, releaseDate, albumType, trackNumber);

                // Check if the set already contains this release
                if (!artistName.equals("Various Artists") && status.equals("Official") && !albumsList.contains(newRelease)) {
                    albumsList.add(newRelease);
                }
            }
        } catch (Exception e) {

        }

        for (Release release : albumsList) {
            System.out.println("Release ID: " + release.getReleaseId() + " | Date: " + release.getDate() + " | Title: " + release.getTitle() + " | Single: " + release.getIsSingle() + " | Pos: " + release.getSongPos());
        }
        return albumsList;
    }

    private JSONObject getReleaseGroupArray() throws JSONException {
        return getReleasesArray().getJSONObject(0);
    }

    private JSONObject getArtistObject() throws JSONException {
        JSONArray artist = getArtistArray();

        for (int i = 0; i < artist.length(); i++) {
            JSONObject artistCredit = artist.getJSONObject(i);

            // Check if "joinphrase" exists in the current artist credit
            if (!artistCredit.has("joinphrase")) {
                return artistCredit;
            }
        }
        return new JSONObject();
    }

    private JSONObject getFeaturesObject() throws JSONException {
        JSONArray artist = getArtistArray();

        for (int i = 0; i < artist.length(); i++) {
            JSONObject artistCredit = artist.getJSONObject(i);

            // Check if "joinphrase" exists in the current artist credit
            if (artistCredit.has("joinphrase")) {
                return artistCredit;
            }
        }
        return new JSONObject();
    }

    public String getArtist() throws JSONException {
        StringBuilder artistStr = new StringBuilder();
        JSONObject artistCredit = getArtistObject();

        // Check if "joinphrase" exists in the current artist credit
        if (!artistCredit.has("joinphrase")) {
            String name = artistCredit.getJSONObject("artist").optString("name", "");
             artistStr.append(name);
        }

        return artistStr.toString();
    }

    public String getFeatures() throws JSONException {
        JSONObject artistCredit = getFeaturesObject();
        StringBuilder featureStr = new StringBuilder();

        // Check if "joinphrase" exists in the current artist credit
        if (artistCredit.has("joinphrase")) {
            String name = artistCredit.getJSONObject("artist").optString("name", "");
            featureStr.append(name);
        }

        return featureStr.toString();
    }

    public String getReleaseID() throws JSONException {
        return object.getString("id");
    }

    public String getArtistID() throws JSONException {
        JSONArray artist = getArtistArray();
        StringBuilder artistStr = new StringBuilder();

        for (int i = 0; i < artist.length(); i++) {
            JSONObject artistCredit = artist.getJSONObject(i);

            // Check if "joinphrase" exists in the current artist credit
            if (!artistCredit.has("joinphrase")) {
                String name = artistCredit.getJSONObject("artist").optString("id", "");
                artistStr.append(name);
            }
        }

        return artistStr.toString();
    }

    public String getArtistAlias() throws JSONException {
        JSONObject artist = getArtistObject().getJSONObject("artist");

        if (artist.has("aliases")) {
            JSONArray aliases = artist.getJSONArray("aliases");

            for (int i = 0; i < aliases.length(); i++) {
                JSONObject alias = aliases.getJSONObject(i);

                // Check if the alias has the type "Artist name"
                if (alias.has("type") && alias.getString("type").equals("Legal name")) {
                    return alias.getString("name");
                }
            }
        }
        return null;
    }


    public String getPictureID() throws JSONException {
        List<Release> covers = getAlbums();

        return covers.isEmpty() ? "null" : covers.get(0).getReleaseId();
    }

    public String getTitle() throws JSONException {
        return object.getString("title");
    }

    public List<String> getGenre() throws JSONException {
        List<String> genres = new ArrayList<>();

        if (!object.has("tags"))
            return genres;

        JSONArray tagsArray = object.getJSONArray("tags");

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
}
