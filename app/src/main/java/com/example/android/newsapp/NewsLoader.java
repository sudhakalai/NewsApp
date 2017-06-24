package com.example.android.newsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by Sudha on 22-Jun-17.
 */

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    String mUrl;

    //Constructor declaration
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //runs in the background
    @Override
    public ArrayList<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        ArrayList<News> newsS = QueryUtils.fetchNews(mUrl);
        return newsS;
    }
}
