package com.abc612008.memorize;

import android.app.Fragment;

/**
 * Created by Null on 2016-08-23.
 */
public class FragmentQuestion extends Fragment {
    protected QuestionCallback onCorrect, onIncorrect;
    public void setCallbacks(QuestionCallback onCorrect, QuestionCallback onIncorrect) {
        this.onCorrect=onCorrect;
        this.onIncorrect=onIncorrect;
    }
}
