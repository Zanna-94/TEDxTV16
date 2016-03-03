package com.example.group.tedxtv16;


import android.graphics.Bitmap;

public class NewsItem extends Item {
    public NewsItem(int id, String name, Bitmap photo, String description, String url) {
        super(id, name, photo, description, url);
        setType(ItemType.NEWS);
    }
}
