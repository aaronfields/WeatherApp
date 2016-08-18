package ly.generalassemb.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aaronfields on 8/17/16.
 */
public class JSONParser {

    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();

        // Create JSONObject from data
        JSONObject jsonObject = new JSONObject(data);

        // Extract info
        Location location = new Location();

        JSONObject coordObject = getObject("coord", jsonObject);
        location.setLatitude(getDouble("lat", coordObject));
        location.setLongitude(getDouble("lon", coordObject));

        JSONObject sysObject = getObject("sys", jsonObject);
        location.setCountry(getString("country", sysObject));
        location.setSunrise(getInt("sunrise", sysObject));
        location.setSunset(getInt("sunset", sysObject));
        location.setCity(getString("name", jsonObject));
        weather.location = location;

        // Get weather info (array)
        JSONArray jsonArray = jsonObject.getJSONArray("weather");

        JSONObject JSONWeather = jsonArray.getJSONObject(0);
        weather.currentWeather.setWeatherId(getInt("id", JSONWeather));
        weather.currentWeather.setDescription(getString("description", JSONWeather));
        weather.currentWeather.setCondition(getString("main", JSONWeather));
        weather.currentWeather.setIcon(getString("icon", JSONWeather));

        JSONObject mainObject = getObject("main", jsonObject);
        weather.currentWeather.setHumidity(getString("humidity", mainObject));
        weather.currentWeather.setPressure(getString("pressure", mainObject));
        weather.temperature.setMaxTemp(getDouble("temp_max", mainObject));
        weather.temperature.setMinTemp(getDouble("temp_min", mainObject));
        weather.temperature.setTemp(getDouble("temp", mainObject));

        JSONObject windObject = getObject("wind", jsonObject);
        weather.wind.setSpeed(getDouble("speed", windObject));
        weather.wind.setDegrees(getDouble("deg", windObject));

        JSONObject cloudObject = getObject("clouds", jsonObject);
        weather.clouds.setPercentage(getInt("all", cloudObject));

        return weather;
    }

    private static JSONObject getObject(String tag, JSONObject jsonObject) throws JSONException{
        JSONObject subObject = jsonObject.getJSONObject(tag);
        return subObject;
    }

    private static String getString(String tag, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tag);
    }

    private static double getDouble(String tag, JSONObject jsonObject) throws JSONException {
        return jsonObject.getDouble(tag);
    }

    private static int getInt(String tag, JSONObject jsonObject) throws JSONException{
        return jsonObject.getInt(tag);
    }

}
