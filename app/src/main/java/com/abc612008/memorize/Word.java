package com.abc612008.memorize;

import java.io.Serializable;

/**
 * Created by Null on 2016-08-20.
 */
public class Word implements Serializable {
    public final String word, phonetic, definition_en, definition_cn;
    public double[] rememberProgresses;

    public Word(String _word,String _phonetic,String _definition_cn,String _definition_en){
        word=_word;
        phonetic=_phonetic;
        definition_cn=_definition_cn;
        definition_en=_definition_en;
        rememberProgresses=new double[Data.QuestionType.Max.ordinal()-1];
    }

    // The return value is between 0 to 100
    public int getAvgProgress(){
        double progressSum=0;
        for (double progress : rememberProgresses) {
            progressSum+=progress;
        }
        return (int)(progressSum/rememberProgresses.length*100);
    }
}
