package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {


    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(@NonNull Context context, ArrayList<Earthquake> arrayList) {
        super(context,0,arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Earthquake earthquake = getItem(position);

        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitude.setText(formatMagnitude(earthquake.getMagnitude()));
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        TextView locationOffset = (TextView) listItemView.findViewById(R.id.location_offset);
        TextView primaryLocation = (TextView) listItemView.findViewById(R.id.primary_location);

        String location = earthquake.getLocation();

        if(location.contains(LOCATION_SEPARATOR)){
            String[] arr = location.split(LOCATION_SEPARATOR);
            locationOffset.setText(arr[0] + LOCATION_SEPARATOR);
            primaryLocation.setText(arr[1]);
        }
        else {
            locationOffset.setText(R.string.nearof);
            primaryLocation.setText(location);
        }
        //location.setText(earthquake.getLocation());

        TextView dateTextview = (TextView) listItemView.findViewById(R.id.date);
        TextView timeTextview = (TextView) listItemView.findViewById(R.id.time);

        Date date = new Date(earthquake.getTimeStamp());

        dateTextview.setText(formatDate(date));
        timeTextview.setText(formatTime(date));

        return listItemView;
    }

    private String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    private String formatTime (Date date){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        return timeFormat.format(date);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude){

        int mag = (int) Math.floor(magnitude);
        int magnitudeColor;
        switch (mag){
            case 0:
            case 1:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude1);
                break;
            case 2:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude2);
                break;
            case 3:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude3);
                break;
            case 4:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude4);
                break;
            case 5:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude5);
                break;
            case 6:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude1);
                break;
            case 7:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude7);
                break;
            case 8:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude8);
                break;
            case 9:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude9);
                break;
            default:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude10plus);

        }
        return magnitudeColor;

    }
}
