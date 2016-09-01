package com.abc612008.memorize;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    private int score;

    private void load(boolean isExport){
        if(Data.words.size()!=0) return;
        score=getSharedPreferences("Data", MODE_PRIVATE).getInt("score",0);
        try {
            ObjectInputStream in;
            if(isExport)
                in = new ObjectInputStream(new FileInputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/memorize/exported_word.dat")));
            else
                in= new ObjectInputStream(openFileInput("words.dat"));
            Object object;
            while((object=in.readObject()) != null){
                Data.words.add((Word)object);
            }
            while((object=in.readObject()) != null){
                Data.wordQueue.add((String)object);
            }
            in.close();
            if(isExport) Util.makeToast(this, "Success!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save(boolean isExport){
        SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
        editor.putInt("score", score);
        editor.apply();
        try {
            ObjectOutputStream out;
            if(isExport)
                out = new ObjectOutputStream(new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/memorize/exported_word.dat")));
            else
                out = new ObjectOutputStream(openFileOutput("words.dat", Context.MODE_PRIVATE));
            for (Word word : Data.words) {
                out.writeObject(word);
            }
            out.writeObject(null);
            for (String word : Data.wordQueue) {
                out.writeObject(word);
            }
            out.writeObject(null);
            out.close();
            if(isExport) Util.makeToast(this, "Success!");
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
        int id, num=0;
        while(true){
            num++;
            id=randomLessThan(Data.words.size());
            if(Math.random()*100<100-Data.words.get(id).getAvgProgress()) break;
            if(num>Data.words.size()*2) break;
        }
        Word correctWord=Data.words.get(id);

        //选定一个题目类型,type越大,progresses[type]越大，被选中的概率越小
        int type=-1;
        while(type==-1||(type==Data.QuestionType.Audio.ordinal()&&(WordSoundPool.mute||WordSoundPool.first))) {
            for (type = 0; type != Data.QuestionType.Max.ordinal(); type++) {
                if (Math.random() < 1 - correctWord.rememberProgresses[type]) break;
            }
        }

        Bundle args = new Bundle();
        switch(Data.QuestionType.values()[type]) {
            case Definition: {
                boolean w2d = Math.random() < 0.5; // word to definition
                //随机生成选项个数和不重复的选项
                int optionNumber = (int) Math.ceil(3 * Math.random()) + 2;
                if (optionNumber > wordNumber - 1) optionNumber = wordNumber - 1;

                HashSet<String> opsSet = new HashSet<>();
                if (w2d) {
                    while (opsSet.size() < optionNumber) {
                        String defNow = Data.words.get(randomLessThan(wordNumber)).definition_cn;
                        if (defNow.equals(correctWord.definition_cn)) continue;
                        opsSet.add(defNow);
                    }
                } else {
                    while (opsSet.size() < optionNumber) {
                        String wordNow = Data.words.get(randomLessThan(wordNumber)).word;
                        if (wordNow.equals(correctWord.word)) continue;
                        opsSet.add(wordNow);
                    }
                }

                String[] ops = new String[opsSet.size()];
                opsSet.toArray(ops);

                //随机选定一个位置放置正确答案
                int correctId = randomLessThan(optionNumber);
                if (w2d)
                    ops[correctId] = correctWord.definition_cn;
                else
                    ops[correctId] = correctWord.word;

                //填充参数
                if (w2d)
                    args.putString("Question", correctWord.word);
                else
                    args.putString("Question", correctWord.definition_cn);
                args.putStringArray("Options", ops);
                args.putInt("Answer", correctId);
                args.putInt("WordID", id);
                args.putString("PlaySound", correctWord.word);
                args.putBoolean("BeforePlay", w2d);
                args.putInt("QuestionType", 0);
                break;
            }
            case Audio:
            {
                boolean a2d = Math.random() < 0.5; // audio to definition
                //随机生成选项个数和不重复的选项
                int optionNumber = (int) Math.ceil(3 * Math.random()) + 2;
                if (optionNumber > wordNumber - 1) optionNumber = wordNumber - 1;

                HashSet<String> opsSet = new HashSet<>();
                if (a2d) {
                    while (opsSet.size() < optionNumber) {
                        String defNow = Data.words.get(randomLessThan(wordNumber)).definition_cn;
                        if (defNow.equals(correctWord.definition_cn)) continue;
                        opsSet.add(defNow);
                    }
                } else {
                    while (opsSet.size() < optionNumber) {
                        String wordNow = Data.words.get(randomLessThan(wordNumber)).word;
                        if (wordNow.equals(correctWord.word)) continue;
                        opsSet.add(wordNow);
                    }
                }

                String[] ops = new String[opsSet.size()];
                opsSet.toArray(ops);

                //随机选定一个位置放置正确答案
                int correctId = randomLessThan(optionNumber);
                if (a2d)
                    ops[correctId] = correctWord.definition_cn;
                else
                    ops[correctId] = correctWord.word;

                //填充参数
                args.putString("Question", "(Audio)");
                args.putStringArray("Options", ops);
                args.putInt("Answer", correctId);
                args.putInt("WordID", id);
                args.putString("PlaySound", correctWord.word);
                args.putBoolean("BeforePlay", true);
                args.putInt("QuestionType", 1);
                break;
            }
            case Spell:
            {
                boolean definition=Math.random()<0.5; // definition or audio
                if(!definition&&(WordSoundPool.mute||WordSoundPool.first))definition=true;
                //填充参数
                args.putString("Question", definition?correctWord.definition_cn:"(Audio)");
                args.putString("AnswerWord", correctWord.word);
                args.putInt("WordID", id);
                args.putBoolean("BeforePlay", !definition);
                args.putInt("QuestionType", 2);
                break;
            }
        }
        return args;
    }

    private void nextQuestion(){
        ((TextView)findViewById(R.id.txt_Score)).setText(String.format(getResources().getString(R.string.score), score));

        if(Data.words.size()<3){
            Util.makeToast(this, "Not enough words to memorize. At least 3 words should be added.");
            return;
        }

        Bundle args=getQuestion();

        FragmentQuestion fr;
        if(args.getInt("QuestionType")==2) fr=new FragmentQuestionSpell();
        else fr=new FragmentQuestionChoose();

        fr.setArguments(args);
        fr.setCallbacks(new QuestionCallback() {
            @Override
            public void execute(int position, int type) {
                //correct
                score+=50;
                if(Data.words.get(position).rememberProgresses[type]==0) Data.words.get(position).rememberProgresses[type]=0.1;
                Data.words.get(position).rememberProgresses[type]*=2;
                if(Data.words.get(position).rememberProgresses[type]>1) Data.words.get(position).rememberProgresses[type]=1;
                nextQuestion();
            }}, new QuestionCallback() {
            @Override
            public void execute(int position, int type) {
                //incorrect
                score-=80;
                Data.words.get(position).rememberProgresses[type]*=0.5;
                nextQuestion();
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.main_container, fr).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WordSoundPool.init();

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_word_list) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WordListActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_search) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_mute) {
            WordSoundPool.mute=!WordSoundPool.mute;
            return true;
        }
        if (id == R.id.action_export) {
            save(true);
            return true;
        }
        if (id == R.id.action_import) {
            load(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        save(false);
    }
}
