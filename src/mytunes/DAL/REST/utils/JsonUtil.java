package mytunes.DAL.REST.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.http.HttpResponse;

public class JsonUtil {

    public static JSONObject arrayToObject(JSONArray data) throws JSONException {
        if (data.length() == 0)
            return null;

        return data.getJSONObject(0);
    }

    public static JSONObject getArrayFromObject(JSONArray data, String category) throws JSONException {
        JSONObject ob = JsonUtil.arrayToObject(data);
        JSONArray releaseArray = ob.getJSONArray(category);
        return (JSONObject) releaseArray.get(0);
    }

    public static String spacesToJson(String str) {
        return str.replaceAll("\\s","%20");
    }

    public static JSONArray getArrayFromURL(HttpResponse<String> bodyParam, String option) throws Exception {
        JSONObject json = new JSONObject(bodyParam.body());
        return json.getJSONArray(option);
    }

    public static boolean isValidJson(String jsonString) {
        try {
            new JSONObject(jsonString);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

}
