package com.abc612008.memorize;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentQuestionChoose extends Fragment {

    private String question;
    private String[] options;
    private String answer;

    public FragmentQuestionChoose() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_choose, container, false);
        question=getArguments().getString("Question");
        options=getArguments().getStringArray("Options");
        answer=getArguments().getString("Answer");
        ((TextView) view.findViewById(R.id.question)).setText(question);
        return view;
    }
}
