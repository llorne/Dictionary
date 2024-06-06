package com.mirea.kt.ribo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBManager {
    private MyAppSQLiteHelper sqLiteHelper;

    public DBManager(MyAppSQLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    public boolean saveWordToDatabase(Word word, WordMeaning wordMeaning) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("WordName", word.getWordName());
        cv.put("WordMeaning", wordMeaning.getWordMeaning());
        cv.put("isFavorite", word.isFavorite() ? 1 : 0 );

        long rowId = db.insert("TABLE_WORDS", null, cv);

        cv.clear();
        db.close();

        return rowId != -1;
    }

    public ArrayList<Word> loadAllWordFromDatabase() {
        ArrayList<Word> words = new ArrayList<>();
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        Cursor dbCursor;
        dbCursor = db.query("TABLE_WORDS",
                null, null, null,
                null, null, null);
        if (dbCursor.moveToFirst()) {
            do {
                String wName = dbCursor.getString(dbCursor.getColumnIndexOrThrow("WordName"));
                int isFavoriteInt = dbCursor.getInt(dbCursor.getColumnIndexOrThrow("isFavorite"));
                boolean isFavorite = isFavoriteInt == 1;
                words.add(new Word(wName, isFavorite));
            } while (dbCursor.moveToNext());
        }

        dbCursor.close();
        db.close();

        return words;
    }

    public boolean updateWordFavoriteStatus(Word word, boolean isFavorite) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("isFavorite", word.isFavorite() ? 1 : 0);
        int test = isFavorite ? 1 : 0;
        String a = String.valueOf(test);
        Log.d("Test_For_Favorite_SQL", a);
//        db.update("TABLE_WORDS", cv, "WordName = ?", new String[]{wordName});S
        try {
            int rowsUpdated = db.update("TABLE_WORDS", cv, "WordName = ?", new String[]{word.getWordName()});
            db.close();
            Log.e("uWFS","Получилось обновить WordFavoriteStatus!!!");
            return rowsUpdated > 0;

        }catch(Exception e){ Log.e("uWFS_Error","Не получилось обновить WordFavoriteStatus" , e); return false;}
    }

    public Integer DataBaseDeleter(String nameword) {
        Log.e("DataBaseDeleter", "Я НАЧИНАЮ УДАЛЯТЬ");
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        return db.delete("TABLE_WORDS", "WordName=?", new String[]{nameword});
    }

    public boolean isValueInDatabase(String value, String description) {
        SQLiteDatabase db = this.sqLiteHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM TABLE_WORDS WHERE WordName = ? AND WordMeaning = ?";
        Cursor cursor = db.rawQuery(query, new String[]{value, description});
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }

    public String getWordMeaning(String wordName) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String wordMeaning = null;
        Cursor cursor = db.query("TABLE_WORDS", new String[]{"WordMeaning"},
                "WordName = ?", new String[]{wordName}, null, null, null);

        if (cursor.moveToFirst()) {
            wordMeaning = cursor.getString(cursor.getColumnIndexOrThrow("WordMeaning"));
        }

        cursor.close();
        db.close();

        return wordMeaning;
    }
    public int getWordFavoriteStatus(String wordName) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        int wordFavoriteStatus = 0;
        Cursor cursor = db.query("TABLE_WORDS", new String[]{"isFavorite"},
                "WordName = ?", new String[]{wordName}, null, null, null);

        if (cursor.moveToFirst()) {
            wordFavoriteStatus = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite"));
        }

        cursor.close();
        db.close();

        return wordFavoriteStatus;
    }
}


/*
    public boolean saveWordMeaningToDatabase(WordMeaning wordMeaning){
        SQLiteDatabase db = this.sqLiteHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("WordMeaning",wordMeaning.getWordMeaning());
        long rowId = db.insert("TABLE_WORDS_Meaning",null,cv);
        cv.clear();
        db.close();
        return rowId != -1;
    }

    public ArrayList<WordMeaning> loadAllWordMeaningFromDatabase() {
        ArrayList<WordMeaning> wordMeaning = new ArrayList<>();
        SQLiteDatabase db = this.sqLiteHelper.getWritableDatabase();
        Cursor dbCursor = null;
        dbCursor = db.query("TABLE_WORDS_MEANING",
                null, null, null,
                null, null, null);
        if (dbCursor.moveToFirst()) {
            do {
                String wMeaning = dbCursor.getString(dbCursor.getColumnIndexOrThrow("wordMeaning"));
                wordMeaning.add(new WordMeaning(wMeaning));
            } while (dbCursor.moveToNext());
        }

        dbCursor.close();
        db.close();
        return wordMeaning;
    }

 */


