package com.crash.etranzact.masterpass;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Etranzact on 10/11/2017.
 */

public class SQLHelper extends SQLiteOpenHelper {

    public static final String  DATABASE_NAME = "qrscanner";

    public SQLHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE scanner (ID PRIMARY KEY, firstname TEXT, lastname TEXT)");
        db.execSQL("CREATE TABLE token_table (ID PRIMARY KEY, token TEXT)");
        //db.execSQL("INSERT INTO token (token) (ID PRIMARY KEY, token TEXT)");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL();
        onCreate(db);
    }




}

