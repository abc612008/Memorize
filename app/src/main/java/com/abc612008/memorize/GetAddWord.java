package com.abc612008.memorize;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Null on 2016-08-24.
 */
public class GetAddWord {
    private RequestQueue requestQueue;
    public GetAddWord(Context context){
        this.requestQueue = Volley.newRequestQueue(context);
    }
    public void getWordData(final String word, Response.Listener<JSONObject> callback, final boolean failQueueEnable){
        requestQueue.add(new JsonObjectRequest(
                Request.Method.GET,
                "http://dict-co.iciba.com/api/dictionary.php?key="+BuildConfig.API_KEY+"&type=json&w="+word,
                null,
                callback,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(failQueueEnable) Data.wordQueue.add(word);
                    }
                }
        ));
    }

    public void addWord(JSONObject response) throws JSONException {
        // FIXME: 2016-08-20
        Data.words.add(new Word(
                response.getString("word_name"),
                response.getJSONArray("symbols").getJSONObject(0).getString("ph_am"),
                response.getJSONArray("symbols").getJSONObject(0).getJSONArray("parts").getJSONObject(0).getJSONArray("means").join(";").replace("\"", ""),
                "Not implement"
        ));
        new DownloadTask().execute(response.getJSONArray("symbols").getJSONObject(0).getString("ph_am_mp3"), "/memorize/audios/",response.getString("word_name")+".mp3");

    }
    private void downloadFile(String url, String path, String name) {
        try {
            URL u = new URL(url);
            DataInputStream stream = new DataInputStream(u.openStream());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            //noinspection ResultOfMethodCallIgnored
            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + path).mkdirs();
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + path + name));
            fos.write(buffer.toByteArray());
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class DownloadTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... args) {
            downloadFile(args[0], args[1], args[2]);
            return null;
        }
    }
}
