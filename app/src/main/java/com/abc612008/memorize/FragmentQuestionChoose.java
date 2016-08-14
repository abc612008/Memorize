package com.abc612008.memorize;

import android.app.Fragment;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentQuestionChoose extends Fragment {

    private Callback onCorrect, onIncorrect;

    public FragmentQuestionChoose() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_choose, container, false);
        String question = getArguments().getString("Question");
        String[] options = getArguments().getStringArray("Options");
        final int answerId=getArguments().getInt("Answer");
        ((TextView) view.findViewById(R.id.question)).setText(question);
        ListView optionsListView=((ListView) view.findViewById(R.id.options));
        optionsListView.setAdapter(
                new ArrayAdapter<>(getActivity(), R.layout.option_item, options));
        optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                if(position==answerId)
                    onCorrect.execute();
                else
                    onIncorrect.execute();
            }});
        return view;
    }
    public void setCallbacks(Callback onCorrect,Callback onIncorrect) {
        this.onCorrect=onCorrect;
        this.onIncorrect=onIncorrect;
    }
}
