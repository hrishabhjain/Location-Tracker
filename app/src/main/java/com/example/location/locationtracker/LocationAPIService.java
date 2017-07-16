package com.example.location.locationtracker;

import com.example.location.locationtracker.model.LocationAPIRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Rishabh on 15/07/17.
 */

public interface LocationAPIService {

    @POST("client/test/user/candidate/location")
    @Headers({
            "Accept: application/json" ,
            "Content-Type: application/json",

    })
    Call<Void> sendLocation(@Header("Authorization") String auth, @Body LocationAPIRequestBody locationAPIRequestBody);
}
