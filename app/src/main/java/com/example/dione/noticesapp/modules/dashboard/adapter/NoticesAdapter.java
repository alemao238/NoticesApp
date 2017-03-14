package com.example.dione.noticesapp.modules.dashboard.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dione.noticesapp.R;
import com.example.dione.noticesapp.modules.models.NoticesModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Donds on 3/13/2017.
 */

public class NoticesAdapter extends ArrayAdapter<NoticesModel> {
    private Context context;
    private Context activity;
    private ArrayList<NoticesModel> noticesModelArrayList;
    public NoticesAdapter(Context activity, @NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<NoticesModel> list) {
        super(context, resource, list);
        this.context = context;
        this.noticesModelArrayList = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return noticesModelArrayList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_notices, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.noticeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.dialog_notice_message);
                TextView listItemContent = (TextView) dialog.findViewById(R.id.listitem_content);
                TextView listItemUser = (TextView) dialog.findViewById(R.id.listitem_user);
                TextView listItemDate = (TextView) dialog.findViewById(R.id.listitem_date);
                dialog.setTitle(noticesModelArrayList.get(position).getTitle());
                listItemContent.setText(noticesModelArrayList.get(position).getMessage());
                listItemUser.setText(noticesModelArrayList.get(position).getUser());
                listItemDate.setText(noticesModelArrayList.get(position).getDate());
                dialog.show();
            }
        });
        holder.date.setText(noticesModelArrayList.get(position).getDate());
        holder.message.setText(noticesModelArrayList.get(position).getShortMessage());
        holder.title.setText(noticesModelArrayList.get(position).getTitle());
        holder.user.setText(noticesModelArrayList.get(position).getUser());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.textview_date)
        TextView date;
        @BindView(R.id.textview_message)
        TextView message;
        @BindView(R.id.textview_title)
        TextView title;
        @BindView(R.id.textview_user)
        TextView user;
        @BindView(R.id.notice_container)
        RelativeLayout noticeContainer;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
