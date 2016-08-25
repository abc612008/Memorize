package com.abc612008.memorize;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentQuestionChoose extends FragmentQuestion {

    public FragmentQuestionChoose() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_choose, container, false);
        String question = getArguments().getString("Question");
        final String[] options = getArguments().getStringArray("Options");
        final int answerId=getArguments().getInt("Answer");
        final int wordID=getArguments().getInt("WordID");
        final int questionType=getArguments().getInt("QuestionType");
        final String sound=getArguments().getString("PlaySound");
        if(sound!=null&&!sound.isEmpty()&&getArguments().getBoolean("BeforePlay"))
            WordSoundPool.play(sound);
        ((TextView) view.findViewById(R.id.question)).setText(question);
        ListView optionsListView=((ListView) view.findViewById(R.id.options));
        assert options != null;
        optionsListView.setAdapter(
                new ArrayAdapter<>(getActivity(), R.layout.option_item, options));
        optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                if(sound!=null&&!sound.isEmpty()&&!getArguments().getBoolean("BeforePlay"))
                    WordSoundPool.play(sound);
                if(position==answerId) {
                    onCorrect.execute(wordID, questionType);
                } else {
                    Util.makeToast(getActivity(), "Incorrect. Answer:"+options[answerId]);
                    onIncorrect.execute(wordID, questionType);
                }
            }});
        return view;
    }

}
