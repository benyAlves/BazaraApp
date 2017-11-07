package org.bazara.saudigitus.bazaraapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.bazara.saudigitus.bazaraapp.Main2Activity;
import org.bazara.saudigitus.bazaraapp.PedidosActivity;
import org.bazara.saudigitus.bazaraapp.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by dalves on 8/15/17.
 */

public class AppFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

        private static final String TAG = "MessagingService";

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {

            Log.d(TAG, "Notificación from: " + remoteMessage.getFrom());

                if (remoteMessage.getNotification() != null) {
                        Log.d(TAG, "Notificación: " + remoteMessage.getNotification().getBody());

                        mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                }
        }

        private void showNotification(String message) {

                Intent i = new Intent(this,Main2Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setContentTitle("FCM Test")
                        .setContentText(message)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentIntent(pendingIntent);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                manager.notify(0,builder.build());
        }


        private void mostrarNotificacion(String title, String body) {

                Intent intent = new Intent(this, PedidosActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());

        }



        }
