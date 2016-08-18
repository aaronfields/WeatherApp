package ly.generalassemb.weatherapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private String zip;

    public WeatherAdapter(List<String> zipList, List<Weather> weatherList, Context context){
        this.zipList = zipList;
        this.weatherList = weatherList;
        this.context = context;

    }

    @Override
    public weatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = inflater.from(parent.getContext()).inflate(R.layout.weather_card_item, parent, false);
        weatherViewHolder holder = new weatherViewHolder(v, context, weatherList);

        return holder;
    }

    @Override
    public void onBindViewHolder(weatherViewHolder holder, int position) {

        Weather weather = weatherList.get(position);
        holder.city.setText(weather.location.getCity());
        holder.zip.setText(zipList.get(position));
        holder.description.setText(weather.currentWeather.getDescription());
        holder.temp.setText(Double.toString(weather.temperature.getTemp())+"°F");

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
            itemView.setOnLongClickListener(this);

            mImage = (ImageView) itemView.findViewById(R.id.image);
            city = (TextView) itemView.findViewById(R.id.city);
            description = (TextView) itemView.findViewById(R.id.description);
            temp = (TextView) itemView.findViewById(R.id.temp);
            zip = (TextView) itemView.findViewById(R.id.zip);

        }

        @Override
        public void onClick(View view) {
            Log.d("CONTEXT", "onClick: " + this.context);
            int position = getAdapterPosition();
            Weather weather = weatherList.get(position);
            String zipText = zipList.get(position);
            Intent intent = new Intent(this.context, DetailActivity.class);
            intent.putExtra("zip", zipText);
            this.context.startActivity(intent);

        }

        @Override
        public boolean onLongClick(View view) {
            int positionClicked = getAdapterPosition();
            Weather weather = weatherList.get(positionClicked);
            String zipText = zipList.get(positionClicked);
            showAlertDialog(positionClicked);
            Toast.makeText(this.context, "Long Click!", Toast.LENGTH_SHORT).show();

            return false;
        }

        private void showAlertDialog(Integer mPosition) {
            final int position = mPosition;
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle("Remove");
            builder.setMessage("Are you sure you want to remove this location?");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    weatherList.remove(position);
                    zipList.remove(position);
                    WeatherAdapter.this.notifyDataSetChanged();
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            builder.show();

        }
    }

}


