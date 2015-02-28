package com.bloc.bloquery.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.bloc.bloquery.R;

/**
 *  New Question Dialog
 */
public class NewQuestionDialogFragment extends DialogFragment {

    public interface NewQuestionListener {
        public void onNewQuestionConfirm(DialogFragment dialog, String question);
    }

    private NewQuestionListener mListener;


    // empty constructor
    public NewQuestionDialogFragment() {}

    // listener setter
    public void setListener(NewQuestionListener listener) { mListener = listener; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // get builder and inflater
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // inflate and set the view for the dialog then add the title
        builder.setView(inflater.inflate(R.layout.fragment_new_question_dialog,null));
        builder.setTitle(R.string.title_new_question);

        // setup the buttons
        builder.setPositiveButton(R.string.btn_submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // get the name of the new collection
                EditText editText = (EditText) getDialog().findViewById(R.id.et_new_question);

                // call the listener confirm method
                mListener.onNewQuestionConfirm(NewQuestionDialogFragment.this, editText.getText().toString());

            }
        });

        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // user clicked cancel, do nothing
            }
        });

        return builder.create();
    }
}
