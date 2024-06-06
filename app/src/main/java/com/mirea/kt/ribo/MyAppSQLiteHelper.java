package com.mirea.kt.ribo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyAppSQLiteHelper extends SQLiteOpenHelper {
    public MyAppSQLiteHelper(Context c, String name,
                             SQLiteDatabase.CursorFactory f, int version) {
        super(c, name, f, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE TABLE_WORDS (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "WordName TEXT, " +
                "WordMeaning TEXT, " +
                "isFavorite INTEGER NOT NULL DEFAULT 0)";
        db.execSQL(CREATE_TABLE);

//        db.execSQL("create table " + "TABLE_WORDS" + " ("
//                + "_id integer primary key autoincrement,"
//                + "WordName text",
//                + "WordMeaning text");");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

}