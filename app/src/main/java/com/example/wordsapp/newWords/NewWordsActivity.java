package com.example.wordsapp.newWords;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordsapp.MainActivity;
import com.example.wordsapp.R;
import com.example.wordsapp.adapter.WordAdapter;
import com.example.wordsapp.db.DbOperator;
import com.example.wordsapp.db.WordOperator;
import com.example.wordsapp.db.WordSQLHelper;

import java.util.Locale;

public class NewWordsActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private WordSQLHelper wordSQLHelper = new WordSQLHelper(this);
    private WordOperator wordOperator = new WordOperator(this, wordSQLHelper);
    private ListView newWordList;
    private TextToSpeech tts;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        //显示单词
        newWordList = findViewById(R.id.new_words_list);
        wordOperator = new WordOperator(this, wordSQLHelper);   //实例化数据库操作
        WordAdapter adapter = new WordAdapter(this, R.layout.port_list_view,
                wordOperator.getAll());     //执行查询，初始化适配器
        newWordList.setAdapter(adapter); //设置适配器，显示查询结果

        newWordList.setOnItemLongClickListener(this);   //长按删除单词
        newWordList.setOnItemClickListener(this);   //点击读音
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    /*
     * 点击读音,修改单词
     * */
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        text = ((TextView) view.findViewById(R.id.word_name)).getText().toString();
        final String meaning = ((TextView) view.findViewById(R.id.word_meaning)).getText().toString();
        final String prounce = ((TextView) view.findViewById(R.id.word_prounce)).getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否修改单词");
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //处理修改
                AlertDialog.Builder builder2 = new AlertDialog.Builder(NewWordsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.layout_update, null);
                builder2.setView(v).setTitle("请输入：");
                final EditText editText1 = (EditText) v.findViewById(R.id.update_name);
                final EditText editText2 = (EditText) v.findViewById(R.id.update_meaning);
                final EditText editText3 = (EditText) v.findViewById(R.id.update_prounce);
                editText1.setText(text);
                editText2.setText(meaning);
                editText3.setText(prounce);

                builder2.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String str1 = editText1.getText().toString();
                        final String str2 = editText2.getText().toString();
                        final String str3 = editText3.getText().toString();
                        wordOperator.update(str1, str2, str3);
                        Log.i("str1",str1+str2+str3);
                        //修改后更新单词列表
                        WordAdapter adapter = new WordAdapter(NewWordsActivity.this, R.layout.port_list_view,
                                wordOperator.getAll());
                        newWordList.setAdapter(adapter);
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                });
                builder2.show();
            }
        });

        builder.setNeutralButton("读音", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tts = new TextToSpeech(NewWordsActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        tts.setLanguage(Locale.ENGLISH);
                        // Log.i("text",text);
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH,  null,null);
                    }
                });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    /*
     *长按删除单词
     */
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        text = ((TextView) view.findViewById(R.id.word_name)).getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认要移出生词本吗？").setTitle("请确认删除");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                wordOperator.deleteWord(text);
                //删除后更新单词列表
                WordAdapter adapter = new WordAdapter(NewWordsActivity.this, R.layout.port_list_view,
                        wordOperator.getAll());
                newWordList.setAdapter(adapter);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
        return true;
    }
}
