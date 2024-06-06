package com.mirea.kt.ribo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WordAdd extends AppCompatActivity implements View.OnClickListener {

    private DBManager dbManager;
    public String value,valueDescription;
    private EditText editTextWordName, editTextWordMeaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_add);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setTitle("Second");
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        dbManager = new DBManager(new MyAppSQLiteHelper(this, "doctors.db", null, 1));
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnBack = findViewById(R.id.btnBack);

        editTextWordName = findViewById(R.id.etWordName);
        editTextWordMeaning = findViewById(R.id.etWordMeaning);

        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAdd) {
            if (this.dbManager != null) {
                String wName = editTextWordName.getText().toString();
                String wMeaning = editTextWordMeaning.getText().toString();
                if (!wName.isEmpty() && !wMeaning.isEmpty()) {
                    boolean result = dbManager.saveWordToDatabase(new Word(wName, false), new WordMeaning(wMeaning));
                    if (result) {
                        Toast.makeText(this, R.string.insert_success, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(this, R.string.insert_error, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, R.string.incorrect_value, Toast.LENGTH_LONG).show();
                }
            }
        } else if (v.getId() == R.id.btnBack) {
            startActivity(new Intent(this, TermsDictionary.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}