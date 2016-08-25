package com.abc612008.memorize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class AddWordsActivity extends AppCompatActivity {
    GetAddWord gaw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_words);
        gaw=new GetAddWord(this);
        //normal add
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] words = ((EditText)findViewById(R.id.edt_words)).getText().toString().split("\n");
                for (final String word : words) {
                    //word duplicate check
                    if(Data.wordQueue.contains(word)) continue;
                    boolean check=true;
                    for (Word w : Data.words) {
                        if(w.word.equals(word)){
                            check=false;
                            break;
                        }
                    }
                    if(!check) continue;
                    int count=0;
                    for (String s : words) {
                        if(s.equals(word)) count++;
                        if(count>1) break;
                    }
                    if(count>1) continue;

                    gaw.getWordData(word, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                gaw.addWord(response);
                            } catch (JSONException e) {
                                Util.makeToast(AddWordsActivity.this, "Word " + word + " cannot be found or unknown error");
                                e.printStackTrace();
                            }
                        }
                    }, true);
                }
                AddWordsActivity.this.finish();
            }
        });
        //add words in queue
        findViewById(R.id.btn_queue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] words = new String[Data.wordQueue.size()];
                Data.wordQueue.toArray(words);
                for (final String word : words) {
                    gaw.getWordData(word, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                gaw.addWord(response);
                                Data.wordQueue.remove(word);
                            } catch (JSONException e) {
                                Util.makeToast(AddWordsActivity.this, "Word " + word + " cannot be found or unknown error");
                                e.printStackTrace();
                            }
                        }
                    }, false);
                }
                AddWordsActivity.this.finish();
            }
        });
    }
}
