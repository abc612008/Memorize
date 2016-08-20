package com.abc612008.memorize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddWordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_words);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] words = ((EditText)findViewById(R.id.edt_words)).getText().toString().split("\n");
                for (String word : words) {
                    Data.words.add(word);
                }
                AddWordsActivity.this.finish();
            }
        });
    }
}
