package com.example.location.locationtracker.modules;

import android.content.Context;

import com.example.location.locationtracker.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rishabh on 15/07/17.
 */

@Module
public class BaseModule {
    private final Context context;

    public BaseModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @ApplicationContext
    public Context context() {
        return context;
    }

    @Provides
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

}
