package com.example.meetingpushnoti

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel";
const val channelName = "com.example.meetingpushnoti";
class FirebaseMessagingService:FirebaseMessagingService() {
    // generate the notification
    // attach the notification created with the custom layout
    // show the notification

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null){
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    private fun getRemoteView(tittle: String,message: String):RemoteViews{
        val remoteViews = RemoteViews("com.example.meetingpushnoti", R.layout.notification)

        remoteViews.setTextViewText(R.id.title, tittle)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(R.id.app_logo, R.drawable.gfg)

        return remoteViews;
    }

    private fun generateNotification(tittle:String, message:String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        // channel id, channel name
        var builder:NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.gfg)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent);

        builder = builder.setContent(getRemoteView(tittle, message));

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

        println("chegou até aqui")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            println("tentou criar a mensagem");
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        println("passou")

        notificationManager.notify(0, builder.build())
    }
}