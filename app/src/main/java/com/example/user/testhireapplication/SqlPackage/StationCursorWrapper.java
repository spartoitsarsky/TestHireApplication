package com.example.user.testhireapplication.SqlPackage;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.user.testhireapplication.Station;

/**
 * Created by v.borovkov on 08.09.2016.
 * Для более удобной работы с курсором
 */
public class StationCursorWrapper extends CursorWrapper {
    public StationCursorWrapper(Cursor cursor) {
        super(cursor);

    }
    public Station getStation(){
        String countryTitle=getString(getColumnIndex(SqlScheme.DbScheme.StationsTable.Cols.COUNTRY_TITLE));
        String districtTitle=getString(getColumnIndex(SqlScheme.DbScheme.StationsTable.Cols.DISTRICT_TITLE));
        String cityId=getString(getColumnIndex(SqlScheme.DbScheme.StationsTable.Cols.CITY_ID));
        String cityTitle=getString(getColumnIndex(SqlScheme.DbScheme.StationsTable.Cols.CITY_TITLE));
        String regionTitle=getString(getColumnIndex(SqlScheme.DbScheme.StationsTable.Cols.REGION_TITLE));
        String stationId=getString(getColumnIndex(SqlScheme.DbScheme.StationsTable.Cols.STATION_ID));
        String stationTitle=getString(getColumnIndex(SqlScheme.DbScheme.StationsTable.Cols.STATION_TITLE));
        String stationType=getString(getColumnIndex(SqlScheme.DbScheme.StationsTable.Cols.STATION_TYPE));
        return new Station(countryTitle,districtTitle,cityId,cityTitle,regionTitle,stationId,stationTitle,stationType);

    }
}
