package com.example.dione.noticesapp.modules.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.manager.SharedPreferenceManager;
import com.example.dione.noticesapp.modules.models.ChatModel;
import com.example.dione.noticesapp.utilities.ApplicationConstants;

import java.util.ArrayList;

/**
 * Created by Donds on 3/17/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ChatModel> chatModelArrayList;
    private SharedPreferenceManager sharedPreferenceManager;
    public ChatAdapter(ArrayList<ChatModel> chatModelArrayList, Context context) {
        this.chatModelArrayList = chatModelArrayList;
        sharedPreferenceManager = new SharedPreferenceManager(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView time;
        private TextView name;

        public MyViewHolder(View view) {
            super(view);
            message = (TextView) view.findViewById(R.id.listitem_message);
            time = (TextView) view.findViewById(R.id.listitem_time);
            name = (TextView) view.findViewById(R.id.listitem_name);
        }
    }
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chat, parent, false);
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_chat1, parent, false);
        }


        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(ChatAdapter.MyViewHolder holder, int position) {
        holder.message.setText(chatModelArrayList.get(position).getMessage());
        holder.time.setText(chatModelArrayList.get(position).getLocTime().split("\\.")[0]);
        if (getItemViewType(position) == 0) {
            holder.name.setText(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME, ""));
        } else {
            holder.name.setText(chatModelArrayList.get(position).getFrom());
        }

    }

    @Override
    public int getItemCount() {
        return chatModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int returnType = 1;
        if (chatModelArrayList.get(position).getFrom().equals(sharedPreferenceManager.getStringPreference(ApplicationConstants.KEY_USERNAME, ""))) {
            returnType = 0;
        }
        return returnType;

    }
}
