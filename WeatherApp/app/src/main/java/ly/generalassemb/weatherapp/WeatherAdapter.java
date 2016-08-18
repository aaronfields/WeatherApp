package ly.generalassemb.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronfields on 8/17/16.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.weatherViewHolder>{
    private LayoutInflater inflater;
    private Context context;
    List<Weather> weatherList;
    private List<String> zipList;
    private Image mImage;
    private String zip;

    public WeatherAdapter(List<String> zipList, List<Weather> weatherList, Context context){
        this.zipList = zipList;
        this.weatherList = weatherList;
        this.context = context;

    }


    @Override
    public weatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.weather_card_item, parent, false);
        weatherViewHolder holder = new weatherViewHolder(v, context, weatherList);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(weatherViewHolder holder, int position) {

        Weather weather = weatherList.get(position);
        holder.city.setText(weather.location.getCity());
        holder.zip.setText(zipList.get(position));
        holder.description.setText(weather.currentWeather.getDescription());
        holder.temp.setText(Double.toString(weather.temperature.getTemp()));
        //holder.temp.setText(weather.temperature.getTemp());

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class weatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public ImageView mImage;
        public TextView city;
        public TextView description;
        public TextView temp;
        public TextView zip;
        List<Weather> weatherList = new ArrayList<>();
        Context context;

        public weatherViewHolder(View itemView, Context context, List<Weather> weatherList) {
            super(itemView);
            this.weatherList = weatherList;
            this.context = context;

            itemView.setOnClickListener(this);

            mImage = (ImageView) itemView.findViewById(R.id.image);
            city = (TextView) itemView.findViewById(R.id.city);
            description = (TextView) itemView.findViewById(R.id.description);
            temp = (TextView) itemView.findViewById(R.id.temp);
            zip = (TextView) itemView.findViewById(R.id.zip);

        }

        @Override
        public void onClick(View view) {


            int position = getAdapterPosition();
            Weather weather = weatherList.get(position);
            String zipText = zipList.get(position);
            Intent intent = new Intent(this.context, DetailActivity.class);
            intent.putExtra("zip", zipText);
            this.context.startActivity(intent);

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

//    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
//
//        @Override
//        protected Weather doInBackground(String... params) {
//            Weather weather = new Weather();
//            String data = ((new HTTPClient()).getWeatherData(params[0]));
//
//            try {
//                weather = JSONParser.getWeather(data);
//                weather.iconData = ((new HTTPClient()).getImage(weather.currentWeather.getIcon()));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return weather;
//        }
//
//        @Override
//        protected void onPostExecute(Weather weather) {
//            super.onPostExecute(weather);
//
////            if(weather.iconData != null && weather.iconData.length > 0) {
////                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
////                mImage.setImageBitmap(img);
////            }
//
//        }
//    }
}


