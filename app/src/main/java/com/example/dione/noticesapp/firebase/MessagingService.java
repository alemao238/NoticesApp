package com.example.dione.noticesapp.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.dione.noticesapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Donds on 3/10/2017.
 */

public class MessagingService extends FirebaseMessagingService {
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        MessagingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MessagingService.this;
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("RECEIVED", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            createNotification(remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("message"),
                        remoteMessage.getData().get("more_message"));
        }
        if (remoteMessage.getNotification() != null) {
            Log.d("RECEIVED", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Intent intent = new Intent();
        intent.setAction("receive_notices");
        intent.putExtra("message", remoteMessage.getData().get("message"));
        sendBroadcast(intent);
    }

    private void createNotification(String title, String content, String more_message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stacktips.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSubText(more_message);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(666, builder.build());
    }
}
