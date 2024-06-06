package com.mirea.kt.ribo;

public class Word {
    private String WordName;
    private boolean isFavorite;

    public Word(String wordName, boolean isFavorite) {
        WordName = wordName;
        this.isFavorite = isFavorite;
    }

    public String getWordName() {
        return WordName;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}