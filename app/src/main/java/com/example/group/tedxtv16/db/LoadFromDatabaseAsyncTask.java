package com.example.group.tedxtv16.db;

import android.content.Context;
import android.os.AsyncTask;

import com.example.group.tedxtv16.item.Item;
import com.example.group.tedxtv16.item.ItemType;
import com.example.group.tedxtv16.item.SpeakerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ovidiudanielbarba on 03/03/16.
 */
public class LoadFromDatabaseAsyncTask extends AsyncTask<Void,Void,Void> {

    private List<Item> speakerItemList;
    private List<Item> newsItemList;
    private List<Item> teamItemList;
    private Context context;

    public LoadFromDatabaseAsyncTask(Context context) {

        speakerItemList = new ArrayList<>();
        newsItemList = new ArrayList<>();
        teamItemList = new ArrayList<>();
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        ItemDAO itemDAO = new ItemDAO(context);
        speakerItemList = itemDAO.getAllItems(ItemType.SPEAKER);
        newsItemList = itemDAO.getAllItems(ItemType.NEWS);
        teamItemList = itemDAO.getAllItems(ItemType.TEAM);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
