package com.tedxtorvergatau.tedxtv16.tedxtv16.db;


import android.os.AsyncTask;
import android.view.View;
import android.widget.FrameLayout;

import com.tedxtorvergatau.tedxtv16.tedxtv16.MainActivity;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.Item;
import com.tedxtorvergatau.tedxtv16.tedxtv16.item.ItemType;

import java.util.List;

public class LoadFromDatabaseAsyncTask extends AsyncTask<Void,Void,Void> {

    private List<Item> speakerItemList;
    private List<Item> newsItemList;
    private List<Item> teamItemList;
    private List<Item> aboutItemList;
    private MainActivity activity;

    private FrameLayout mainFrame;
    private FrameLayout loadFrame;

    public LoadFromDatabaseAsyncTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        newsItemList.clear();
        speakerItemList.clear();
        teamItemList.clear();
        aboutItemList.clear();

        ItemDAO itemDAO = new ItemDAO(activity);
        speakerItemList.addAll(itemDAO.getAllItems(ItemType.SPEAKER));
        newsItemList.addAll(itemDAO.getAllItems(ItemType.NEWS));
        teamItemList.addAll(itemDAO.getAllItems(ItemType.TEAM));
        aboutItemList.addAll(itemDAO.getAllItems(ItemType.ABOUT));
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        activity.refreshFragment();

        mainFrame.setVisibility(View.VISIBLE);
        loadFrame.setVisibility(View.INVISIBLE);
    }

    public void setSpeakerItemList(List<Item> speakerItemList) {
        this.speakerItemList = speakerItemList;
    }

    public void setNewsItemList(List<Item> newsItemList) {
        this.newsItemList = newsItemList;
    }

    public void setTeamItemList(List<Item> teamItemList) {
        this.teamItemList = teamItemList;
    }

    public void setAboutItemList(List<Item> aboutItemList) {
        this.aboutItemList = aboutItemList;
    }

    public void setMainFrame(FrameLayout mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setLoadFrame(FrameLayout loadFrame) {
        this.loadFrame = loadFrame;
    }
}
