package com.abc612008.memorize;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        FragmentManager fragment=getFragmentManager();
        FragmentQuestionChoose fr = new FragmentQuestionChoose();
        Bundle args=new Bundle();
        args.putString("Question","test");
        args.putStringArray("Options",new String[]{"1","2","3","4","5"});
        args.putInt("Answer",1);
        fr.setArguments(args);
        fr.setCallbacks(new Callback() {
            @Override
            public void execute() {
                //correct
                Log.d("choose","correct!");
            }}, new Callback() {
            @Override
            public void execute() {
                //incorrect
                Log.d("choose","incorrect!");
            }
        });
        fragment.beginTransaction().replace(R.id.main_container, fr).commit();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
