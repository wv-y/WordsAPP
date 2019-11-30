package com.example.wordsapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wordsapp.WordsData.WordsData;

import java.util.LinkedList;
import java.util.List;


public class DbOperator {

    private static DictHelper dictHelper;
   // private static DbOperator dbOperator;
    private Context context;

    public DbOperator(Context context, DictHelper dbHelper){
        this.context = context;
        this.dictHelper = dbHelper;
    }

    /*public static DbOperator getDBOperator(View.OnClickListener context, DictHelper dbHelper){
        return dbOperator == null?new DbOperator(context,dbHelper):dbOperator;
    }*/

    //查询某个单词
   /* public WordsData getWord(String name) {
        String name1 = "";
        String meaning = "";
        String prounce = "";
        SQLiteDatabase db = WordHelper.getReadableDatabase();
        String sql = "select * from words where name=?";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        if (cursor.moveToNext()) {
            name1 = cursor.getString(cursor.getColumnIndex("name"));
            meaning = cursor.getString(cursor.getColumnIndex("meaning"));
            prounce = cursor.getString(cursor.getColumnIndex("prounce"));
        }
        WordsData wordsData = new WordsData();
        wordsData.setName(name1);
        wordsData.setMeaning(meaning);
        wordsData.setProunce(prounce);
        return wordsData;
    }*/




    /**
     * 在词典中模糊查询，将查到的结果放到列表里
     */
    public List<WordsData> wordQuery(String word) {
        List<WordsData> result = new LinkedList<>();
        String sql = "select * from word where word like ?";
        SQLiteDatabase db = dictHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{"%" + word + "%"});

        while (cursor.moveToNext()) {
            WordsData wordsData = new WordsData();
            String name = cursor.getString(cursor.getColumnIndex("word"));
            String meaning = cursor.getString(cursor.getColumnIndex("trans"));
            String prounce = cursor.getString(cursor.getColumnIndex("prounce_us"));
            wordsData.setName(name);
            wordsData.setMeaning(meaning);
            wordsData.setProunce(prounce);
            result.add(wordsData);
        }
        return result;
    }




}
