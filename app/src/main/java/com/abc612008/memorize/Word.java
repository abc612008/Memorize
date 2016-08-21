package com.abc612008.memorize;

/**
 * Created by Null on 2016-08-20.
 */
public class Word {
    public final String word, phonetic, definition_en, definition_cn;
    public double[] rememberProgresses;

    public Word(String _word,String _phonetic,String _definition_cn,String _definition_en){
        word=_word;
        phonetic=_phonetic;
        definition_cn=_definition_cn;
        definition_en=_definition_en;
        rememberProgresses=new double[2];
    }
}
