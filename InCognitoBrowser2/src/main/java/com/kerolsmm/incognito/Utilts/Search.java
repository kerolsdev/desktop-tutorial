package com.kerolsmm.incognito.Utilts;

import android.webkit.URLUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Search {

    static final String searchCompleteUrl = "https://www.google.com/complete/search?client=firefox&q=%s";


    public Search () {

    }

    public static ArrayList<String> getCompletions(String text) {
        int total = 0;
        byte[] data = new byte[16384];
        try {
            URL url = new URL(URLUtil.composeSearchUrl(text, searchCompleteUrl, "%s"));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                while (total <= data.length) {
                    int count = in.read(data, total, data.length - total);
                    if (count == -1) {
                        break;
                    }
                    total += count;
                }
                if (total == data.length) {
                    // overflow
                    return new ArrayList<>();
                }
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            // Swallow exception and return empty list
            return new ArrayList<>();
        }

        // Result looks like:
        // [ "original query", ["completion1", "completion2", ...], ...]

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(new String(data, StandardCharsets.UTF_8));
        } catch (JSONException e) {
            return new ArrayList<>();
        }
        jsonArray = jsonArray.optJSONArray(1);
        if (jsonArray == null) {
            return new ArrayList<>();
        }
        final int MAX_RESULTS = 10;
        ArrayList<String> result = new ArrayList<>(Math.min(jsonArray.length(), MAX_RESULTS));
        for (int i = 0; i < jsonArray.length() && result.size() < MAX_RESULTS; i++) {
            String s = jsonArray.optString(i);
            if (s != null && !s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }


}
