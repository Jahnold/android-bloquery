package com.bloc.bloquery.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bloc.bloquery.R;
import com.parse.ParseObject;

/**
 *  Question Fragment
 */
public class QuestionFragment extends Fragment {

    private ParseObject mQuestion;

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


            return true;

        }
        else {

            return super.onOptionsItemSelected(item);

        }

    }

}
