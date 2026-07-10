package com.example.androidexample.features.notifications.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.androidexample.R;
import com.example.androidexample.features.notifications.model.NotificationObject;
import com.example.androidexample.features.notifications.model.Status;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<NotificationObject>
{

    public NotificationAdapter(@NonNull Context context, List<NotificationObject> notifications)
    {
        super(context, 0, notifications);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationObject notification = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_notifications, parent, false);
        }

        TextView title    = convertView.findViewById(R.id.notification_title);
        TextView msg      = convertView.findViewById(R.id.notification_msg);
        TextView type     = convertView.findViewById(R.id.notification_type);
        TextView date     = convertView.findViewById(R.id.notification_date);
        TextView status   = convertView.findViewById(R.id.notification_status);
        TextView recipient = convertView.findViewById(R.id.notification_recipient);

        title.setText(notification.getTitle());
        msg.setText(notification.getMessage());
        type.setText(String.valueOf(notification.getId()));
        date.setText(notification.getDate());
        status.setText(notification.getStatus().name());
        recipient.setText(notification.getRecipient().name());

        return convertView;
    }

}
