package com.bloc.bloquery;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.bloc.bloquery.Fragments.FeedFragment;
import com.bloc.bloquery.Fragments.LogInFragment;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class BloQuery extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blo_query);
        if (savedInstanceState == null) {

            // check whether there is a logged in user
            ParseUser user = ParseUser.getCurrentUser();
            if (user != null) {

                // show feed fragment
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new FeedFragment(), "FeedFragment")
                        .commit();

            }
            else {

                // no user, show login fragment
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new LogInFragment(), "LogInFragment")
                        .commit();
                
            }


        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blo_query, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_settings:
                return true;

            case R.id.action_logout:
                logout();

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void logout() {

        // log out with parse
        ParseUser.logOut();

        LogInFragment logInFragment = (LogInFragment) getFragmentManager().findFragmentByTag("LogInFragment");

        if (logInFragment == null) {
            logInFragment = new LogInFragment();
        }

        // change to log in fragment
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container,logInFragment, "LogInFragment")
                .commit();
    }

}
