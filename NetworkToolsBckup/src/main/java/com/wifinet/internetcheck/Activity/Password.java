package com.wifinet.internetcheck.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.wifinet.internetcheck.Adapter.AdapterRouter;
import com.wifinet.internetcheck.Adapter.Router;
import com.wifinet.internetcheck.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class Password extends AppCompatActivity {
    String text_Privacy;
    ArrayList<Router> arrayList;
    private AdapterRouter adapterRouter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        arrayList = new ArrayList<>();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        RecyclerView recyclerView = findViewById(R.id.RoutersRecyclerview);
        adapterRouter = new AdapterRouter(new ArrayList<Router>());
        ReadFile(recyclerView,adapterRouter);




   /*  AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
        SearchView searchView = findViewById(R.id.SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                Log.e("", "onQueryTextChange: " + newText );
                return true;
            }
        });
    }

    public void ReadFile (RecyclerView recyclerView  , AdapterRouter adapterRouter )

    {

        InputStream is = getResources().openRawResource(R.raw.router);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            text_Privacy = sb.toString();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                br.close();
                arrayList = extractFeatureFromJson(text_Privacy);
                adapterRouter.setArrayList(arrayList);
                recyclerView.setAdapter(adapterRouter);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    /*    if (interstitialAd != null){
            interstitialAd.destroy();
        }*/
    }

    /**
     * Return a list of {@link Router} objects that has been built up from
     * parsing the given JSON response.
     */
    private static ArrayList<Router> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) { return null; }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Router> Routers = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("Router");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < earthquakeArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.

                // Extract the value for the key called "mag"
                String model = currentEarthquake.getString("model");

                // Extract the value for the key called "place"
                String username = currentEarthquake.getString("username");

                // Extract the value for the key called "time"
                String password = currentEarthquake.getString("password");

                // Extract the value for the key called "url"
                String brand = currentEarthquake.getString("brand");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.


                // Add the new {@link Earthquake} to the list of earthquakes.
                Routers.add(new Router(brand,model,username,password));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return Routers;
    }

    private void filter(String text) {

        ArrayList<Router> filteredList = new ArrayList<>();

        for (Router item : arrayList) {
            if (item.getBrand().toLowerCase().contains(text.toLowerCase()) || item.getModel().toLowerCase().contains(text.toLowerCase()) ) {
                filteredList.add(item);
            }
        }

        adapterRouter.setArrayList(filteredList);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }


}