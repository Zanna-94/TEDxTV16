package com.example.group.tedxtv16.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;


import com.example.group.tedxtv16.item.AboutItem;
import com.example.group.tedxtv16.item.Item;
import com.example.group.tedxtv16.item.ItemType;
import com.example.group.tedxtv16.item.NewsItem;
import com.example.group.tedxtv16.item.SpeakerItem;
import com.example.group.tedxtv16.item.TeamItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    // it needs the context of the activity to create the SpeakerDataBaseHelper which fetches the db and reads and writes to it
    private Context context;

    // table names on the DB
    private final String SPEAKER_TABLE = "Speakers";
    private final String NEWS_TABLE = "News";
    private final String TEAM_TABLE = "Team";
    private final String ABOUT_TABLE = "About";

    // column names
    private final String ID_COLUMN = "_id";
    private final String NAME_COLUMN = "name";
    private final String PHOTO_COLUMN = "photo";
    private final String DESCRIPTION_COLUMN = "description";
    private final String URL_COLUMN = "url";


    // It needs the app context to run
    public ItemDAO(Context context) {
        this.context = context;
    }

    public void clearTable(ItemType itemType) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this.context);

        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        // DELETE * FROM
        database.execSQL("DELETE  FROM " + getTableName(itemType));

        database.close();
    }

    public void clearAllTables() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this.context);

        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        database.execSQL("DELETE FROM " + SPEAKER_TABLE);
        database.execSQL("DELETE FROM " + NEWS_TABLE);
        database.execSQL("DELETE FROM " + TEAM_TABLE);
        database.execSQL("DELETE FROM " + ABOUT_TABLE);

        database.close();

    }

    /**
     * Insert itemlist into DB. FIRST CLEARS ALL entries and then INSERTS the new ones
     *
     * @param itemList itemList to overwrite in DB (speakers,news)..
     */
    public void overWriteItemList(List<Item> itemList) {

        ItemType itemType;
        if (itemList.size() > 0) {
            // FIRST clears all the entries
            itemType = itemList.get(0).getType();
            clearTable(itemType);

            DataBaseHelper dataBaseHelper = new DataBaseHelper(this.context);
            SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

            Bitmap photo;
            ContentValues contentValues = new ContentValues();
            String tableName = getTableName(itemType);
            for (Item item : itemList) {
                photo = item.getPhoto();
                contentValues.put(NAME_COLUMN, item.getName());
                contentValues.put(DESCRIPTION_COLUMN, item.getDescription());
                contentValues.put(PHOTO_COLUMN, encodeBitmapToBase64(photo, 100));
                contentValues.put(URL_COLUMN, item.getUrl());

                database.insert(tableName, null, contentValues);
            }

            database.close();
        }
    }


    /**
     * Inserts item to DB table depending on the item type (automatically identified)
     *
     * @param item Item to be inserted
     */
    public void insertItem(Item item) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this.context);

        Bitmap speakerPhoto = item.getPhoto();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN, item.getName());
        contentValues.put(DESCRIPTION_COLUMN, item.getDescription());
        contentValues.put(PHOTO_COLUMN, encodeBitmapToBase64(speakerPhoto, 100));
        contentValues.put(URL_COLUMN, item.getUrl());

        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        String tableName = getTableName(item.getType());

        database.insert(tableName, null, contentValues);


        database.close();
    }

    public String getTableName(ItemType itemType) {

        String tableName = null;

        switch (itemType.getTypeID()) {
            case 1:
                tableName = SPEAKER_TABLE;
                break;
            case 2:
                tableName = NEWS_TABLE;
                break;
            case 3:
                tableName = TEAM_TABLE;
                break;
            case 4:
                tableName = ABOUT_TABLE;
                break;
        }


        return tableName;
    }


    /**
     * Returns from the DB (if exists) ALL the itemTypes you specify( News,Speaker,Team) and loads it into a List of Items
     * ex: if you want all the speakers, call getAllItems(ItemType.SPEAKER)
     *
     * @param itemType itemType to load
     * @return List<Item> in the DB
     */
    public List<Item> getAllItems(ItemType itemType) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this.context);

        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        String tableName = getTableName(itemType);

        String[] columns = {ID_COLUMN, NAME_COLUMN, PHOTO_COLUMN, DESCRIPTION_COLUMN, URL_COLUMN};

        Cursor c = database.query(tableName, columns, null, null, null, null, null);

        List<Item> list = new ArrayList<>();
        Item item = null;

        SpeakerItem.setMaxID(c.getCount());


        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String name = c.getString(1);
                String encodedBase64Bitmap = c.getString(2);
                String description = c.getString(3);
                String url = c.getString(4);

                switch (itemType.getTypeID()) {
                    case 1:
                        item = new SpeakerItem(id, name, decodeBitmapFromBase64(encodedBase64Bitmap), description, url);
                        break;
                    case 2:
                        item = new NewsItem(id, name, decodeBitmapFromBase64(encodedBase64Bitmap), description, url);
                        break;
                    case 3:
                        item = new TeamItem(id, name, decodeBitmapFromBase64(encodedBase64Bitmap), description, url);
                        break;
                    case 4:
                        item = new AboutItem(id, name, decodeBitmapFromBase64(encodedBase64Bitmap), description, url);
                        break;
                }

                list.add(item);

            } while (c.moveToNext());
        }

        database.close();

        return list;
    }

    /**
     * Encode a Bitmap into Base64
     * Visit http://freeonlinetools24.com/base64-image to test if it's correctly converted
     *
     * @param bitmap   Bitmap
     * @param compress int, compression quality, from 0 to 100
     * @return String
     */
    public static String encodeBitmapToBase64(Bitmap bitmap, int compress) {
        return Base64.encodeToString(compressBitmap(bitmap, compress), Base64.DEFAULT);
    }


    /**
     * Compress Bitmap in PNG format and choose quality
     *
     * @param bitmap   Bitmap to convert
     * @param compress int, from 0 to 100, compression quality
     * @return byte[] of the compressed bitMap
     */

    private static byte[] compressBitmap(Bitmap bitmap, int compress) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, compress, stream);
        byte[] byteArray = stream.toByteArray();

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    /**
     * Decode a Bitmap from Base64
     *
     * @param encoded String
     * @return Bitmap
     */
    public static Bitmap decodeBitmapFromBase64(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
