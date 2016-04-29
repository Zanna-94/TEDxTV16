package com.tedxtorvergatau.tedxtv16.tedxtv16.item;

/**
 * Created by ovidiudanielbarba on 03/03/16.
 */
public enum ItemType {
    SPEAKER(1),NEWS(2),TEAM(3),ABOUT(4);

    private int typeID;

    ItemType(int i) {
        this.typeID = i;
    }

    public int getTypeID() {
        return typeID;
    }
}
