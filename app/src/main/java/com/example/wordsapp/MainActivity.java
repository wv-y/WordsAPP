package com.example.wordsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wordsapp.WordsData.WordsData;
import com.example.wordsapp.adapter.SampleAdapter;
import com.example.wordsapp.adapter.WordAdapter;
import com.example.wordsapp.db.DbOperator;
import com.example.wordsapp.db.DictHelper;
import com.example.wordsapp.db.WordOperator;
import com.example.wordsapp.db.WordSQLHelper;
import com.example.wordsapp.jingshan.JinshanApi;
import com.example.wordsapp.newWords.NewWordsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private EditText editText;
    private Button but_search;
    private Button but_cancel;
    private ListView words_list, sample_list;
    private DbOperator dbOperator;
    private WordOperator wordOperator;
    private TextToSpeech tts;
    private String sample,str;
    JinshanApi jinshan = new JinshanApi();
    private String text, meaning, prounce;
    private WordsData wordsData = new WordsData();
    private DictHelper dictHelper = new DictHelper(this);
    private WordSQLHelper wordSQLHelper = new WordSQLHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.text_edit);
        but_search = (Button) findViewById(R.id.but_search);
        but_cancel = (Button) findViewById(R.id.but_cancel);
        words_list = (ListView) findViewById(R.id.words_list);
        but_search.setOnClickListener(this);
        but_cancel.setOnClickListener(this);


        words_list.setOnItemClickListener(MainActivity.this); //点击后读音,如果是在横屏下查找例句并显示
        words_list.setOnItemLongClickListener(MainActivity.this);//长按后将单词插入生词本


        //监听输入框文本改变
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                str = editText.getText().toString();
                if(!str.equals("")) {
                    dbOperator = new DbOperator(MainActivity.this, dictHelper);   //实例化数据库操作
                    WordAdapter adapter = new WordAdapter(MainActivity.this, R.layout.port_list_view,
                            dbOperator.wordQuery(str));     //执行查询，初始化适配器
                    words_list.setAdapter(adapter); //设置适配器，显示查询结果
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    /**
     * 点击搜索、编辑框，能够进行模糊查找
     * 点击取消，清空文本框里的内容
     */
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.text_edit:
            case R.id.but_search: {
                // final DictHelper dictHelper = new DictHelper(this);
                str = editText.getText().toString();
                if(!str.equals("")) {
                    dbOperator = new DbOperator(MainActivity.this, dictHelper);   //实例化数据库操作
                    WordAdapter adapter = new WordAdapter(MainActivity.this, R.layout.port_list_view,
                            dbOperator.wordQuery(str));     //执行查询，初始化适配器
                    words_list.setAdapter(adapter); //设置适配器，显示查询结果
                }
                    break;
            }

            case R.id.but_cancel: {
                str = editText.getText().toString();
                if (!str.equals(""))
                    editText.setText("");
                break;
            }
        }
    }

    /*
     * listView的点击事件监听器
     * 实现横竖屏下点击读音，横屏下点击同时显示网络例句
     * */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //通过view获取其内部的组件，进而进行操作
        //获得当前点击的单词内容
        text = ((TextView) view.findViewById(R.id.word_name)).getText().toString();
        //大多数情况下，position和id相同，并且都从0开始
        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.ENGLISH);
               // Log.i("text",text);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH,  null,null);
            }
        });


        /*
         *横屏下点击单词查找例句并显示
         * */
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) { //横屏下获取例句

            /**
             * 将查找的例句通过handler传出来，并显示
             */
            sample_list = (ListView) findViewById(R.id.sample_list);
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.what == 1) {
                        String str = (String) msg.getData().get("sample");

                        //显示到例句列表中
                        List<String> list = new ArrayList<>();
                        list.add(str);
                        SampleAdapter adapter = new SampleAdapter(MainActivity.this, R.layout.land_list_view,
                                list);     //执行查询，初始化适配器
                        sample_list.setAdapter(adapter); //设置适配器，显示查询结果

                    }
                }
            };

            /*
             * 在子进程中调用网络api方法
             * */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sample = jinshan.translation(text);//查找单词的例句
                        Message ms = new Message();
                        ms.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("sample", sample);
                        ms.setData(bundle);
                        handler.sendMessage(ms);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    /**
     * 长按将该单词加入生词本
     */

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        //通过view获取其内部的组件，进而进行操作
        //获得当前单词的详细内容
        wordsData.setName(((TextView) view.findViewById(R.id.word_name)).getText().toString());
        wordsData.setMeaning(((TextView) view.findViewById(R.id.word_meaning)).getText().toString());
        wordsData.setProunce(((TextView) view.findViewById(R.id.word_prounce)).getText().toString());

        // final DictHelper dictHelper = new DictHelper(this);

        wordOperator = new WordOperator(MainActivity.this, wordSQLHelper);   //实例化数据库操作
        try {
            wordOperator.insertWords(wordsData.getName(), wordsData.getMeaning(), wordsData.getProunce());//将数据插入生词本数据库
            Toast.makeText(MainActivity.this, "已加入生词本",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this,"加入失败",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //getMenuInflater()方法可以获得MenuInflater对象
        // 调用inflate方法创建一个菜单
        // 第一个参数：指定资源文件的位置
        // 第二个参数：指定我们的菜单条目要添加到哪一个menu对象中
        return true;        //代表是否可以把菜单显示出来
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        //获取条目的唯一id就可以判断点击的是哪一个条目
        {
            case R.id.new_words:// 跳转到生词本
                Intent intent = new Intent(MainActivity.this, NewWordsActivity.class);
                startActivity(intent);
                break;
            case R.id.help:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("点击单词可读音，长按加入生词本!\n" +
                        "生词本中长按可删除").setTitle("帮助");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Toast t = makeText(MainActivity.this, "用户按下了确认按钮",Toast.LENGTH_SHORT);
                    }
                });
                builder.show();
                break;
            default:
        }
        return true;
    }

}
