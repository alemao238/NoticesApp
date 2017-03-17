package com.example.dione.noticesapp.modules.dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.modules.chats.ChatActivity;
import com.example.dione.noticesapp.modules.models.AccountsModel;
import com.example.dione.noticesapp.modules.models.ChatModel;
import com.example.dione.noticesapp.utilities.ApplicationConstants;
import com.example.dione.noticesapp.utilities.Helpers;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;

/**
 * Created by Donds on 3/14/2017.
 */

public class AdminsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AccountsModel> accountsModels;
    private DatabaseReference chatReference;
    private SharedPreferenceManager sharedPreferenceManager;
    private Helpers helpers;
    public AdminsAdapter(Context context, ArrayList<AccountsModel> accountsModels , DatabaseReference chatsReference) {
        this.accountsModels = accountsModels;
        this.context = context;
        this.chatReference = chatsReference;
        this.helpers = new Helpers(context);
        this.sharedPreferenceManager = new SharedPreferenceManager(context);
    }
    @Override
    public int getCount() {
        return accountsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return accountsModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.grid_item_admins, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_item_image);
        final TextView textView = (TextView)convertView.findViewById(R.id.grid_item_name);
        Picasso.with(context)
                .load(accountsModels.get(position).getPhotoUrl())
                .placeholder(R.drawable.progress_animation)
                .error(R.mipmap.ic_person_outline_black_24dp)
                .resize(50, 50)
                .centerCrop()
                .into(imageView);
        textView.setText(accountsModels.get(position).getDisplayName());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { 
                if (!accountsModels.get(position).getUsername().equals(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME, ""))) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(ApplicationConstants.KEY_UID, accountsModels.get(position).getUid());
                    intent.putExtra(ApplicationConstants.KEY_CHATMATE, accountsModels.get(position).getDisplayName());
                    intent.putExtra(ApplicationConstants.KEY_USERNAME, accountsModels.get(position).getUsername());
                    context.startActivity(intent);
                } else {
                    helpers.showToast(context.getString(R.string.info_same_user));
                }

            }
        });
        return convertView;
    }
}
