package com.bloc.bloquery.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bloc.bloquery.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by matthewarnold on 26/02/15.
 */
public class RegisterFragment extends Fragment {

    // empty constructor
    public RegisterFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register, container, false);

        // get refs
        final EditText txtEmail = (EditText) v.findViewById(R.id.et_email);
        final EditText txtFirst = (EditText) v.findViewById(R.id.et_first_name);
        final EditText txtLast = (EditText) v.findViewById(R.id.et_last_name);
        final EditText txtPassword = (EditText) v.findViewById(R.id.et_password);
        Button register = (Button) v.findViewById(R.id.btn_register);

        // set the button listener
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the values for all the fields
                String email = txtEmail.getText().toString();
                String first = txtFirst.getText().toString();
                String last = txtLast.getText().toString();
                String password = txtPassword.getText().toString();

                // make sure none of them are null
                if (email != null && first != null && last != null & password != null) {

                    // create a new Parse user
                    ParseUser user = new ParseUser();
                    user.setUsername(email);
                    user.setPassword(password);
                    user.put("first_name", first);
                    user.put("last_name", last);

                    // try a signup
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                // user created successfully, redirect to feed fragment
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container, new FeedFragment(), "FeedFragment")
                                        .commit();

                            }
                            else {

                                // something went wrong
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });

        return v;
    }
}
