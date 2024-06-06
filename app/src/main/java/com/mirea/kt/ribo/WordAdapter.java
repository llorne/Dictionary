package com.mirea.kt.ribo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
    private Context context;
    MyAppSQLiteHelper sqLiteHelper;
    List<String> pdffiles;
    private int counter = 0;
    private ArrayList<Word> words;
    private  ArrayList<Word> wordsFiltered;


//    private ArrayList<WordMeaning> meanings;


    //    private ArrayList<WordMeaning> meanings;
    private DBManager dbManager;

    interface OnWordClickListener{
        void onWordClick(Word words, int position);
    }

    public WordAdapter(ArrayList<Word> words, Context context) {
        this.words = words;
        this.context = context;
        this.wordsFiltered = new ArrayList<>(words);
        this.dbManager = new DBManager(new MyAppSQLiteHelper(context, "doctors.db", null, 1));


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final ImageButton DeleteButton;
        final ImageButton imageButton;
        ImageButton share;



        public ViewHolder(View view) {
            super(view);

            nameView = view.findViewById(R.id.tvWordName);
            imageButton = view.findViewById(R.id.clickStarListener);
            DeleteButton = view.findViewById(R.id.clickDeleteListener);
            share=view.findViewById(R.id.shareBtn);

        }
    }


    @NonNull
    @Override
    public WordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent,
                false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.ViewHolder holder, int position) {

        Word word = words.get(position);

        dbManager = new DBManager(new MyAppSQLiteHelper(holder.itemView.getContext(), "doctors.db", null, 1));

        holder.nameView.setText(String.format(word.getWordName()));
        ArrayList<Word> words = new DBManager(new MyAppSQLiteHelper(holder.itemView.getContext(), "doctors.db", null, 1)).loadAllWordFromDatabase();


        holder.imageButton.setOnClickListener(v -> {
            Log.d("IMAGE_BUTTON_CLICK", "Клик по слову: " + word.getWordName());
            word.setFavorite(!word.isFavorite());
            if (word.isFavorite()) {
                holder.imageButton.setImageResource(R.drawable.ic_action_star);
                counter = 0;
            } else {
                holder.imageButton.setImageResource(R.drawable.ic_action_star_border);
                counter = 0;
            }
            Log.d("UPDATE_STATUS", "Обновления статуса для слова: " + word.getWordName());

            boolean updateStatus = dbManager.updateWordFavoriteStatus(word, word.isFavorite());
            Log.d("UPDATE_STATUS", "Обновлено статус у слова: " + word.getWordName() + " так: " + updateStatus);

            if (!updateStatus) {
                Log.e("UPDATE_ERROR", "Не получилось обновить статус favorite у: " + word.getWordName());
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wordName = word.getWordName();
                String wordMeaning = dbManager.getWordMeaning(wordName);
                String shareText = "Слово: " + wordName + "\nЗначение: " + wordMeaning;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent telegramIntent = new Intent(Intent.ACTION_SEND);
                telegramIntent.setType("text/plain");
                telegramIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                telegramIntent.setPackage("org.telegram.messenger");
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                whatsappIntent.setPackage("com.whatsapp");
                Intent chooserIntent = Intent.createChooser(intent, "Поделиться:");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{telegramIntent, whatsappIntent});
                context.startActivity(chooserIntent);
            }
        });

        holder.DeleteButton.setOnClickListener(v -> {
            dbManager.DataBaseDeleter(word.getWordName());
            Log.d("holder.DeleteButton", "Я НАЖАЛ НА ЗНАК УДАЛЕНИЯ");
            TermsDictionary td = new TermsDictionary();

//            td.update_loader("");
        });
        holder.itemView.setOnClickListener(v -> {
            if (context != null && word != null && word.getWordName() != null) {
                String wordMeaning = dbManager.getWordMeaning(word.getWordName());
                Intent intent = new Intent(context, Meaning.class);
                intent.putExtra("wordName", word.getWordName().toString());
                intent.putExtra("wordMeaning", wordMeaning);
                context.startActivity(intent);
            } else {
                Log.e("ITEM__CONTEXT", "Context or word is null");
            }
        });
        if (dbManager.getWordFavoriteStatus(word.getWordName())==1) {
            holder.imageButton.setImageResource(R.drawable.ic_action_star);
            counter++;
        } else {
            holder.imageButton.setImageResource(R.drawable.ic_action_star_border);
        }


    }
        private void readCursorData (Word word, ViewHolder viewHolder){
            SQLiteDatabase db =sqLiteHelper.getReadableDatabase();
            Cursor cursor = db.query("TABLE_WORDS", null, "WordName = ?", new String[]{word.getWordName()}, null, null, null);

            try {
                if (cursor.moveToFirst()) {
                    int isFavoriteIndex = cursor.getColumnIndex("isFavorite");
                    String isFavoriteStr = cursor.getString(isFavoriteIndex);
                    boolean isFavorite = isFavoriteStr.equals("1");

                    // Set the favorite status to the Word object
                    word.setFavorite(isFavorite);

                    // Update the UI accordingly
                    if (isFavorite) {
                        viewHolder.imageButton.setImageResource(R.drawable.ic_action_star);
                    } else {
                        viewHolder.imageButton.setImageResource(R.drawable.ic_action_star_border);
                    }
                }
            } finally {
                cursor.close();
                db.close();
            }
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    wordsFiltered = new ArrayList<>(words); // Показать все элементы
                } else {
                    ArrayList<Word> filteredList = new ArrayList<>();
                    for (Word word : words) {
                        if (word.getWordName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(word);
                        }
                    }
                    wordsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = wordsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                wordsFiltered = (ArrayList<Word>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public void filterList(ArrayList<Word> filteredList) {
        words = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return words.size();
    }
}