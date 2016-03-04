package com.example.group.tedxtv16.item;


import android.graphics.Bitmap;

public class TeamItem extends Item {

    public TeamItem(int id, String name, Bitmap photo, String description, String url) {
        super(id, name, photo, description, url);
        setType(ItemType.TEAM);
    }
}
