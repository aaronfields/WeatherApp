package ly.generalassemb.weatherapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText input;
    private String zip;
    private String city;
    private WeatherAdapter adapter;
    private List<Weather> weatherList = new ArrayList<>();
    private String[] starterCities;
    private List<String> zipList = new ArrayList<>();
    private Context context;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();

        starterCities = new String[3];
        starterCities[0] = "78704";
        starterCities[1] = "70611";
        starterCities[2] = "10002";

        mRecyclerView = (RecyclerView) findViewById(R.id.weather_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Iterate through cities we want our app pre-populated with
        for(String city : starterCities) {
            zipList.add(city);
            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{city});
            adapter = new WeatherAdapter(zipList, weatherList, MainActivity.this);
            mRecyclerView.setAdapter(adapter);
        }

        // Create fab as means to add more zip codes
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Zip Code");
                input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        zip = input.getText().toString();
                        if(zip.length() != 5){
                            input.setError("Not a valid input");
                            Toast.makeText(MainActivity.this, "Not a valid zip code", Toast.LENGTH_SHORT).show();
                        } else {
                            zipList.add(zip);
                            // Query API with zip the user put in
                            JSONWeatherTask task = new JSONWeatherTask();
                            task.execute(new String[]{zip});

                            // View detail page of new zip
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("zip", zip);
                            startActivity(intent);
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }
        // Use AsyncTask to get JSON data on background thread
    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new HTTPClient()).getWeatherData(params[0]));
            Log.d("TAG", "doInBackground: " + data);

            try {
                weather = JSONParser.getWeather(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }
        // Once data has been received, use onPostExecute to publish results on UI thread
        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            weatherList.add(weather);
            adapter = new WeatherAdapter(zipList, weatherList, context);
            mRecyclerView.setAdapter(adapter);

        }
    }
    // Make sure we're connected to the internet
    public void checkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("SEARCH", "onCreate: You are connected");
            isConnected = true;

        } else {
            Log.d("SEARCH", "onCreate: You are not connected");
            isConnected = false;
        }

    }
}
