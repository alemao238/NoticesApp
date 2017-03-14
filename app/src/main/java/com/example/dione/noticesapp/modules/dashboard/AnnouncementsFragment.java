package com.example.dione.noticesapp.modules.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.application.NoticesApplication;
import com.example.dione.noticesapp.bus.BusProvider;
import com.example.dione.noticesapp.modules.dashboard.adapter.NoticesAdapter;
import com.example.dione.noticesapp.modules.models.NoticesModel;
import com.example.dione.noticesapp.modules.models.RefreshModel;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AnnouncementsFragment extends Fragment {
    private Unbinder unbinder;
    private ArrayList<NoticesModel> noticesModelArrayList;
    private NoticesAdapter noticesAdapter;
    @BindView(R.id.listview_notices)
    ListView listNotices;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_announcements, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Subscribe
    public void onReceiveNotices(NoticesModel noticesModel) {
        bindList(noticesModel);
    }

    @Subscribe
    public void onReceiveRefresh(RefreshModel refreshModel) {
        if (refreshModel.isRefresh()) {
            noticesModelArrayList = new ArrayList<>();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    private void bindList(NoticesModel noticesModel) {
        noticesModelArrayList.add(noticesModel);
        noticesAdapter = new NoticesAdapter(getActivity(), getContext(), R.layout.list_item_notices, noticesModelArrayList);
        listNotices.setAdapter(noticesAdapter);
        noticesAdapter.notifyDataSetChanged();
    }

}
