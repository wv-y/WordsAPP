package com.example.wordsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wordsapp.R;
import com.example.wordsapp.WordsData.WordsData;

import java.util.List;
import java.util.Map;

public class WordAdapter extends ArrayAdapter {
    /*  public WordAdapter(Context context, int resource) {
          super(context, resource);
      }*/
    private final int resourceId;

    public WordAdapter(Context context, int textViewResourceId, List<WordsData> items) {
        super(context, textViewResourceId, items);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WordsData wordsData = (WordsData) getItem(position); // 获取当前项的实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象

        TextView wordName = (TextView) view.findViewById(R.id.word_name);   //获取单词name
        TextView wordMeaning = (TextView) view.findViewById(R.id.word_meaning);     //获取单词meaning
        TextView wordProunce = (TextView) view.findViewById(R.id.word_prounce);

        wordName.setText(wordsData.getName()); //为文本视图设置文本内容
        wordMeaning.setText(wordsData.getMeaning());
        wordProunce.setText(wordsData.getProunce());
        return view;
    }


}
