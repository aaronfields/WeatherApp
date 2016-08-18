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
        starterCities[2] = "75204";

        mRecyclerView = (RecyclerView) findViewById(R.id.weather_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        for(String city : starterCities) {
            zipList.add(city);
            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{city});
            adapter = new WeatherAdapter(zipList, weatherList, MainActivity.this);
            mRecyclerView.setAdapter(adapter);
        }

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
                        } else {
                            zipList.add(zip);
                            // Query database
                            JSONWeatherTask task = new JSONWeatherTask();
                            task.execute(new String[]{zip});

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

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            weatherList.add(weather);
            adapter = new WeatherAdapter(zipList, weatherList, context);
            mRecyclerView.setAdapter(adapter);

        }
    }

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
