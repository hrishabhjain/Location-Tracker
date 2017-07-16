package com.example.location.locationtracker.component;

import com.example.location.locationtracker.service.LocationService;
import com.example.location.locationtracker.modules.ForegroundServiceModule;
import com.example.location.locationtracker.modules.LocationAPIServiceModule;
import com.example.location.locationtracker.modules.LocationTrackerModule;

import dagger.Component;

/**
 * Created by Rishabh on 15/07/17.
 */
@Component(modules = {LocationAPIServiceModule.class, LocationTrackerModule.class, ForegroundServiceModule.class})
public interface LocationServiceComponent {
    void inject(LocationService locationService);
}
