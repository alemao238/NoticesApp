package com.example.dione.noticesapp.modules.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.modules.models.AccountsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Donds on 3/14/2017.
 */

public class AdminsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AccountsModel> accountsModels;
    public AdminsAdapter(Context context, ArrayList<AccountsModel> accountsModels) {
        this.accountsModels = accountsModels;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.grid_item_admins, null);
        }
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_item_image);
        final TextView textView = (TextView)convertView.findViewById(R.id.grid_item_name);
        Picasso.with(context)
                .load(accountsModels.get(position).getPhotoUrl())
                .placeholder(R.drawable.progress_animation)
                .resize(50, 50)
                .centerCrop()
                .into(imageView);
        textView.setText(accountsModels.get(position).getDisplayName());

        return convertView;
    }
}
