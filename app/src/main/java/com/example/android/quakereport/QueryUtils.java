package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String LOG_TAG = "QueryUtils";

    private QueryUtils() {
    }

    public static ArrayList<Earthquake> fetchEarthquakes(String urlString){
        Log.i(LOG_TAG,"fetchEarthquakes");
        URL url = createURL(urlString);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = makeHttpRequest(url);

        return extractEarthquakes(jsonResponse);
    }

    private static ArrayList<Earthquake> extractEarthquakes(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject rootObject = new JSONObject(jsonResponse);
            JSONArray featuresArray = rootObject.getJSONArray("features");
            for (int i = 0; i < featuresArray.length(); i++) {
                JSONObject earthquake = featuresArray.getJSONObject(i);
                JSONObject properties = earthquake.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                Earthquake earthquakeObject = new Earthquake(mag, place, time, url);
                earthquakes.add(earthquakeObject);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    private static URL createURL(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = urlConnection.getInputStream();
                jsonResponse = readFromStream(stream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.i(LOG_TAG,e.getMessage());
            return jsonResponse;
        }
        finally {
            urlConnection.disconnect();
        }

        return jsonResponse;


    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        String result = "";
        if (inputStream == null) {
            return result;
        }
        InputStreamReader ir = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(ir);

        StringBuilder builder = new StringBuilder();

        String line = reader.readLine();
        while (line != null) {
            builder.append(line);
            line = reader.readLine();
        }

        return builder.toString();
    }

}