package com.example.dione.noticesapp.modules.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.bus.BusProvider;
import com.example.dione.noticesapp.modules.dashboard.adapter.AdminsAdapter;
import com.example.dione.noticesapp.modules.models.AccountsModel;
import com.example.dione.noticesapp.modules.models.NoticesModel;
import com.example.dione.noticesapp.modules.models.RefreshModel;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by Donds on 3/14/2017.
 */

public class AdminsFragment extends Fragment {
    private View view;
    private ArrayList<AccountsModel> accountsModelArrayList;
    private AdminsAdapter adminsAdapter;
    private GridView gridAdmins;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admins, container, false);
        accountsModelArrayList = new ArrayList<>();
        gridAdmins = (GridView) view.findViewById(R.id.grid_admins);
        adminsAdapter = new AdminsAdapter(getContext(), accountsModelArrayList);
        gridAdmins.setAdapter(adminsAdapter);


        return view;
    }

    @Subscribe
    public void onReceiveNotices(AccountsModel accountsModel) {
        bindList(accountsModel);
    }

    private void bindList(AccountsModel accountsModel) {
        accountsModelArrayList.add(accountsModel);
        adminsAdapter = new AdminsAdapter(getContext(), accountsModelArrayList);
        gridAdmins.setAdapter(adminsAdapter);
        adminsAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onReceiveRefresh(RefreshModel refreshModel) {
        if (refreshModel.isRefresh()) {
            accountsModelArrayList = new ArrayList<>();
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
}
