package com.bloc.bloquery.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bloc.bloquery.R;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 *  Answer Adapter
 */
public class AnswerAdapter extends ArrayAdapter<ParseObject> {

    // working copy of the questions list
    private ArrayList<ParseObject> mAnswers;

    // constructor
    public AnswerAdapter(Context context, int textViewResourceId, ArrayList<ParseObject> items) {

        super(context, textViewResourceId, items);
        this.mAnswers = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // check whether the view needs inflating (it may be recycled)
        if (convertView == null) {
            convertView =  LayoutInflater.from(getContext()).inflate(R.layout.adapter_answer, null);
        }

        // get the parse object (answer) at the current position
        final ParseObject answer = mAnswers.get(position);

        if (answer != null) {

            // get refs
            TextView userTextView = (TextView) convertView.findViewById(R.id.txt_user);
            TextView answerTextView = (TextView) convertView.findViewById(R.id.txt_answer);
            final TextView pointsTextView = (TextView) convertView.findViewById(R.id.txt_points);
            ImageButton btnVoteUp = (ImageButton) convertView.findViewById(R.id.btn_vote_up);
            ImageButton btnVoteDown = (ImageButton) convertView.findViewById(R.id.btn_vote_down);

            // extract the user
            ParseObject user = answer.getParseUser("user");

            // set the text
            userTextView.setText(user.getString("first_name") + " " + user.getString("last_name").substring(0,1));
            answerTextView.setText(answer.getString("answer"));
            pointsTextView.setText(String.valueOf(answer.getInt("points")));

            // set click listeners on the buttons
            btnVoteUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // increment the points
                    answer.increment("points");
                    answer.saveInBackground();
                    pointsTextView.setText(String.valueOf(answer.getInt("points")));

                }
            });

            btnVoteDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // decrement the points
                    answer.increment("points", -1);
                    answer.saveInBackground();
                    pointsTextView.setText(String.valueOf(answer.getInt("points")));

                }
            });

        }

        return convertView;
    }
}
