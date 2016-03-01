package com.example.group.tedxtv16;

import android.graphics.Bitmap;

/**
 * Created by simone_mancini on 13/02/16.
 */
public class Speaker {

    private String name;
    private Bitmap photo;
    private String desctiption;

    public Speaker (String name, String description){
        this.name = name;
        this.desctiption = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }
}
