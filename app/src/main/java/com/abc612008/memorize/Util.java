package com.abc612008.memorize;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Null on 2016-08-21.
 */
public class Util {
    public static void makeToast(Context con, String msg){
        Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
    }
}
