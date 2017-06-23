package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sudha on 21-Jun-17.
 */

public class NewsAdapter extends ArrayAdapter<News>{


    public NewsAdapter(Context context, int resource, ArrayList<News> newsS) {
        super(context, 0, newsS);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listView = convertView;

        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleTextView = (TextView) listView.findViewById(R.id.news_title);
        titleTextView.setText(currentNews.getTitle());

        TextView sectionTextView = (TextView) listView.findViewById(R.id.news_section);
        sectionTextView.setText(currentNews.getSection());

        TextView dateTextView = (TextView) listView.findViewById(R.id.news_date);
        dateTextView.setText(currentNews.getDate());

        return listView;
    }
}
