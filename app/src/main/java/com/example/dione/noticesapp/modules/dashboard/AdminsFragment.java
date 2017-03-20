package com.example.dione.noticesapp.modules.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.bus.BusProvider;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.modules.dashboard.adapter.AdminsAdapter;
import com.example.dione.noticesapp.modules.dashboard.adapter.ChatAdapter;
import com.example.dione.noticesapp.modules.dashboard.adapter.DividerItemDecoration;
import com.example.dione.noticesapp.modules.dashboard.adapter.NoticesAdapter;
import com.example.dione.noticesapp.modules.models.AccountsModel;
import com.example.dione.noticesapp.modules.models.ChatModel;
import com.example.dione.noticesapp.modules.models.NoticesModel;
import com.example.dione.noticesapp.modules.models.RefreshModel;
import com.example.dione.noticesapp.utilities.ApplicationConstants;
import com.example.dione.noticesapp.utilities.ErrorUtils;
import com.example.dione.noticesapp.utilities.Helpers;
import com.example.dione.noticesapp.utilities.InputValidator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import org.joda.time.LocalDate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Donds on 3/14/2017.
 */

public class AdminsFragment extends Fragment{
    private View view;
    private DatabaseReference chatsReference;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private SharedPreferenceManager sharedPreferenceManager;
    @BindView(R.id.recyclerview_chats)
    RecyclerView chatsList;
    @BindView(R.id.button_send)
    ImageView sendMessage;
    @BindView(R.id.edittext_message)
    EditText message;
    @BindView(R.id.til_message)
    TextInputLayout tilMessage;
    private ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private LocalDate localDate = new LocalDate();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admins, container, false);
        ButterKnife.bind(this, view);
        sharedPreferenceManager = new SharedPreferenceManager(getContext());

        chatAdapter = new ChatAdapter(chatModelArrayList, getContext());
        chatAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        chatsList.setLayoutManager(mLayoutManager);
        chatsList.setItemAnimator(new DefaultItemAnimator());
        chatsList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        chatsList.setAdapter(chatAdapter);
        return view;
    }

    @Subscribe
    public void onReceiveChats(ChatModel chatModel) {
        bindList(chatModel);
    }

    private void bindList(ChatModel chatModel) {
        chatModelArrayList.add(0, chatModel);
        chatAdapter = new ChatAdapter(chatModelArrayList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        chatsList.setLayoutManager(mLayoutManager);
        chatsList.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        chatsList.smoothScrollToPosition(0);
    }

    @Subscribe
    public void onReceiveRefresh(RefreshModel refreshModel) {
        if (refreshModel.isRefresh()) {
            chatModelArrayList = new ArrayList<>();
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

    @OnClick({R.id.button_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send:
                boolean isMessageValid = InputValidator.validate(getContext(), message, tilMessage);
                if (isMessageValid) {
                    chatsReference = database.child("chats");
                    chatsReference.child(localDate.toString()).push().setValue(new ChatModel(message.getText().toString(), sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME, "")));
                    message.getText().clear();
                }
                break;
        }
    }
}
