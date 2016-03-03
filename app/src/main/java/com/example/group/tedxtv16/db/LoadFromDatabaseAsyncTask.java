package com.example.group.tedxtv16.db;

import android.os.AsyncTask;

import com.example.group.tedxtv16.NewsItem;
import com.example.group.tedxtv16.SpeakerItem;
import com.example.group.tedxtv16.TeamItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ovidiudanielbarba on 03/03/16.
 */
public class LoadFromDatabaseAsyncTask extends AsyncTask<Void,Void,Void> {

    private List<SpeakerItem> speakerItemList;
    private List<NewsItem> newsItemList;
    private List<TeamItem> teamItemList;

    public LoadFromDatabaseAsyncTask() {


        speakerItemList = new ArrayList<>();
        newsItemList = new ArrayList<>();
        teamItemList = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
