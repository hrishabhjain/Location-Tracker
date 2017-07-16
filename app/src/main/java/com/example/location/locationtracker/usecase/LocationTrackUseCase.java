package com.example.location.locationtracker.usecase;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.location.locationtracker.ApplicationContext;
import com.example.location.locationtracker.LocationAPIService;
import com.example.location.locationtracker.database.FailedLocationSyncDbUseCase;
import com.example.location.locationtracker.model.ClientDetails;
import com.example.location.locationtracker.model.CustomLocation;
import com.example.location.locationtracker.model.LocationAPIRequestBody;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.location.locationtracker.modules.LocationAPIServiceModule.AUTHORIZATION;

/**
 * Created by Rishabh on 15/07/17.
 */

public class LocationTrackUseCase implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    @Inject
    @ApplicationContext
    public Context context;

    @Inject
    public CustomLocation customLocation;

    @Inject
    public LocationRequest locationRequest;

    @Inject
    public LocationAPIService locationAPIService;

    @Inject
    public LocationAPIRequestBody locationAPIRequestBody;

    @Inject
    public ClientDetails clientDetails;

    @Inject
    public GoogleApiClient.Builder googleApiClientBuilder;

    @Inject
    public FailedLocationSyncDbUseCase failedLocationSyncDbUseCase;

    @Inject
    @Named(AUTHORIZATION)
    public String authorizationHeader;

    @Inject
    public Job job;

    @Inject
    public FirebaseJobDispatcher dispatcher;

    public GoogleApiClient googleApiClient;

    private Location lastLocation;

    private long lastLocationTimestamp;

    private Callback<Void> voidCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.errorBody() != null) {
                failedLocationSyncDbUseCase.saveLocation(customLocation, lastLocationTimestamp);
                dispatcher.mustSchedule(job);
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            failedLocationSyncDbUseCase.saveLocation(customLocation, lastLocationTimestamp);
            dispatcher.mustSchedule(job);
        }
    };

    @Inject
    public LocationTrackUseCase() {}

    public void execute() {
        createLocationRequest();
        googleApiClient = googleApiClientBuilder.addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void createLocationRequest() {
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void makeAPICall(Location location, long timestamp) {
        lastLocation = location;
        lastLocationTimestamp = timestamp;

        customLocation.setLat(lastLocation.getLatitude());
        customLocation.setLng(lastLocation.getLongitude());
        customLocation.setTimestamp(timestamp);
        locationAPIRequestBody.setLocation(customLocation);

        Call<Void> voidCall = locationAPIService.sendLocation(authorizationHeader, locationAPIRequestBody);
        voidCall.enqueue(voidCallback);
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location == null) {
            return;
        }

        if (lastLocation == null) {
            lastLocation = location;
            makeAPICall(location, System.currentTimeMillis());
            return;
        }

        if (lastLocation.distanceTo(location) > 100) {
            makeAPICall(location, System.currentTimeMillis());
        } else if ((System.currentTimeMillis() - lastLocationTimestamp) > 120000) {
            makeAPICall(location, System.currentTimeMillis());
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}

