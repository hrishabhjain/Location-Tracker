package com.example.location.locationtracker.model;

import javax.inject.Inject;

/**
 * Created by Rishabh on 15/07/17.
 */
public class LocationAPIRequestBody {
    private CustomLocation location;
    private BatteryStatus batteryStatus;

    @Inject
    public LocationAPIRequestBody(CustomLocation location, BatteryStatus batteryStatus) {
        this.location = location;
        this.batteryStatus = batteryStatus;
    }

    public CustomLocation getLocation() {
        return location;
    }

    public void setLocation(CustomLocation location) {
        this.location = location;
    }

    public BatteryStatus getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(BatteryStatus batteryStatus) {
        this.batteryStatus = batteryStatus;
    }
}
