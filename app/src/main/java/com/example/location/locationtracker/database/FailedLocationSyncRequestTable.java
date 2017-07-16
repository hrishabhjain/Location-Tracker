package com.example.location.locationtracker.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.inject.Inject;

/**
 * Created by Rishabh on 16/07/17.
 */
@DatabaseTable(tableName= "failed_location_sync_request_api")
public class FailedLocationSyncRequestTable {

    public static final String TIMESTAMP = "timestamp";

    @DatabaseField(id = true , unique = true)
    private long timestamp;

    @DatabaseField
    private String jsonString;

    @Inject
    public FailedLocationSyncRequestTable() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

}
