package com.example.location.locationtracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.location.locationtracker.ApplicationContext;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import static com.example.location.locationtracker.database.FailedLocationSyncRequestTable.TIMESTAMP;

/**
 * Created by Rishabh on 16/07/17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "location_tracker.sqlite";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    private Dao<FailedLocationSyncRequestTable, Long> failedLocationSyncRequestTableDao = null;

    @Inject
    public DatabaseHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,
                    FailedLocationSyncRequestTable.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    private Dao<FailedLocationSyncRequestTable, Long> getFailedLocationSyncRequestTableDao() {
        if (null == failedLocationSyncRequestTableDao) {
            try {
                this.failedLocationSyncRequestTableDao = getDao(FailedLocationSyncRequestTable.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return failedLocationSyncRequestTableDao;
    }

    public void storeLocation(FailedLocationSyncRequestTable failedLocationSyncRequestTable) throws SQLException {
        getFailedLocationSyncRequestTableDao().createOrUpdate(failedLocationSyncRequestTable);
        Log.e("DB", "Location saved " + failedLocationSyncRequestTable.getJsonString());
    }

    public List<FailedLocationSyncRequestTable> getUnSyncedLocation() throws SQLException {
        QueryBuilder<FailedLocationSyncRequestTable, Long> qb = getFailedLocationSyncRequestTableDao().queryBuilder();
        qb.orderBy(TIMESTAMP, true);
        PreparedQuery<FailedLocationSyncRequestTable> preparedQuery = qb.prepare();
        return getFailedLocationSyncRequestTableDao().query(preparedQuery);
    }

    public void deleteSyncedLocation(long timestamp) throws SQLException {
        DeleteBuilder<FailedLocationSyncRequestTable, Long> deleteBuilder = getFailedLocationSyncRequestTableDao().deleteBuilder();
        deleteBuilder.where().eq(TIMESTAMP, timestamp);
        PreparedDelete<FailedLocationSyncRequestTable> pq = deleteBuilder.prepare();
        getFailedLocationSyncRequestTableDao().delete(pq);
        Log.e("Rishabh", "Deleted: " + timestamp);
    }
}

