package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<News>>{

    private static String SAMPLE_JSON="http://content.guardianapis.com/search?q=debates&api-key=test";
    NewsAdapter adapter;
    private static final int BOOK_LOADER_ID = 1;
    private TextView mEmptyStateTextView; //empty textView initialization



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoaderManager loaderManager = getLoaderManager();




        final ListView newsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        if(isInternetConnectionAvailable()){
            loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
        }else{
            adapter.clear();
            mEmptyStateTextView.setText("No internet connection");

        }

        final View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        adapter = new NewsAdapter(this,0, new ArrayList<News>());

        newsListView.setAdapter(adapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News news = adapter.getItem(position);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(news.getUrlLink()));
                startActivity(i);
            }
        });


    }


    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle) {

        return new NewsLoader(this, SAMPLE_JSON);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> newsS) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText("no results found");

        adapter.clear();

        if (newsS != null && !newsS.isEmpty()) {
            adapter.addAll(newsS);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {

        adapter.clear();
    }

    private boolean isInternetConnectionAvailable(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


}
