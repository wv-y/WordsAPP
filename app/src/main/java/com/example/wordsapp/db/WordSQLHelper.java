package com.example.wordsapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordSQLHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "database.db"; //数据库名字

    public static final int DB_VERSION = 1;     //数据库版本

    public static final String Create_DB =  "CREATE TABLE  words " +
            "(name VARCHAR(32) PRIMARY KEY NOT NULL," +
            "meaning TEXT,"+
            "prounce TEXT)";
    public WordSQLHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
        sqLiteDatabase.execSQL(Create_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
