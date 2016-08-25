package com.abc612008.memorize;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;

import java.util.HashMap;

/**
 * Created by Null on 2016-08-22.
 */
public class WordSoundPool {
    private static SoundPool soundPool;
    private static HashMap<String, Integer> sounds = new HashMap<String, Integer>();
    public static boolean mute=false;
    public static boolean first=true;

    public static void init(){
        soundPool=new SoundPool(3, AudioManager.STREAM_MUSIC ,0);
    }

    public static void play(String word){
        if(first) {first=false;return;}
        if(mute) return;
        if(sounds.containsKey(word)){
            soundPool.play(sounds.get(word), 1, 1, 0, 0, 1);
        }else{
            final int id=soundPool.load(Environment.getExternalStorageDirectory().getAbsolutePath() + "/memorize/audios/" + word +".mp3", 1);
            sounds.put(word, id);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                    soundPool.play(id, 1, 1, 1, 0, 1);
                }
            });
        }
    }
}
