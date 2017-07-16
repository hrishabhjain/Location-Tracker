package com.example.location.locationtracker.modules;

import android.content.Context;

import com.example.location.locationtracker.ApplicationContext;
import com.example.location.locationtracker.LocationAPIService;
import com.example.location.locationtracker.R;
import com.example.location.locationtracker.model.ClientDetails;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rishabh on 15/07/17.
 */

@Module(includes = NetworkModule.class)
public class LocationAPIServiceModule {

    public static final String AUTHORIZATION = "authorization";
    @Provides
    public LocationAPIService getProductHuntAPIService(Retrofit retrofit) {
        return retrofit.create(LocationAPIService.class);
    }

    @Provides
    public Retrofit getRetrofit(OkHttpClient okHttpClient, Gson gson, @ApplicationContext Context context) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(context.getString(R.string.API_URL))
                .build();

    }

    @Provides
    @Named(AUTHORIZATION)
    public String getAuthenticationHeader(ClientDetails clientDetails) {
        return Credentials.basic(clientDetails.getUsername(), clientDetails.getPassword());
    }
}
