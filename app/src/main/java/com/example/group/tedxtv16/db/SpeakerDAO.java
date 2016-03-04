package com.example.group.tedxtv16.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.group.tedxtv16.item.ItemType;
import com.example.group.tedxtv16.item.SpeakerItem;
import com.example.group.tedxtv16.item.Item;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ovidiudanielbarba on 01/03/16.
 */

public class SpeakerDAO {

    // it needs the context of the activity to create the SpeakerDataBaseHelper which fetches the db and reads and writes to it
    private Context context;

    // table names on the DB
    private final String SPEAKER_TABLE = "Speakers";
    private final String NEWS_TABLE = "News";
    private final String TEAM_TABLE = "Team";

    // column names
    private final String ID_COLUMN = "_id";
    private final String NAME_COLUMN = "name";
    private final String PHOTO_COLUMN = "photo";
    private final String DESCRIPTION_COLUMN = "description";
    private final String URL_COLUMN = "url";


    public SpeakerDAO(Context context) {
        this.context = context;
    }

    // write a speaker to the Speakers Table
    public void insertItem(Item item){

        SpeakerDataBaseHelper dataBaseHelper = new SpeakerDataBaseHelper(this.context);

        Bitmap speakerPhoto = item.getPhoto();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN, item.getName());
        contentValues.put(DESCRIPTION_COLUMN,item.getDescription());
        contentValues.put(PHOTO_COLUMN, encodeBitmapToBase64(speakerPhoto, 100));
        contentValues.put(URL_COLUMN, item.getUrl());

        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        database.insert(SPEAKER_TABLE, null, contentValues);

        SpeakerItem.incrementMaxID();

        database.close();
    }

    public SpeakerItem findSpeakerByID(int id){
        SpeakerDataBaseHelper dataBaseHelper = new SpeakerDataBaseHelper(this.context);

        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        String[] projections = {ID_COLUMN,NAME_COLUMN,PHOTO_COLUMN};
        String[] whereArgs = {String.valueOf(id)};

        Cursor c = database.query(SPEAKER_TABLE,projections,"_id = ?", whereArgs,null,null,null);

        SpeakerItem speaker = null;

        if(c.moveToFirst()){
            do {

                String name = c.getString(1);
                //String description = c.getString(3);
                String encodedBase64Bitmap = c.getString(2);

                //speaker = new SpeakerItem(id,decodeBitmapFromBase64(encodedBase64Bitmap),name);

            } while (c.moveToNext());
        }

        database.close();

        return speaker;
    }

    public List<SpeakerItem> getAllItems(ItemType itemType){
        SpeakerDataBaseHelper dataBaseHelper = new SpeakerDataBaseHelper(this.context);

        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        String tableName = null;

        switch (itemType.getTypeID()){
            case 1:
                tableName = SPEAKER_TABLE;
                break;
            case 2:
                tableName = NEWS_TABLE;
                break;
            case 3:
                tableName = TEAM_TABLE;
                break;
        }

        String[] columns = {ID_COLUMN,NAME_COLUMN,PHOTO_COLUMN,DESCRIPTION_COLUMN,URL_COLUMN};

        Cursor c = database.query(tableName,columns,null,null,null,null,null);

        List<SpeakerItem> list = new ArrayList<>();
        SpeakerItem speakerItem;

        SpeakerItem.setMaxID(c.getCount());


        if(c.moveToFirst()){
            do {
                int id = c.getInt(0);
                String name = c.getString(1);
                String encodedBase64Bitmap = c.getString(2);
                String description = c.getString(3);
                String url = c.getString(4);

                speakerItem = new SpeakerItem(id,name,decodeBitmapFromBase64(encodedBase64Bitmap),description,url);

                list.add(speakerItem);

            }while (c.moveToNext());
        }

        database.close();

        return list;
    }

    /**
     * Encode a Bitmap into Base64
     * @param bitmap Bitmap
     * @param compress int, compression quality, from 0 to 100
     * @return String
     */
    public static String encodeBitmapToBase64(Bitmap bitmap, int compress) {
        return Base64.encodeToString(compressBitmap(bitmap, compress), Base64.DEFAULT);
    }



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
     * @param encoded String
     * @return Bitmap
     */
    public static Bitmap decodeBitmapFromBase64(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
