package com.mirea.kt.ribo;

import java.util.ArrayList;

public class WordManager {

    private static WordManager instance;
    private ArrayList<Word> words = new ArrayList<>();

    private WordManager() {
        // Приватный конструктор
    }

    public static synchronized WordManager getInstance() {
        if (instance == null) {
            instance = new WordManager();
        }
        return instance;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void addWord(Word word) {
        words.add(word);
    }

    public void clearWords() {
        words.clear();
    }


}