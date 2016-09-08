package com.example.user.testhireapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.user.testhireapplication.SqlPackage.SqlBaseHelper;
import com.example.user.testhireapplication.SqlPackage.SqlScheme;
import com.example.user.testhireapplication.SqlPackage.StationCursorWrapper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by v.borovkov on 05.09.2016.
 */
public class StationLab {
    private static StationLab sStationLab;
    private List<Station> mStations;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private StationLab(Context ctx) {
        mStations=new ArrayList<>();
        mContext = ctx.getApplicationContext();
        mDatabase = SqlBaseHelper.getInstance(mContext).getWritableDatabase();
    }

    public List<Station> returnList() {
        return mStations;
    }

    public static StationLab getInstance(Context ctx) {
        if (sStationLab == null) return new StationLab(ctx);
        else return sStationLab;
    }


    public void placeToDB(Station station) throws SQLException {
        long insertId = mDatabase.insertOrThrow(SqlScheme.DbScheme.StationsTable.TABLE_NAME, null, getContentValues(station));
    }

    public ContentValues getContentValues(Station station) {
        ContentValues values = new ContentValues();


        values.put(SqlScheme.DbScheme.StationsTable.Cols.CITY_ID, station.getCityId());
        values.put(SqlScheme.DbScheme.StationsTable.Cols.CITY_TITLE, station.getCityTitle());
        values.put(SqlScheme.DbScheme.StationsTable.Cols.COUNTRY_TITLE, station.getCountryTitle());
        values.put(SqlScheme.DbScheme.StationsTable.Cols.DISTRICT_TITLE, station.getDistrictTitle());
        values.put(SqlScheme.DbScheme.StationsTable.Cols.REGION_TITLE, station.getRegionTitle());
        values.put(SqlScheme.DbScheme.StationsTable.Cols.STATION_ID, station.getStationId());
        values.put(SqlScheme.DbScheme.StationsTable.Cols.STATION_TITLE, station.getStationTitle());
        values.put(SqlScheme.DbScheme.StationsTable.Cols.STATION_TYPE, station.getStationType());
        values.put(SqlScheme.DbScheme.StationsTable.Cols.STATION_TITLE_LOWERCASE, station.getStationTitle().toLowerCase());
        values.put(SqlScheme.DbScheme.StationsTable.Cols.COUNTRY_TITLE_LOWERCASE, station.getCountryTitle().toLowerCase());
        values.put(SqlScheme.DbScheme.StationsTable.Cols.CITY_TITLE_LOWERCASE, station.getCityTitle().toLowerCase());


        return values;

    }

    //преобразование данных из БД в модель
    private StationCursorWrapper queryStations(String whereClause, String[] whereArgs, String orderByCond) {
        Cursor cursor = mDatabase.query(SqlScheme.DbScheme.StationsTable.TABLE_NAME, null, whereClause, whereArgs, null, null, orderByCond);
        return new StationCursorWrapper(cursor);

    }

    public List<Station> getStations(String stationTypeWhereClause) {
        mStations = new ArrayList<>();
        StationCursorWrapper cursor = queryStations(stationTypeWhereClause, null, SqlScheme.DbScheme.StationsTable.Cols.COUNTRY_TITLE_LOWERCASE + "," + SqlScheme.DbScheme.StationsTable.Cols.CITY_TITLE_LOWERCASE);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mStations.add(cursor.getStation());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return mStations;
    }


}
