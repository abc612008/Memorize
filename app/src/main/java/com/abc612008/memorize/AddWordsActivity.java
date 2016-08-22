package com.abc612008.memorize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.*;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddWordsActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    private void getWordData(String word, Response.Listener<JSONObject> callback){
        requestQueue.add(new JsonObjectRequest(
                Request.Method.GET,
                "http://dict-co.iciba.com/api/dictionary.php?key="+BuildConfig.API_KEY+"&type=json&w="+word,
                null,
                callback,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("getWordData","Failed to get the word because "+error.toString());
                        Util.makeToast(AddWordsActivity.this,"Failed to add words. Please check your network connection.");
                        //TODO: add it to list and add it later.
                    }
                }
        ));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_add_words);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] words = ((EditText)findViewById(R.id.edt_words)).getText().toString().split("\n");
                for (final String word : words) {
                    getWordData(word, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // FIXME: 2016-08-20
                                Data.words.add(new Word(
                                        response.getString("word_name"),
                                        response.getJSONArray("symbols").getJSONObject(0).getString("ph_am"),
                                        response.getJSONArray("symbols").getJSONObject(0).getJSONArray("parts").getJSONObject(0).getJSONArray("means").join(";").replace("\"", ""),
                                        "Not implement"
                                ));
                            } catch (JSONException e) {
                                Util.makeToast(AddWordsActivity.this, "Word " + word + " cannot be found or unknown error");
                                e.printStackTrace();
                            }
                        }
                    });
                }
                AddWordsActivity.this.finish();
            }
        });
    }
}
