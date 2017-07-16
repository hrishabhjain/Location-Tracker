package com.example.location.locationtracker.model;

import javax.inject.Inject;

/**
 * Created by Rishabh on 15/07/17.
 */

public class BatteryStatus {

    private int charge;
    private int expectedLife;
    private String chargingStatus;
    private Long timestamp;

    @Inject
    public BatteryStatus() {
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getExpectedLife() {
        return expectedLife;
    }

    public void setExpectedLife(int expectedLife) {
        this.expectedLife = expectedLife;
    }

    public String getChargingStatus() {
        return chargingStatus;
    }

    public void setChargingStatus(String chargingStatus) {
        this.chargingStatus = chargingStatus;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
