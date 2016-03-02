package com.example.group.tedxtv16;

import android.graphics.Bitmap;

/**
 * Created by Francesco on 01/03/16.
 */
public class SpeakerItem {

    private int id;
    private Bitmap bitmap;
    private String speaker;


    public SpeakerItem(String speaker, Bitmap bitmap) {
        this.speaker = speaker;
        this.bitmap = bitmap;
    }

    public SpeakerItem(int id, Bitmap bitmap, String speaker) {
        this.id = id;
        this.bitmap = bitmap;
        this.speaker = speaker;
    }

    public String getSpeaker() {
        return speaker;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getId() {
        return id;
    }
}
