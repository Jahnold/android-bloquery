package com.bloc.bloquery.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bloc.bloquery.BloQuery;
import com.bloc.bloquery.R;
import com.bloc.bloquery.Views.SquareImageView;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *  Fragment to view user's profiles
 */
public class ProfileViewFragment extends Fragment {

    private ParseUser mUser;
    private SquareImageView mProfilePic;

    // empty constructor
    public ProfileViewFragment() {}

    // user setter
    public void setUser(ParseUser user) { mUser = user; }

    private void setProfilePic(Bitmap pic) {

        // create a parse file for the bitmap
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final ParseFile profilePic = new ParseFile(mUser.getObjectId() + ".png", stream.toByteArray());
        profilePic.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {

                    // associate picture with user

                    mUser.put("profile", profilePic);
                    mUser.saveInBackground();

                }
                else { e.printStackTrace(); }
            }
        });

        // update the image view
        mProfilePic.setImageBitmap(pic);

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate view and get refs
        View v = inflater.inflate(R.layout.fragment_profile_view, container, false);

        TextView name = (TextView) v.findViewById(R.id.txt_name);
        final TextView desc = (TextView) v.findViewById(R.id.txt_description);
        mProfilePic = (SquareImageView) v.findViewById(R.id.image_profile);
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
                            mProfilePic.setImageBitmap(bitmap);

                        }
                        else { e.printStackTrace(); }
                    }
                });

            }
            else {

                // no profile pic, set a generic
                mProfilePic.setImageResource(R.drawable.noprofile);
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
                                Intent intent = new Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                );
                                //intent.putExtra("session_token", mUser.getSessionToken());
                                startActivityForResult(intent, 555);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 555 && resultCode == BloQuery.RESULT_OK && data != null) {

            // get the chosen image
            Uri imageUri = data.getData();
            Bitmap image = null;
            try {

                image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                Bitmap.createScaledBitmap(image, 500, 500, true);

            } catch (IOException e) { e.printStackTrace(); }

            setProfilePic(image);

        }
    }

}
