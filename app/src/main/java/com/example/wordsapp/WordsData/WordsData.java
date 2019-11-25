package com.example.wordsapp.WordsData;

import android.net.Uri;
import android.provider.BaseColumns;

public class WordsData {
    private String name = "";
    private String meaning = "";
    private String prounce = "";

    public WordsData(){}
    /*public WordsData(String word, String meaning, String prounce){
        this.word = word;
        this.meaning = meaning;
        this.prounce = prounce;
    }*/

    public String getName() {
        return name;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getProunce() {
        return prounce;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setMeaning(String meaning){
        this.meaning=meaning;
    }

    public void setProunce(String prounce){
        this.prounce=prounce;
    }



}
