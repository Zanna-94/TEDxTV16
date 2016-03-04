package com.example.group.tedxtv16.item;

import android.graphics.Bitmap;

/**
 * Created by Francesco on 01/03/16.
 */
public class SpeakerItem extends Item {

    public SpeakerItem(int id, String name, Bitmap photo, String description, String url) {
        super(id, name, photo, description, url);
        setType(ItemType.SPEAKER);
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
}
