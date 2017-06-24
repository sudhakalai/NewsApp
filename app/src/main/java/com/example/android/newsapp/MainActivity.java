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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<News>>{

    //Declaring Global variables

    private static String SAMPLE_JSON="";
    private static String BASE_URL = "http://content.guardianapis.com/search?";

    NewsAdapter adapter; //Adapter declaration
    private static final int BOOK_LOADER_ID = 1;
    private TextView mEmptyStateTextView; //empty textView initialization



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoaderManager loaderManager = getLoaderManager(); // initializing loadManager

        final ListView newsListView = (ListView) findViewById(R.id.list); //initializing listView

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);



        final View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        adapter = new NewsAdapter(this,0, new ArrayList<News>());

        newsListView.setAdapter(adapter); //Attaching adapter to listView

        Button searchButton = (Button) findViewById(R.id.searchButton);

        //Setting on click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.clear();
                loadingIndicator.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setText("");

                EditText searchText = (EditText) findViewById(R.id.searchText);
                Uri baseUri = Uri.parse(BASE_URL);
                Uri.Builder uriBuilder = baseUri.buildUpon();

                uriBuilder.appendQueryParameter("q", searchText.getText().toString());
                uriBuilder.appendQueryParameter("api-key","test");

                SAMPLE_JSON = uriBuilder.toString();

                if(isInternetConnectionAvailable()){
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                }else{
                    adapter.clear();
                    loadingIndicator.setVisibility(View.GONE);
                    mEmptyStateTextView.setText("No internet connection");
                }
                SAMPLE_JSON = "";
                BASE_URL = "http://content.guardianapis.com/search?";
                searchText.setText("");

            }
        });

        //Setting onItemClickListener for the listView so that each list item  opens a url when clicked
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

        // Set empty state text to display "No results found."
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
