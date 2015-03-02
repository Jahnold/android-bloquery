package com.bloc.bloquery.Adapters;

import android.content.Context;
import android.graphics.Paint;
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

            // get refs
            TextView userTextView = (TextView) convertView.findViewById(R.id.txt_user);
            TextView questionTextView = (TextView) convertView.findViewById(R.id.txt_question);

            // extract user object
            ParseObject user = question.getParseUser("user");

            // set text view text
            userTextView.setPaintFlags(userTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            userTextView.setText(user.getString("first_name") + " " + user.getString("last_name").substring(0,1));
            questionTextView.setText(question.getString("question"));

        }

        return convertView;
    }
}
