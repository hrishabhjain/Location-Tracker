package com.example.location.locationtracker.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.example.location.locationtracker.modules.ForegroundServiceModule;
import com.example.location.locationtracker.usecase.LocationTrackUseCase;
import com.example.location.locationtracker.component.DaggerLocationServiceComponent;
import com.example.location.locationtracker.modules.BaseModule;

import javax.inject.Inject;

/**
 * Created by Rishabh on 15/07/17.
 */

public class LocationService extends Service {

    public static final String LOCATION_SERVICE  = "com.example.location.locationtracker.service.LocationService";
    private int stickyNotificationId = 829;

    @Inject
    public Intent notificationIntent;

    @Inject
    public PendingIntent pendingIntent;

    @Inject
    public Notification stickyNotification;

    @Inject
    public LocationTrackUseCase locationTrackUseCase;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerLocationServiceComponent.builder()
                .baseModule(new BaseModule(this))
                .foregroundServiceModule(new ForegroundServiceModule(this))
                .build().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stickyNotification.priority = Notification.PRIORITY_MAX;
        }

        startForeground(stickyNotificationId, stickyNotification);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                locationTrackUseCase.execute();
            }
        };
        new Handler().postDelayed(runnable, 200);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        locationTrackUseCase.stopLocationUpdates();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
