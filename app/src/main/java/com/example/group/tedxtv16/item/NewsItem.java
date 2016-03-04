package com.example.group.tedxtv16.item;


import android.graphics.Bitmap;
import android.os.Parcel;

public class NewsItem extends Item {
    public NewsItem(int id, String name, Bitmap photo, String description, String url) {
        super(id, name, photo, description, url);
        setType(ItemType.NEWS);
    }

    public static void incrementMaxID(){
        maxID++;
    }

    public static void setMaxID(int maxID) {
        SpeakerItem.maxID = maxID;
    }

    public static int getMaxID() {
        return maxID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
