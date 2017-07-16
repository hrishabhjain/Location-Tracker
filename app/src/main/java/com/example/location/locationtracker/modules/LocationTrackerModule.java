package com.example.location.locationtracker.modules;

import android.content.Context;

import com.example.location.locationtracker.ApplicationContext;
import com.example.location.locationtracker.service.SyncLaterService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rishabh on 15/07/17.
 */

@Module(includes = BaseModule.class)
public class LocationTrackerModule {

    @Provides
    public GoogleApiClient.Builder googleApiClientBuilder(@ApplicationContext Context context) {
        return new GoogleApiClient.Builder(context);
    }

    @Provides
    public LocationRequest getLocationRequest() {
        return new LocationRequest();
    }

    @Provides
    public FirebaseJobDispatcher getFirebaseJobDispatcher(@ApplicationContext Context context) {
        return new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }

    @Provides
    public Job getJob(FirebaseJobDispatcher firebaseJobDispatcher) {
        return firebaseJobDispatcher.newJobBuilder()
                .setService(SyncLaterService.class)
                .setTag("sync-location-tag")
                .setRecurring(false)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
    }
}
