package com.example.jmfs1.ebec;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.jmfs1.ebec.core.Alert;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by root on 3/5/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().toString());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getTitle());
        Log.d(TAG, remoteMessage.getData().size() + "");



        SharedPreferences sp = getSharedPreferences("LOGIN_PREFS", MODE_PRIVATE);
        String teamName = sp.getString("TEAM", "Some Error Ocurred");
        //msg dum participante e sou organizer
        //if((remoteMessage.getFrom()=="/topics/topic-group" && teamName=="topic-group") || (remoteMessage.getFrom()=="/topics/core-team" && teamName=="core-team") || (remoteMessage.getFrom()!="/topics/core-team" && remoteMessage.getFrom()!="/topics/topic-group")){
        if((remoteMessage.getFrom().equals("/topics/core-team") && teamName.equals("core-team")) || (remoteMessage.getFrom().equals("/topics/topic-group") && teamName.equals("topic-group")) || (!remoteMessage.getFrom().equals("/topics/core-team") && !remoteMessage.getFrom().equals("/topics/core-team"))){

            // Notification sound
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // Get data
            String message = remoteMessage.getNotification().getBody();

            // Save data
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy   HH:mm:ss");
            Alert alert = new Alert(message, df.format(new Date()));
//        alert.save();
            // Notification builder
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ebec)
                    .setContentTitle("EBEC Challenge Aveiro 2018")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(new Random().nextInt(Integer.MAX_VALUE) /* ID of notification */, notificationBuilder.build());
        }
    }

}
