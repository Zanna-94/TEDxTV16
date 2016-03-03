package com.example.group.tedxtv16.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.group.tedxtv16.SpeakerItem;

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

    private final String SPEAKER_TABLE = "Speakers";
    private final String ID_COLUMN = "_id";
    private final String NAME_COLUMN = "name";
    private final String PHOTO_COLUMN = "photo";
    //private final String DESCRIPTION_COLUMN = "description";

    public SpeakerDAO(Context context) {
        this.context = context;
    }

    // write a speaker to the Speakers Table
    public void insertSpeaker(SpeakerItem speaker){

        SpeakerDataBaseHelper dataBaseHelper = new SpeakerDataBaseHelper(this.context);

        Bitmap speakerPhoto = speaker.getBitmap();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN,speaker.getSpeaker());
        //contentValues.put(DESCRIPTION_COLUMN,speaker.getDesctiption());
        contentValues.put(PHOTO_COLUMN, encodeBitmapToBase64(speakerPhoto,100));

        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        database.insert(SPEAKER_TABLE, PHOTO_COLUMN, contentValues);

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

                speaker = new SpeakerItem(id,decodeBitmapFromBase64(encodedBase64Bitmap),name);

            } while (c.moveToNext());
        }

        database.close();

        return speaker;
    }

    public List<SpeakerItem> getAllSpeakers(){
        SpeakerDataBaseHelper dataBaseHelper = new SpeakerDataBaseHelper(this.context);

        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        String[] columns = {ID_COLUMN,NAME_COLUMN,PHOTO_COLUMN};

        Cursor c = database.query(SPEAKER_TABLE,columns,null,null,null,null,null);

        List<SpeakerItem> list = new ArrayList<>();
        SpeakerItem speakerItem;


        if(c.moveToFirst()){
            do {
                int id = c.getInt(0);
                String name = c.getString(1);

                String encodedBase64Bitmap = c.getString(2);

                speakerItem = new SpeakerItem(id,decodeBitmapFromBase64(encodedBase64Bitmap),name);

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
