package com.bloc.bloquery.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bloc.bloquery.R;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 *  Question Feed Adapter
 */
public class FeedQuestionAdapter extends ArrayAdapter<ParseObject> {

    // working copy of the questions list
    private ArrayList<ParseObject> mQuestions;

    // constructor
    public FeedQuestionAdapter(Context context, int textViewResourceId, ArrayList<ParseObject> items) {

        super(context, textViewResourceId, items);
        this.mQuestions = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // check whether the view needs inflating (it may be recycled)
        if (convertView == null) {
            convertView =  LayoutInflater.from(getContext()).inflate(R.layout.fragment_feed_item, null);
        }

        // get the parse object (question) at the current position
        ParseObject question = mQuestions.get(position);

        if (question != null) {

            TextView textView = (TextView) convertView.findViewById(R.id.txt_question);
            textView.setText(question.getString("question"));

        }

        return convertView;
    }
}
