package com.example.location.locationtracker.service;

import android.os.Handler;

import com.example.location.locationtracker.LocationAPIService;
import com.example.location.locationtracker.component.DaggerSyncServiceComponent;
import com.example.location.locationtracker.database.FailedLocationSyncDbUseCase;
import com.example.location.locationtracker.model.CustomLocation;
import com.example.location.locationtracker.model.LocationAPIRequestBody;
import com.example.location.locationtracker.modules.BaseModule;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.location.locationtracker.modules.LocationAPIServiceModule.AUTHORIZATION;

/**
 * Created by Rishabh on 16/07/17.
 */

public class SyncLaterService extends JobService {

    @Inject
    public LocationAPIService locationAPIService;

    @Inject
    public CustomLocation customLocation;

    @Inject
    public LocationAPIRequestBody locationAPIRequestBody;

    @Inject
    public FailedLocationSyncDbUseCase failedLocationSyncDbUseCase;

    @Inject
    @Named(AUTHORIZATION)
    public String authorizationHeader;

    private JobParameters jobParameters;
    private List<CustomLocation> customLocationList;
    private long timestamp;
    private int counter = 0;

    private Observer<List<CustomLocation>> dataObserver = new Observer<List<CustomLocation>>() {
        @Override
        public void onSubscribe(Disposable d) {}

        @Override
        public void onNext(List<CustomLocation> customLocations) {
            customLocationList = customLocations;
            syncLocation();
        }

        @Override
        public void onError(Throwable e) {}

        @Override
        public void onComplete() {}
    };

    private Callback<Void> voidCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.errorBody() == null) {
                failedLocationSyncDbUseCase.deleteEntry(customLocationList.get(counter).getTimestamp());
                counter++;
                syncLocation();
            } else {
                jobFinished(jobParameters, true);
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            jobFinished(jobParameters, true);
        }
    };


    @Override
    public boolean onStartJob(JobParameters job) {

        DaggerSyncServiceComponent.builder().baseModule(new BaseModule(this)).build().inject(this);
        jobParameters = job;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                failedLocationSyncDbUseCase.getUnSyncedLocation()
                        .observeOn(Schedulers.single())
                        .subscribe(dataObserver);
            }
        };
        new Handler().postDelayed(runnable, 200);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    public void syncLocation() {
        if (customLocationList.size() > counter) {
            makeAPICall(customLocationList.get(counter));
        } else {
            jobFinished(jobParameters, false);
        }
    }
    private void makeAPICall(CustomLocation customLocation) {
        locationAPIRequestBody.setLocation(customLocation);
        Call<Void> voidCall = locationAPIService.sendLocation(authorizationHeader, locationAPIRequestBody);
        voidCall.enqueue(voidCallback);
    }
}
