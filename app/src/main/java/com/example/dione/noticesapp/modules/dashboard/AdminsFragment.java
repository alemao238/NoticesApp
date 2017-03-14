package com.example.dione.noticesapp.modules.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.modules.dashboard.adapter.AdminsAdapter;
import com.example.dione.noticesapp.modules.models.AdminsModel;

import java.util.ArrayList;

/**
 * Created by Donds on 3/14/2017.
 */

public class AdminsFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admins, container, false);
        ArrayList<AdminsModel> adminsModelArrayList = new ArrayList<>();
        adminsModelArrayList.add(new AdminsModel("Android 1", "http://storage.googleapis.com/ix_choosemuse/uploads/2016/02/android-logo.png"));
        adminsModelArrayList.add(new AdminsModel("Android 2", "https://qph.ec.quoracdn.net/main-qimg-058f0f3cd298660496113a80f2d7ab27"));
        adminsModelArrayList.add(new AdminsModel("Android 3", "https://cdn4.iconfinder.com/data/icons/free-large-android-icons/512/Android.png"));
        adminsModelArrayList.add(new AdminsModel("Android 4", "http://storage.googleapis.com/ix_choosemuse/uploads/2016/02/android-logo.png"));
        adminsModelArrayList.add(new AdminsModel("Android 5", "https://qph.ec.quoracdn.net/main-qimg-058f0f3cd298660496113a80f2d7ab27"));
        adminsModelArrayList.add(new AdminsModel("Android 6", "https://cdn4.iconfinder.com/data/icons/free-large-android-icons/512/Android.png"));
        adminsModelArrayList.add(new AdminsModel("Android 7", "http://storage.googleapis.com/ix_choosemuse/uploads/2016/02/android-logo.png"));
        adminsModelArrayList.add(new AdminsModel("Android 8", "https://qph.ec.quoracdn.net/main-qimg-058f0f3cd298660496113a80f2d7ab27"));
        adminsModelArrayList.add(new AdminsModel("Android 9", "https://cdn4.iconfinder.com/data/icons/free-large-android-icons/512/Android.png"));
        adminsModelArrayList.add(new AdminsModel("Android 10", "http://storage.googleapis.com/ix_choosemuse/uploads/2016/02/android-logo.png"));
        adminsModelArrayList.add(new AdminsModel("Android 11", "https://qph.ec.quoracdn.net/main-qimg-058f0f3cd298660496113a80f2d7ab27"));
        adminsModelArrayList.add(new AdminsModel("Android 12", "https://cdn4.iconfinder.com/data/icons/free-large-android-icons/512/Android.png"));
        adminsModelArrayList.add(new AdminsModel("Android 13", "http://storage.googleapis.com/ix_choosemuse/uploads/2016/02/android-logo.png"));
        adminsModelArrayList.add(new AdminsModel("Android 14", "https://qph.ec.quoracdn.net/main-qimg-058f0f3cd298660496113a80f2d7ab27"));
        adminsModelArrayList.add(new AdminsModel("Android 15", "https://cdn4.iconfinder.com/data/icons/free-large-android-icons/512/Android.png"));
        adminsModelArrayList.add(new AdminsModel("Android 16", "http://storage.googleapis.com/ix_choosemuse/uploads/2016/02/android-logo.png"));
        adminsModelArrayList.add(new AdminsModel("Android 17", "https://qph.ec.quoracdn.net/main-qimg-058f0f3cd298660496113a80f2d7ab27"));
        adminsModelArrayList.add(new AdminsModel("Android 18", "https://cdn4.iconfinder.com/data/icons/free-large-android-icons/512/Android.png"));
        adminsModelArrayList.add(new AdminsModel("Android 19", "http://storage.googleapis.com/ix_choosemuse/uploads/2016/02/android-logo.png"));
        adminsModelArrayList.add(new AdminsModel("Android 20", "https://qph.ec.quoracdn.net/main-qimg-058f0f3cd298660496113a80f2d7ab27"));
        adminsModelArrayList.add(new AdminsModel("Android 21", "https://cdn4.iconfinder.com/data/icons/free-large-android-icons/512/Android.png"));
        GridView gridAdmins = (GridView) view.findViewById(R.id.grid_admins);
        AdminsAdapter adminsAdapter = new AdminsAdapter(getContext(), adminsModelArrayList);
        gridAdmins.setAdapter(adminsAdapter);


        return view;
    }
}
