package com.example.location.locationtracker.modules;

import android.content.Context;

import com.example.location.locationtracker.ApplicationContext;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Rishabh on 15/07/17.
 */

@Module(includes = BaseModule.class)
public class NetworkModule {

    @Provides
    public Cache cache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1000 * 1000);
    }

    @Provides
    public File cacheFile(@ApplicationContext Context context) {
        return new File(context.getCacheDir(), "okhttp_cache");
    }

    @Provides
    public OkHttpClient okHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }
}


