package com.bloc.bloquery.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bloc.bloquery.Adapters.FeedQuestionAdapter;
import com.bloc.bloquery.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 *  Feed Fragment - Shows a list of the last 20 questions
 */
public class FeedFragment extends Fragment {

    private ArrayList<ParseObject> mQuestions = new ArrayList<>();
    private FeedQuestionAdapter mAdapter;

    // empty constructor
    public FeedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // tell android we have a menu for this fragment
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the view and get a ref to the list view
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        ListView questionsList = (ListView) v.findViewById(R.id.list_feed);

        // create the adapter and set it to our list view
        mAdapter = new FeedQuestionAdapter(
                getActivity(),
                0,
                mQuestions
        );
        questionsList.setAdapter(mAdapter);

        // load the 20 most recent questions from parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
        query.include("user");
        query.setLimit(20);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                if (e == null) {

                    // success
                    // replace questions with the result
                    mQuestions.clear();
                    mQuestions.addAll(parseObjects);

                    // update the adapter
                    mAdapter.notifyDataSetChanged();

                }
                else {
                    e.printStackTrace();
                }

            }
        });

        // set up a click listener for the feed items
        questionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get the question that was clicked on
                ParseObject question = mQuestions.get(position);

                // load the question fragment
                QuestionFragment questionFragment = (QuestionFragment) getFragmentManager().findFragmentByTag("QuestionFragment");
                if (questionFragment == null) {
                    questionFragment = new QuestionFragment();
                }

                // pass it our question then load it into view
                questionFragment.setQuestion(question);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, questionFragment, "QuestionFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // if it's not already loaded the inflate the menu for the 'new question' button
        if (menu.findItem(R.id.action_new_question) == null) {

            inflater.inflate(R.menu.menu_feed,menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     *  Handle clicks on the 'new question' actionbar item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_new_question) {

            NewQuestionDialogFragment dialog = new NewQuestionDialogFragment();
            dialog.setListener(new NewQuestionDialogFragment.NewQuestionListener() {
                @Override
                public void onNewQuestionConfirm(DialogFragment dialog, String questionText) {

                    // create a new question parse object
                    ParseObject question = new ParseObject("Question");
                    question.put("question", questionText);
                    question.put("user", ParseUser.getCurrentUser());
                    question.saveInBackground();

                    // add it to the feed of questions
                    mAdapter.add(question);

                }
            });
            dialog.show(getFragmentManager(), "NewQuestionDialog");

            return true;

        }
        else {

            return super.onOptionsItemSelected(item);

        }

    }
}
