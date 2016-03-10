package com.example.group.tedxtv16.db;

import android.content.Context;
import android.os.AsyncTask;

import com.example.group.tedxtv16.item.Item;

import java.util.List;

public class InsertListIntoDBAsyncTask extends AsyncTask<List<Item>,Void,Void>{
    Context context;

    public InsertListIntoDBAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(List<Item>... lists) {
        if(lists == null){
            throw new IllegalArgumentException("Must specify list of items.");
        }
        List<Item> speakerList = lists[0];
        List<Item> teamList = lists[1];
        List<Item> newsList = lists[2];
        List<Item> aboutList = lists[3];

        ItemDAO itemDAO = new ItemDAO(context);
        itemDAO.overWriteItemList(speakerList);
        itemDAO.overWriteItemList(teamList);
        itemDAO.overWriteItemList(newsList);
        itemDAO.overWriteItemList(aboutList);


        return null;
    }
}
