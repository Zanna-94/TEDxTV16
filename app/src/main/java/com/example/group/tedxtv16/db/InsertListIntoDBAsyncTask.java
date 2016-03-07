package com.example.group.tedxtv16.db;

import android.content.Context;
import android.os.AsyncTask;

import com.example.group.tedxtv16.item.Item;

import java.util.List;

/**
 * Created by ovidiudanielbarba on 07/03/16.
 */
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

        ItemDAO itemDAO = new ItemDAO(context);
        itemDAO.overWriteItemList(speakerList);

        return null;
    }
}
