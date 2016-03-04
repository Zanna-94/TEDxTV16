package com.example.group.tedxtv16.item;

import android.graphics.Bitmap;

/**
 * Created by ovidiudanielbarba on 03/03/16.
 */
public abstract class Item {

    // initially equal to 1; when getAllItems(ItemType ) is called,it initializes it right
    public static int maxID = 1;
    private int id;
    private String name;
    private Bitmap photo;
    private String description;
    private String url;
    private ItemType type;

    public Item(int id, String name, Bitmap photo, String description, String url) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }
}
