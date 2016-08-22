package com.abc612008.memorize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        String[] words=new String[Data.words.size()];
        for (int i = 0; i < Data.words.size(); i++) {
            words[i]=Data.words.get(i).word;
        }
        ((ListView)findViewById(R.id.word_list)).setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, words));

        ((ListView)findViewById(R.id.queue_list)).setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Data.wordQueue.toArray()));
    }
}
