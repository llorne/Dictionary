package com.mirea.kt.ribo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteShow extends AppCompatActivity{ //implements View.OnClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_show);
        Toolbar tb = findViewById(R.id.FavoriteToolbar);
        ImageButton imageButton;
//        imageButton = findViewById(R.id.BackBtn);
        setSupportActionBar(tb);
        ArrayList<Word> words = new DBManager(new MyAppSQLiteHelper(this, "doctors.db", null, 1)).loadAllWordFromDatabase();
        Log.i("Check__words_favorite", words.toString());
        RecyclerView favoriteRView = findViewById(R.id.FavoriteRView);
        favoriteRView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        ArrayList<Word> favoriteWords = new ArrayList<>();

        Intent intent = new Intent(this, WordAdapter.class);

        for (Word word : words) {
            if (word.isFavorite()) {
                favoriteWords.add(word);
            }
        } intent.putExtra("favoritable",favoriteWords);

        WordAdapter favoriteAdapter = new WordAdapter(favoriteWords, this);
        favoriteRView.setAdapter(favoriteAdapter);
//        imageButton.setOnClickListener(this);
    }
//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent(FavoriteShow.this,TermsDictionary.class);
//        startActivity(intent);
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.simple_menu, menu);
//        return true;
//
//    }
}