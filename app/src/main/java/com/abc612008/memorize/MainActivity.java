package com.abc612008.memorize;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    private int score;

    private void load(){
        score=getSharedPreferences("Data", MODE_PRIVATE).getInt("score",0);
        try {
            ObjectInputStream in = new ObjectInputStream(openFileInput("words.dat"));
            Object object;
            while((object=in.readObject()) != null){
                Data.words.add((Word)object);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save(){
        SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
        editor.putInt("score", score);
        editor.apply();
        try {
            ObjectOutputStream out = new ObjectOutputStream(openFileOutput("words.dat", Context.MODE_PRIVATE));
            for (Word word : Data.words) {
                out.writeObject(word);
            }
            out.writeObject(null);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int randomLessThan(int num){
        return (int)Math.floor(num*Math.random());
    }

    private Bundle getQuestion(){
        int wordNumber=Data.words.size();

        //选定一个word作为题目
        int id=randomLessThan(wordNumber);
        Word correctWord=Data.words.get(id);

        //随机生成选项个数和不重复的选项
        int optionNumber=(int)Math.ceil(3*Math.random())+2;
        if(optionNumber>wordNumber) optionNumber=wordNumber;

        HashSet<String> opsSet=new HashSet<>();
        while(opsSet.size()<optionNumber) {
            String wordNow=Data.words.get(randomLessThan(wordNumber)).word;
            if(wordNow.equals(correctWord.word)) continue;
            opsSet.add(wordNow);
        }

        String[] ops=new String[opsSet.size()];
        opsSet.toArray(ops);

        //随机选定一个位置放置正确答案
        int correctId=randomLessThan(optionNumber);
        ops[correctId]=correctWord.word;

        //填充参数
        Bundle args=new Bundle();
        args.putString("Question",correctWord.definition_cn);
        args.putStringArray("Options",ops);
        args.putInt("Answer",correctId);
        args.putInt("WordID",id);
        return args;
    }

    private void nextQuestion(){
        ((TextView)findViewById(R.id.txt_Score)).setText(String.format(getResources().getString(R.string.score), score));

        if(Data.words.size()<2){
            Util.makeToast(this, "Not enough words to memorize.");
            return;
        }

        FragmentQuestionChoose fr = new FragmentQuestionChoose();
        fr.setArguments(getQuestion());
        fr.setCallbacks(new QuestionCallback() {
            @Override
            public void execute(int position, int type) {
                //correct
                score+=10; //TODO: change score according rules
                Data.words.get(position).rememberProgresses[type]*=1.2;
                nextQuestion();
            }}, new QuestionCallback() {
            @Override
            public void execute(int position, int type) {
                //incorrect
                score-=10; //TODO: change score according rules
                Data.words.get(position).rememberProgresses[type]*=0.8;
                nextQuestion();
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.main_container, fr).commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddWordsActivity.class);
                startActivity(intent);
            }
        });

        nextQuestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_word_list) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WordList.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }
}
