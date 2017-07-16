package com.example.location.locationtracker.modules;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.example.location.locationtracker.MainActivity;
import com.example.location.locationtracker.R;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rishabh on 16/07/17.
 */

@Module
public class ForegroundServiceModule {

    private Context context;
    public ForegroundServiceModule(Context context) {
        this.context = context;
    }

    @Provides
    public Intent getIntent() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Provides
    public PendingIntent getPendingIntent(Intent intent) {
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Provides
    public Notification getNotification(PendingIntent pendingIntent) {
        return new NotificationCompat.Builder(context)
                .setContentTitle("Location Service")
                .setTicker("Your location is being tracked !!")
                .setContentText("Your location is being tracked !!")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .build();
    }
}
