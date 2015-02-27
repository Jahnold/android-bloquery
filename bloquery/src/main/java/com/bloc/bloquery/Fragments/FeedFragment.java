package com.bloc.bloquery.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloc.bloquery.R;

/**
 * Created by matthewarnold on 26/02/15.
 */
public class FeedFragment extends Fragment {

    // empty constructor
    public FeedFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_feed, container, false);

        return v;
    }
}
