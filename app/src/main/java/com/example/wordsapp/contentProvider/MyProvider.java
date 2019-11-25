package com.example.wordsapp.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


import com.example.wordsapp.db.WordSQLHelper;

public class MyProvider extends ContentProvider {

    private WordSQLHelper myDBHelpter;
    private Context mContext;
    public static final String AUTOHORITY = "com.example.wordsapp.provider";
    // 设置ContentProvider的唯一标识
    public static final int User_Code = 0;
    public static final int USER_ITEM = 1;
    // UriMatcher类使用:在ContentProvider 中注册URI

    //创建UriMatcher对象
    private static final UriMatcher uriMatcher;
    static{
        //实例化UriMatcher对象
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 可以实现匹配URI的功能 参数1：authority 参数2：路径 参数3：自定义代码
        uriMatcher.addURI(AUTOHORITY,"words", User_Code);
        uriMatcher.addURI(AUTOHORITY, "words/#", USER_ITEM);
        // 若URI资源路径 = content://cn.scu.myprovider/user ，则返回注册码User_Code
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        //实现创建MyDBHelpter对象
        myDBHelpter = new WordSQLHelper(getContext());
        Log.i("test","hi");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = myDBHelpter.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case User_Code:
                cursor = db.query("words",strings,s,strings1,null,null,s1);
                break;
            case USER_ITEM:
                String queryId = uri.getPathSegments().get(1);
                 cursor = db.query("words",strings,"name=?",new String[]{queryId},null,null,s1);
                break;
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        SQLiteDatabase db = myDBHelpter.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case USER_ITEM:
            case User_Code:
                //参数1：表名  参数2：没有赋值的设为空   参数3：插入值
                /*long newWordsId*/
                db.insert("words", null, contentValues);
               // uriRetuen = Uri.parse("content://"+AUTOHORITY+"/words/"+newWordsId);
                mContext.getContentResolver().notifyChange(uri,null);
                break;
            default:
                break;
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = myDBHelpter.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case User_Code:
                db.delete("words",s,strings);
                break;
            case USER_ITEM:
                String deleteId = uri.getPathSegments().get(1);
                db.delete("words","name=?",new String []{deleteId});
                break;
            default:
                break;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = myDBHelpter.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case User_Code:
                db.update("words",contentValues,s,strings);
                break;
            case USER_ITEM:
                String updateId = uri.getPathSegments().get(1);
                db.update("words",contentValues,"name=?",new String []{updateId});
                break;
            default:
                break;
        }

        return 0;
    }
}
