package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        //Setting the title
        TextView titleTextView = (TextView) listView.findViewById(R.id.news_title);
        titleTextView.setText(currentNews.getTitle());

        //Setting the section
        TextView sectionTextView = (TextView) listView.findViewById(R.id.news_section);
        sectionTextView.setText(currentNews.getSection());

        //Setting the date

        TextView dateTextView = (TextView) listView.findViewById(R.id.news_date);
        dateTextView.setText(formatDate(currentNews.getDate()));

        return listView;
    }

    //Formats date
    private String formatDate(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        Date dateObject = null;
        try{
            dateObject = simpleDateFormat.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }

        simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        String dateToDisplay = simpleDateFormat.format(dateObject);

        return dateToDisplay;
    }

}
