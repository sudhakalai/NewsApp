package com.example.android.newsapp;

import android.text.TextUtils;
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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Sudha on 21-Jun-17.
 */

public class QueryUtils {

    private static String LOG_TAG = "QueryUtils";

    //Constructor Declaration
    public QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.v(LOG_TAG, "makeHttpRequest: Response code shows error" + responseCode);
            }

        } catch (IOException e) {

            Log.e(LOG_TAG, "makeHttpRequest: IOException occurred ", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Parses the json response
     */
    private static ArrayList<News> extractNews(String newsJSON) {

        ArrayList<News> newsS = new ArrayList<>();

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        try {

            JSONObject jsonObject = new JSONObject(newsJSON);
            JSONObject responseObject = jsonObject.getJSONObject("response");
            JSONArray resultsArray = responseObject.getJSONArray("results");

            for(int i=0; i < resultsArray.length(); i++){

                JSONObject newsObject = resultsArray.getJSONObject(i);
                String newsTitle = newsObject.getString("webTitle");
                String newsSection = newsObject.getString("sectionName");
                String newsDate = newsObject.getString("webPublicationDate");
                String newsLink = newsObject.getString("webUrl");

                News news = new News(newsTitle, newsSection, newsDate, newsLink);
                newsS.add(news);
            }

        } catch (JSONException e) {
            Log.e(TAG, "extractNews: JSON Exception occured", e);
        }

        return newsS;
    }

//Extracting the news list

    public static ArrayList<News> fetchNews (String requestUrl){
        URL url = createUrl(requestUrl);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractNews(jsonResponse);

    }

}
