package com.example.dione.noticesapp.modules.chats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.modules.dashboard.adapter.ChatAdapter;
import com.example.dione.noticesapp.modules.dashboard.adapter.DividerItemDecoration;
import com.example.dione.noticesapp.modules.models.ChatModel;
import com.example.dione.noticesapp.utilities.ApplicationConstants;
import com.example.dione.noticesapp.utilities.Helpers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChatActivity extends AppCompatActivity implements ValueEventListener{
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private SharedPreferenceManager sharedPreferenceManager;
    private Helpers helpers;
    private DatabaseReference myChatReference;
    private String CHATMATE = "";
    private String FINAL_CHAT_ENDPOINT = ApplicationConstants.FIREBASE_ENDPOINT_CHATS;
    private String CHATTING = "";
    @BindView(R.id.recyclerview_chats)
    RecyclerView chatsList;
    @BindView(R.id.button_send)
    ImageView sendMessage;
    private ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();
    private ChatAdapter chatAdapter;


    @OnClick({R.id.button_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send:
                LocalDate localDate = new LocalDate();

                Log.d("ENDPOINT", sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_UID,""));
                Log.d("ENDPOINT", sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME, ""));
                Log.d("ENDPOINT", CHATTING);

                DatabaseReference replyReference = database.child("chats" + "/" + sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_UID,"") +
                        "/" +
                        CHATTING);
                replyReference.child(localDate.toString()).push().setValue(new ChatModel("custom message", sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME,"")));
//                chatReference.child(accountsModels.get(position).getUid())
//                        .child(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME, ""))
//                        .child(new LocalDate().toString())
//                        .push().setValue(new ChatModel("My message",
//                        sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME, "")));
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        chatAdapter = new ChatAdapter(chatModelArrayList, getApplicationContext());
        chatAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        chatsList.setLayoutManager(mLayoutManager);
        chatsList.setItemAnimator(new DefaultItemAnimator());
        chatsList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        chatsList.setAdapter(chatAdapter);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
        helpers = new Helpers(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            FINAL_CHAT_ENDPOINT += bundle.getString(ApplicationConstants.KEY_UID);
            CHATMATE = bundle.getString(ApplicationConstants.KEY_CHATMATE);
            CHATTING = bundle.getString(ApplicationConstants.KEY_USERNAME);
            myChatReference = database.child(FINAL_CHAT_ENDPOINT);
            myChatReference.addValueEventListener(this);
            Log.d("DSADASD", FINAL_CHAT_ENDPOINT + "/" + sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME, ""));
        } else {
            helpers.showToast(getString(R.string.error_illegal_entry));
            finish();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ChatModel chatModel;
        for (DataSnapshot snapShot : dataSnapshot.getChildren()){
            if (sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME, "").equals(snapShot.getKey())) {
                for (DataSnapshot postSnapshot : snapShot.getChildren()) {
                    Log.d("CHAT_LOGS", postSnapshot.getKey()); // 2017-03-17
                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {
                        chatModel = new ChatModel();
                        Log.d("CHAT_LOGS", postSnapshot2.getKey()); //-KfPCEXL6OaxXtCE6iVN
                        Log.d("CHAT_LOGS", postSnapshot2.getValue().toString()); // {message=My message, locTime=10:59:51.533, from=user2}
                        for (DataSnapshot messageSnapshot : postSnapshot2.getChildren()) {
                            switch (messageSnapshot.getKey()) {
                                case "from":
                                    chatModel.setFrom(messageSnapshot.getValue().toString());
                                    break;
                                case "locTime":
                                    chatModel.setLocTime(messageSnapshot.getValue().toString());
                                    break;
                                case "message":
                                    chatModel.setMessage(messageSnapshot.getValue().toString());
                                    break;
                            }
                        }
                        chatModelArrayList.add(chatModel);
                        chatAdapter.notifyDataSetChanged();
                    }

                }
            }

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }





}
