package com.bloc.bloquery.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bloc.bloquery.Adapters.AnswerAdapter;
import com.bloc.bloquery.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 *  Question Fragment
 */
public class QuestionFragment extends Fragment {

    private ParseObject mQuestion;
    private ArrayList<ParseObject> mAnswers = new ArrayList<>();
    private AnswerAdapter mAdapter;

    // empty constructor
    public QuestionFragment() {}

    // question setter
    public void setQuestion(ParseObject question) { mQuestion = question; }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // tell android we have a menu for this fragment
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate view and get refs
        View v = inflater.inflate(R.layout.fragment_question,container,false);
        TextView userTextView = (TextView) v.findViewById(R.id.txt_user);
        TextView questionTextView = (TextView) v.findViewById(R.id.txt_question);
        ListView answerList = (ListView) v.findViewById(R.id.list_answers);

        // load the question details
        if (mQuestion != null) {

            // extract user
            ParseObject user = mQuestion.getParseUser("user");

            // user and question text
            userTextView.setPaintFlags(userTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            userTextView.setText(user.getString("first_name") + " " + user.getString("last_name").substring(0,1));
            questionTextView.setText(mQuestion.getString("question"));

            // create the adapter and set it to our list view
            mAdapter = new AnswerAdapter(
                    getActivity(),
                    0,
                    mAnswers
            );
            answerList.setAdapter(mAdapter);

            // load any answers
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Answer");
            query.whereEqualTo("question", mQuestion);
            query.include("user");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> answers, ParseException e) {

                    if (e == null) {

                        // add the loaded answers to our list view
                        mAnswers.addAll(answers);
                        mAdapter.notifyDataSetChanged();

                    }
                    else {
                        e.printStackTrace();
                    }
                }
            });

        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // if it's not already loaded the inflate the menu for the 'new question' button
        if (menu.findItem(R.id.action_new_answer) == null) {

            inflater.inflate(R.menu.menu_question,menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     *  Handle clicks on the 'new answer' actionbar item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_new_answer) {

            NewAnswerDialogFragment dialog = new NewAnswerDialogFragment();
            dialog.setListener(new NewAnswerDialogFragment.NewAnswerListener() {
                @Override
                public void onNewAnswerConfirm(DialogFragment dialog, String answerText) {

                    // create a new answer parse object
                    ParseObject answer = new ParseObject("Answer");
                    answer.put("answer", answerText);
                    answer.put("points", 0);
                    answer.put("question", mQuestion);
                    answer.put("user", ParseUser.getCurrentUser());
                    answer.saveInBackground();

                    // add answer to the list view
                    mAdapter.add(answer);

                }
            });

            dialog.show(getFragmentManager(), "NewAnswerDialog");

            return true;

        }
        else {

            return super.onOptionsItemSelected(item);

        }

    }

}
