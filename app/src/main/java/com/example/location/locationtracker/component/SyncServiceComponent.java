package com.example.location.locationtracker.component;

import com.example.location.locationtracker.modules.LocationAPIServiceModule;
import com.example.location.locationtracker.service.SyncLaterService;

import dagger.Component;

/**
 * Created by Rishabh on 16/07/17.
 */


@Component(modules = LocationAPIServiceModule.class)
public interface SyncServiceComponent {
    void inject(SyncLaterService syncLaterService);
}
