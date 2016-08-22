package com.abc612008.memorize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddWordsActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    private void getWordData(final String word, Response.Listener<JSONObject> callback, final boolean failQueueEnable){
        requestQueue.add(new JsonObjectRequest(
                Request.Method.GET,
                "http://dict-co.iciba.com/api/dictionary.php?key="+BuildConfig.API_KEY+"&type=json&w="+word,
                null,
                callback,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(failQueueEnable)
                            Data.wordQueue.add(word);
                        else
                            Util.makeToast(AddWordsActivity.this,"Failed to add words. Please check your network connection.");
                    }
                }
        ));
    }

    private void addWord(JSONObject response) throws JSONException{
        // FIXME: 2016-08-20
        Data.words.add(new Word(
                response.getString("word_name"),
                response.getJSONArray("symbols").getJSONObject(0).getString("ph_am"),
                response.getJSONArray("symbols").getJSONObject(0).getJSONArray("parts").getJSONObject(0).getJSONArray("means").join(";").replace("\"", ""),
                "Not implement"
        ));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_add_words);
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

                    getWordData(word, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                addWord(response);
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
                    getWordData(word, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                addWord(response);
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
