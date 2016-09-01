package com.abc612008.memorize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    GetAddWord gaw;

    private void setTexts(String word,String def_cn){
        ((TextView) findViewById(R.id.word)).setText(word);
        ((TextView) findViewById(R.id.definition_cn)).setText(def_cn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        gaw = new GetAddWord(this);
        ((EditText)findViewById(R.id.edt_word)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                gaw.getWordData(((EditText)findViewById(R.id.edt_word)).getText().toString(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    setTexts(response.getString("word_name"),response.getJSONArray("symbols").getJSONObject(0).getJSONArray("parts").getJSONObject(0).getJSONArray("means").join(";").replace("\"", ""));
                                } catch (JSONException ignored) {
                                }
                            }
                        }, true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTexts("","");
            }
        });
        findViewById(R.id.btn_add_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gaw.getWordData(((EditText)findViewById(R.id.edt_word)).getText().toString(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    gaw.addWord(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, true);
                setTexts("","");
            }
        });
    }
}
