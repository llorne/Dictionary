package com.mirea.kt.ribo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Meaning extends AppCompatActivity {
    private TextView tvWordName, tvWordMeaning;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("EEEE__DD", "1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meaning);

        tvWordName = findViewById(R.id.tvWordName);
        tvWordMeaning = findViewById(R.id.tvWordMeaning);

        Intent intent = getIntent();
        String wName = intent.getStringExtra("wordName");
        String wMeaning = intent.getStringExtra("wordMeaning");

        tvWordName.setText(wName);
        tvWordMeaning.setText(wMeaning);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Intent switcher = new Intent(Meaning.this, MainActivity.class);
                    startActivity(switcher);
                } else if (id == R.id.nav_terms) {
                    Intent switcher = new Intent(Meaning.this, TermsDictionary.class);
                    startActivity(switcher);
                }
                else if (id == R.id.nav_favorite){
                    Intent switcher = new Intent(Meaning.this,FavoriteShow.class);
                    startActivity(switcher);
                }
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });
    }
}
