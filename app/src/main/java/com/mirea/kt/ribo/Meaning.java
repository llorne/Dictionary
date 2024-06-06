package com.mirea.kt.ribo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Meaning extends AppCompatActivity {
    private Word word;

    private WordMeaning wordMeaning;
    private TextView tvWordName, tvWordMeaning;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("EEEE__DD","1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meaning);


//        word = (Word) intent.getSerializableExtra("WordName");


//        wordMeaning = (WordMeaning) intent.getSerializableExtra("WordMeaning");


        tvWordName = findViewById(R.id.tvWordName);


        tvWordMeaning = findViewById(R.id.tvWordMeaning);


        Intent intent = getIntent();
        String wName =intent.getStringExtra("wordName");
        String wMeaning = intent.getStringExtra("wordMeaning");
//        WordMeaning wordMeaning = (WordMeaning)getIntent().getSerializableExtra("wordMeaning");

        tvWordName.setText(wName);
        tvWordMeaning.setText(wMeaning);



//        tvWordMeaning.setText(wordMeaning.toString());



        bottomNavigationView = findViewById(R.id.BottomNavigationView);


        bottomNavigationView.setOnClickListener(v -> {
            Intent switcher = new Intent(Meaning.this, TermsDictionary.class);


            Meaning.this.startActivity(switcher);
        });
    }
}
