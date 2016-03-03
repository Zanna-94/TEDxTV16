package com.example.group.tedxtv16.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ovidiudanielbarba on 01/03/16.
 */
// class that helps to create a new database TEDxTV16.db if it doesn't exist, loads it if it's already created
// and lets you read and write on that database

public class SpeakerDataBaseHelper extends SQLiteOpenHelper {

    // database version. helps to upgrade to new version
    private static final int DATABASE_VERSION = 1;
    // database name on the device
    private static final String DATABASE_NAME = "TEDxTV16.db";

    // SQL create Speakers Table (if first time)
    private static final String SPEAKERS_TABLE_CREATE_STATEMENT = "CREATE TABLE `Speakers` (\n" +
            "\t`_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            "\t`name`\tTEXT NOT NULL,\n" +
            "\t`photo`\tTEXT NOT NULL,\n" +
            "\t`description`\tTEXT,\n" +
            "\t`url`\tTEXT NOT NULL\n" +
            ")";

    private static final String NEWS_TABLE_CREATE_STATEMENT = "CREATE TABLE `News` (\n" +
            "\t`_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            "\t`name`\tTEXT NOT NULL,\n" +
            "\t`photo`\tTEXT NOT NULL,\n" +
            "\t`description`\tTEXT,\n" +
            "\t`url`\tTEXT NOT NULL\n" +
            ")";

    private static final String TEAM_TABLE_CREATE_STATEMENT = "CREATE TABLE `Team` (\n" +
            "\t`_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            "\t`name`\tTEXT NOT NULL,\n" +
            "\t`photo`\tTEXT NOT NULL,\n" +
            "\t`description`\tTEXT,\n" +
            "\t`url`\tTEXT NOT NULL\n" +
            ")";


    // constructor. it needs the context of the activity to create
    public SpeakerDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // called AUTOMATICALLY  when the class doesn't find the DB on the device and creates the tables
    // don't call
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SPEAKERS_TABLE_CREATE_STATEMENT);
        sqLiteDatabase.execSQL(NEWS_TABLE_CREATE_STATEMENT);
        sqLiteDatabase.execSQL(TEAM_TABLE_CREATE_STATEMENT);
    }

    // needed for future upgrades
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
