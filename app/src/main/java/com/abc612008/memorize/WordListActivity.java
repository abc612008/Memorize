package com.abc612008.memorize;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class WordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        Vector<Word> sortedWords = new Vector<>(Data.words);
        Collections.sort(sortedWords,new Comparator<Word>(){
            public int compare(Word arg1, Word arg2) {
                double progress1=0,progress2=0;
                for (double progress : arg1.rememberProgresses) progress1+=progress;
                for (double progress : arg2.rememberProgresses) progress2+=progress;
                return Double.compare(progress1, progress2);
            }
        });
        ((ListView)findViewById(R.id.word_list)).setAdapter(new WordListAdapter(this, sortedWords));

        ((ListView)findViewById(R.id.queue_list)).setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Data.wordQueue.toArray()));
    }


    private class WordListAdapter extends BaseAdapter {

        public final class ViewHolder{
            public TextView word,phonetic,definition_cn,definition_en;
            public ProgressBar progress;
        }

        private LayoutInflater mInflater;

        private Vector<Word> _data;

        public WordListAdapter(Context context, Vector<Word> dataSrc) {
            this.mInflater = LayoutInflater.from(context);
            this._data=dataSrc;
        }

        private Vector<Word> getData(){
            return _data;
        }

        @Override
        public int getCount() {
            return getData().size();
        }

        @Override
        public Object getItem(int position) {
            return getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.word_item, parent, false);
                holder = new ViewHolder();
                holder.word = (TextView) convertView.findViewById(R.id.word);
                holder.phonetic = (TextView) convertView.findViewById(R.id.phonetic);
                holder.definition_cn = (TextView) convertView.findViewById(R.id.definition_cn);
                holder.definition_en = (TextView) convertView.findViewById(R.id.definition_en);
                holder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }
            holder.word.setText(getData().get(position).word);
            holder.phonetic.setText(getData().get(position).phonetic);
            holder.definition_cn.setText(getData().get(position).definition_cn);
            holder.definition_en.setText(getData().get(position).definition_en);
            double[] progresses = getData().get(position).rememberProgresses;
            double prog=0;
            for (double progress : progresses) {
                prog+=progress;
            }
            prog/=progresses.length;
            prog*=100;
            holder.progress.setProgress((int)prog);
            return convertView;
        }
    }
}
