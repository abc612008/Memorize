package com.abc612008.memorize;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentQuestionSpell extends FragmentQuestion {
    public FragmentQuestionSpell() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_fragment_question_spell, container, false);
        String question = getArguments().getString("Question");
        final String answer=getArguments().getString("AnswerWord");
        final int wordID=getArguments().getInt("WordID");
        final int questionType=getArguments().getInt("QuestionType");
        if(getArguments().getBoolean("BeforePlay"))
            WordSoundPool.play(answer);
        ((TextView)view.findViewById(R.id.question)).setText(question);
        ((EditText)view.findViewById(R.id.answer)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(((EditText) view.findViewById(R.id.answer)).getText().toString().equals(answer)) {
                    if(!getArguments().getBoolean("BeforePlay"))
                        WordSoundPool.play(answer);
                    onCorrect.execute(wordID, questionType);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(!getArguments().getBoolean("BeforePlay"))
                //    WordSoundPool.play(answer);
                if(((EditText) view.findViewById(R.id.answer)).getText().toString().equals(answer)) {
                    onCorrect.execute(wordID, questionType);
                } else {
                    Util.makeToast(getActivity(), "Incorrect. Answer:"+answer);
                    onIncorrect.execute(wordID, questionType);
                }
            }
        });
        return view;
    }
}
