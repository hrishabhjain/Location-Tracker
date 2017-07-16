package com.example.location.locationtracker.database;


import android.util.Log;

import com.example.location.locationtracker.model.CustomLocation;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Rishabh on 16/07/17.
 */

public class FailedLocationSyncDbUseCase {

    @Inject
    public FailedLocationSyncRequestTable failedLocationSyncRequestTable;

    private DatabaseHelper databaseHelper;
    private Gson gson;

    @Inject
    public FailedLocationSyncDbUseCase(DatabaseHelper databaseHelper, Gson gson) {
        this.databaseHelper = databaseHelper;
        this.gson = gson;
    }

    public void saveLocation(CustomLocation location, long timestamp) {
        failedLocationSyncRequestTable.setTimestamp(timestamp);
        failedLocationSyncRequestTable.setJsonString(gson.toJson(location));
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                databaseHelper.storeLocation(failedLocationSyncRequestTable);
            }
        }).subscribeOn(Schedulers.computation()).subscribe();
    }

    public Observable<List<CustomLocation>> getUnSyncedLocation() {
        return Observable.create(new ObservableOnSubscribe<List<CustomLocation>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<CustomLocation>> e) throws Exception {
                try {
                    List<FailedLocationSyncRequestTable> failedLocationSyncRequestTableList = databaseHelper.getUnSyncedLocation();
                    List<CustomLocation> customLocationList = new ArrayList<CustomLocation>();
                    for (FailedLocationSyncRequestTable failedLocationSyncRequestTable: failedLocationSyncRequestTableList) {
                        customLocationList.add(gson.fromJson(failedLocationSyncRequestTable.getJsonString(), CustomLocation.class));
                        Log.e("Rishabh", "Get:" + failedLocationSyncRequestTable.getTimestamp());
                    }
                    e.onNext(customLocationList);
                } catch (SQLException sqle) {
                    e.onError(sqle);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.computation());
    }

    public void deleteEntry(final long timestamp) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                databaseHelper.deleteSyncedLocation(timestamp);
            }
        }).subscribeOn(Schedulers.computation()).subscribe();
    }
}
