package com.abc612008.memorize;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentQuestionSpell extends FragmentQuestion {
    public FragmentQuestionSpell() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fragment_question_spell, container, false);
        String question = getArguments().getString("Question");
        final String answer=getArguments().getString("AnswerWord");
        final int wordID=getArguments().getInt("WordID");
        final int questionType=getArguments().getInt("QuestionType");
        if(getArguments().getBoolean("BeforePlay"))
            WordSoundPool.play(answer);
        ((TextView)view.findViewById(R.id.question)).setText(question);
        view.findViewById(R.id.answer).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(((EditText) view.findViewById(R.id.answer)).getText().toString().equals(answer)) {
                    if(!getArguments().getBoolean("BeforePlay"))
                        WordSoundPool.play(answer);
                    onCorrect.execute(wordID, questionType);
                }
                return false;
            }
        });
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getArguments().getBoolean("BeforePlay"))
                    WordSoundPool.play(answer);
                if(((EditText) view.findViewById(R.id.answer)).getText().toString().equals(answer)) {
                    onCorrect.execute(wordID, questionType);
                } else {
                    Util.makeToast(getActivity(), "错误，正确答案为:"+answer);
                    onIncorrect.execute(wordID, questionType);
                }
            }
        });
        return view;
    }
}
