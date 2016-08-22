package com.abc612008.memorize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WordList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView lv= new ListView(this);
        String[] words=new String[Data.words.size()];
        for (int i = 0; i < Data.words.size(); i++) {
            words[i]=Data.words.get(i).word;
        }
        lv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, words));
        setContentView(lv);
    }
}
