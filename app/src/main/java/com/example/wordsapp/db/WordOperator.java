package com.example.wordsapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wordsapp.WordsData.WordsData;

import java.util.ArrayList;
import java.util.List;

/*
*
* 生词本数据库操作
 */
public class WordOperator {
    private static WordSQLHelper WordHelper;

    private Context context;

    public WordOperator(Context context, WordSQLHelper dbHelper){
        this.context = context;
        this.WordHelper = dbHelper;
    }


    //删除生词本里的单词
    public void deleteWord(String name) {
        SQLiteDatabase db = WordHelper.getWritableDatabase();
        String sql = "delete from words where name = ?";
        db.execSQL(sql, new String[]{name});
       /* //刷新
        ((MainActivity)context).setWordsListView(getAll());*/
    }

    //向生词本插入单词
    public void insertWords(String name,String meaning,String prounce) {
        String sql = "insert into words(name,meaning,prounce)values(?,?,?)";
        SQLiteDatabase db = WordHelper.getWritableDatabase();

        db.execSQL(sql, new String[]{name, meaning, prounce});
    }

    //查找所有的生词本中的单词
    public List<WordsData> getAll(){
        String sql ="select * from words";
        SQLiteDatabase db = WordHelper.getReadableDatabase();
        List<WordsData> result = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,new String[]{});
        while (cursor.moveToNext()) {
            WordsData wordsData = new WordsData();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String meaning = cursor.getString(cursor.getColumnIndex("meaning"));
            String prounce = cursor.getString(cursor.getColumnIndex("prounce"));
            wordsData.setName(name);
            wordsData.setMeaning(meaning);
            wordsData.setProunce(prounce);
            result.add(wordsData);
        }
        return result;
    }

    public void update(String name1,String meaning1,String prounce1){
        SQLiteDatabase db = WordHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name1);
        values.put("meaning",meaning1);
        values.put("prounce",prounce1);
        db.update("words",values,"name=?",new String[]{name1});
    }
}
