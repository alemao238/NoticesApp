package com.example.dione.noticesapp.modules.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.utilities.ApplicationConstants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Donds on 3/10/2017.
 */

public class ProfileFragment extends Fragment {
    @BindView(R.id.textview_display_name)
    TextView displayName;
    @BindView(R.id.textview_email)
    TextView email;
    @BindView(R.id.imageview_profile_image)
    ImageView profileImage;
    private SharedPreferenceManager sharedPreferenceManager;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        displayName.setText(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_DISPLAY_NAME, ""));
        email.setText(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_EMAIL, ""));
        Picasso.with(getContext())
                .load(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_PHOTO_URL, ""))
                .resize(50, 50)
                .centerCrop()
                .into(profileImage);
        return view;
    }
}
