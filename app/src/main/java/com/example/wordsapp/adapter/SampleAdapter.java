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

public class SampleAdapter extends ArrayAdapter {
    private final int resourceId;

    public SampleAdapter(Context context, int textViewResourceId, List<String> items) {
        super(context, textViewResourceId, items);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String SampleData = (String) getItem(position); // 获取当前项的F实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象

        TextView sample = (TextView) view.findViewById(R.id.word_sample);
        sample.setText(SampleData);
        return view;
    }

}
