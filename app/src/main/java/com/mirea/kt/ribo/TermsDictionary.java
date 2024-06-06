package com.mirea.kt.ribo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ribo.onOptionsSelected;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TermsDictionary extends AppCompatActivity implements onOptionsSelected,WordAdapter.OnWordClickListener {
    private Word word;


    private SearchView searchView;
    private RecyclerView rcView;
    private WordAdapter adapter;
    private WordManager wordManager;

    private ArrayList<Word> words = new ArrayList<>();
    private boolean isFavorite;
    public String valueData;
//    public RecyclerView rV;
    private DBManager dbManager;
    private boolean checker = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        imageButton = findViewById(R.id.clickStarListener);
        setContentView(R.layout.activity_terms_dictionary);
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();

        dbManager = new DBManager(new MyAppSQLiteHelper(this, "doctors.db", null, 1));
        checker=false;
        if (!checker) {
            Bundle arguments = getIntent().getExtras();
            valueData = arguments.get("data").toString();
//        valueDescription=arguments.get("description").toString();
            Log.d("MyValueResult", valueData);
            try {
                JSONArray jsonArray = new JSONArray(valueData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String value = jsonObject.getString("value");
                    Log.e("GETVALUE__", value);
                    String valueDescription = jsonObject.getString("description");
                    if (!dbManager.isValueInDatabase(value, valueDescription)) {
                        checker=true;
                        boolean result = dbManager.saveWordToDatabase(new Word(value, false), new WordMeaning(valueDescription));
                    }
                }
            } catch (JSONException e) {
                Log.e("JSONException", "ДАННЫЕ НЕ ПЕРЕДАЛИСЬ");
            }
        }

        if (ab != null) {
            ab.setTitle("Словарь");
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        update_loader("");


    }

    @Override
    public void onWordClick(Word word,int position){
        Toast.makeText(this,"Click on"+word.getWordName()+"",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update_loader("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dictionary_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//                update_loader(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
//                adapter.getFilter().filter(newText);
                update_loader(query);
                return true;
            }
        });
        return true;
    }


//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Intent intent = new Intent(getApplicationContext(), SearchingResultActivity.class);
//                intent.putExtra("query", query);
//                startActivity(intent);
//                if (!searchView.isIconified()) {
//                    searchView.setIconified(true);
//                }
//                searchItem.collapseActionView();
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String s) {
//                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
//                return false;
//            }
//        });
////
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//
//        searchView.setQueryHint("Search terms...");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                filter(newText);
//                return true;
//            }
//        });


//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // Toast like print
//
//                if( ! searchView.isIconified()) {
//                    searchView.setIconified(true);
//                }
//                searchItem.collapseActionView();
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String s) {
//                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
//                return false;
//            }
//        });




//        SearchManager searchManager = (SearchManager) TermsDictionary.this.getSystemService(Context.SEARCH_SERVICE); //ПОНЯТЬ
//        SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(TermsDictionary.this.getComponentName()));
//        }
//        return super.onCreateOptionsMenu(menu);






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = menuItem.getItemId();
        if (R.id.action_favorite == item.getItemId()) {
            Toast.makeText(this, "Ваши избранные термины",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TermsDictionary.this,FavoriteShow.class);
            TermsDictionary.this.startActivity(intent);
            return true;
        } else if (R.id.action_update == item.getItemId()) {
            Toast.makeText(this, "Обновлено успешно!",
                    Toast.LENGTH_LONG).show();
//            loadWordsFromDatabase();
            update_loader("");


        } else if (R.id.action_exit == item.getItemId()) {
            finish();
            return true;
        } else if (R.id.action_add == item.getItemId()) {
            Intent switcher = new Intent(TermsDictionary.this, WordAdd.class);
            TermsDictionary.this.startActivity(switcher);
            return true;
        } else if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        } else if (R.id.clickStarListener == item.getItemId()) {

        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

//    private void loadWordsFromDatabase() {
//        try {
//            words = dbManager.loadAllWordFromDatabase();
//            if (words == null) {
//                words = new ArrayList<>();
//            }
//            if (adapter != null) {
//                adapter.filterList(words);
//            }
//            Log.d("TAG_", "Words loaded: " + words.size() + " words");
//        } catch (Exception e) {
//            Log.e("TAG_", "Error loading words from database", e);
//            Toast.makeText(this, "Error loading words", Toast.LENGTH_LONG).show();
//        }
//    }
//    private void filter(String text) {
//        ArrayList<Word> filteredList = new ArrayList<>();
//        for (Word word : words) {
//            if (word.getWordName().toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add(word);
//            }
//        }
//        adapter.filterList(filteredList);
//    }

    public void update_loader(String query) {
        ArrayList<Word> words = new DBManager(new MyAppSQLiteHelper(this, "doctors.db", null, 1)).loadAllWordFromDatabase();
        Log.i("Check__words", words.toString());
//        for (Word word : words) {
//            ArrayList<Word> isFavorite = word.isFavorite(); // Получаем статус isFavorite для текущего объекта word
//            Log.i("Check__Favorite","Word: " + word + ", isFavorite: " + isFavorite);
//        }
        RecyclerView rcView = findViewById(R.id.rView);
        WordAdapter adapter = new WordAdapter(words, (Context) this);
        rcView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        Log.i("ADAPTER_PRINT", adapter.toString());

        if (query != null && !query.isEmpty()) {
            ArrayList<Word> filteredWords = new ArrayList<>();
            Log.i("Check__words", words.toString());
            Set<Character> queryChars = new HashSet<>();
            for (char c : query.toCharArray()) {
                queryChars.add(c);
            }
            for (Word word : words) {
                if (!word.getWordName().isEmpty() && queryChars.contains(word.getWordName().charAt(0))) {
                    filteredWords.add(word);
                }
            }

            WordAdapter filteredAdapter = new WordAdapter(filteredWords, this);
            Log.i("FILTERED_WORDS", filteredWords.toString());
            rcView.setAdapter(filteredAdapter);

        }
        else{
            rcView.setAdapter(adapter);
        }
    }
}
//        for (Word word : words) {
//            if (imageButton != null) {
//                if (word.isFavorite()) {
//                    imageButton.setImageResource(R.drawable.ic_action_star);
//                } else imageButton.setImageResource(R.drawable.ic_action_star_border);
//            }
//        }

//        for (Word word : words) {
//            RecyclerView.ViewHolder holder = rcView.findViewHolderForAdapterPosition(i);
//            WordAdapter.ViewHolder wordHolder = (WordAdapter.ViewHolder) holder;
//            if (word.isFavorite()) {
//                wordHolder.imageButton.setImageResource(R.drawable.ic_action_star_border);
//            }else wordHolder.imageButton.setImageResource(R.drawable.ic_action_star);
//        }

//        if (word.isFavorite()) {
//            imageButton.setImageResource(R.drawable.ic_action_star);
//        }else {
//            imageButton.setImageResource(R.drawable.ic_action_star_border);
//        }




//    public void deleter(String ){
//        dbManager = new DBManager(new MyAppSQLiteHelper(this, "doctors.db", null, 1));
//        Integer delete = dbManager.DataBaseDeleter()
//    }

