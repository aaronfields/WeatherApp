package ly.generalassemb.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    private String zip;
    private TextView city;
    private TextView description;
    private TextView temp;
    private TextView humidity;
    private TextView pressure;
    private TextView windSpeed;
    private TextView windDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get zip that was clicked from MainActivity
        Intent intent = getIntent();
        zip = intent.getStringExtra("zip");

        city = (TextView) findViewById(R.id.city);
        description = (TextView) findViewById(R.id.description);
        temp = (TextView) findViewById(R.id.temp);
        humidity = (TextView) findViewById(R.id.humidity);
        pressure = (TextView) findViewById(R.id.pressure);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        windDegree = (TextView) findViewById(R.id.windDegree);

        // Query API with zip passed with Intent
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[] {zip});


    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        // Do in background thread
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new HTTPClient()).getWeatherData(params[0]));

            try {
                weather = JSONParser.getWeather(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return weather;
        }

        // Post results on UI thread
        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            city.setText(weather.location.getCity());
            temp.setText(Double.toString(weather.temperature.getTemp())+"°F");
            description.setText(weather.currentWeather.getDescription());
            humidity.setText("Humidity: "+weather.currentWeather.getHumidity()+"%");
            pressure.setText("Pressure: "+weather.currentWeather.getPressure()+" mb");
            windSpeed.setText("Wind Speed: "+Double.toString(weather.wind.getSpeed())+" mph");
            windDegree.setText("Wind Direction: "+Double.toString(weather.wind.getDegrees())+"°");

        }
    }
}
