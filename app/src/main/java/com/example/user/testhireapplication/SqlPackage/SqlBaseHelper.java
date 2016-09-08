package com.example.user.testhireapplication.SqlPackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static com.example.user.testhireapplication.SqlPackage.SqlScheme.*;


/**
 * Created by user on 04.09.2016.
 * Класс для работы с БД
 */
public class SqlBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "tutu_database.db";

//SQL запрос для создания таблицы
    private static final String CREATE_TABLE_STATIONS = "CREATE TABLE " + SqlScheme.DbScheme.StationsTable.TABLE_NAME + " ( " + " _id integer primary key autoincrement , "
            + SqlScheme.DbScheme.StationsTable.Cols.DISTRICT_TITLE + " , " + SqlScheme.DbScheme.StationsTable.Cols.COUNTRY_TITLE + " , " + SqlScheme.DbScheme.StationsTable.Cols.CITY_ID + " , " + SqlScheme.DbScheme.StationsTable.Cols.CITY_TITLE
            + " , " + SqlScheme.DbScheme.StationsTable.Cols.REGION_TITLE + " , " + SqlScheme.DbScheme.StationsTable.Cols.STATION_ID + " , " + SqlScheme.DbScheme.StationsTable.Cols.STATION_TITLE +
            " , " + SqlScheme.DbScheme.StationsTable.Cols.STATION_TYPE + " , " + SqlScheme.DbScheme.StationsTable.Cols.STATION_TITLE_LOWERCASE + " , " + SqlScheme.DbScheme.StationsTable.Cols.CITY_TITLE_LOWERCASE + " ," +
            SqlScheme.DbScheme.StationsTable.Cols.COUNTRY_TITLE_LOWERCASE + " )";
    private static SqlBaseHelper sInstance;
//синглтон
    public static synchronized SqlBaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SqlBaseHelper(context);
        }
        return sInstance;

    }

    private SqlBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_STATIONS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}