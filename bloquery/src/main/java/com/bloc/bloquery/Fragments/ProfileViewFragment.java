package com.bloc.bloquery.Fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bloc.bloquery.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 *  Fragment to view user's profiles
 */
public class ProfileViewFragment extends Fragment {

    private ParseUser mUser;

    // empty constructor
    public ProfileViewFragment() {}

    // user setter
    public void setUser(ParseUser user) { mUser = user; }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate view and get refs
        View v = inflater.inflate(R.layout.fragment_profile_view, container, false);

        TextView name = (TextView) v.findViewById(R.id.txt_name);
        final TextView desc = (TextView) v.findViewById(R.id.txt_description);
        final ImageView profile = (ImageView) v.findViewById(R.id.image_profile);
        ImageButton menu = (ImageButton) v.findViewById(R.id.btn_edit_menu);
        final EditText editDesc = (EditText) v.findViewById(R.id.et_description);
        Button btnSave = (Button) v.findViewById(R.id.btn_save_description);
        Button btnCancel = (Button) v.findViewById(R.id.btn_cancel);
        final LinearLayout buttons = (LinearLayout) v.findViewById(R.id.edit_buttons);

        // set the values from the user object
        if (mUser != null) {

            // name
            name.setText(mUser.getString("first_name") + " " + mUser.getString("last_name"));

            // description & edit text version
            if (mUser.getString("description") != null) {
                desc.setText(mUser.getString("description"));
                editDesc.setText(mUser.getString("description"));
            }


            // profile pic
            if (mUser.get("profile") != null ) {

                ParseFile profileFile = mUser.getParseFile("profile");
                profileFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {

                        if (e == null) {

                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            profile.setImageBitmap(bitmap);

                        }
                        else { e.printStackTrace(); }
                    }
                });

            }
            else {

                // no profile pic, set a generic
                profile.setImageResource(R.drawable.noprofile);
            }

            // menu
            if (mUser.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {

                // this is the profile of the current user, show the menu
                menu.setVisibility(View.VISIBLE);

                // create the pop up menu
                menu.setFocusable(false);
                final PopupMenu popupMenu = new PopupMenu(getActivity(), menu);
                popupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, getString(R.string.menu_profile_pic));
                popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, getString(R.string.menu_description));

                // set the menu button click listener
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupMenu.show();
                    }
                });

                // set the menu item click listeners
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case 0:
                                // edit profile pic
                                // start an intent to get a new picture
                                break;

                            case 1:
                                // edit description
                                // hide the text view and show the edit text buttons
                                desc.setVisibility(View.GONE);
                                editDesc.setVisibility(View.VISIBLE);
                                buttons.setVisibility(View.VISIBLE);
                                break;
                        }

                        return false;
                    }
                });

            }
            else {

                // make sure the menu button is invisible
                menu.setVisibility(View.INVISIBLE);
            }

            // edit description buttons click listeners
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // hide the edit stuff and show the text view
                    desc.setVisibility(View.VISIBLE);
                    editDesc.setVisibility(View.GONE);
                    buttons.setVisibility(View.GONE);

                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // save the description to the user object
                    mUser.put("description", editDesc.getText().toString());
                    mUser.saveInBackground();

                    // update the text view
                    desc.setText(editDesc.getText().toString());

                    // hide the edit stuff and show the text view
                    desc.setVisibility(View.VISIBLE);
                    editDesc.setVisibility(View.GONE);
                    buttons.setVisibility(View.GONE);

                }
            });

        }

        return v;
    }
}
