package com.example.user.testhireapplication.SqlPackage;


public class SqlScheme {
    public final class DbScheme{
        public final class StationsTable{
            public static final String TABLE_NAME="stations";
            public final  class Cols {
                public final static String COUNTRY_TITLE="countryTitle";
                public final static String DISTRICT_TITLE="districtTitle";
                public final static String CITY_ID="cityId";
                public final static String CITY_TITLE="cityTitle";
                public final static String REGION_TITLE="regionTitle";
                public final static String STATION_ID="stationId";
                public final static String STATION_TITLE="stationTitle";
                public final static String STATION_TYPE="stationType";
                /*
                Колонки для сортировок, сделал по старой памяти, раньше трудно было сортировать большие буквы криллицы
                 */
                public final static String STATION_TITLE_LOWERCASE="stationTitleLowercase";
                public final static String COUNTRY_TITLE_LOWERCASE="countryTitleLowercase";
                public final static String CITY_TITLE_LOWERCASE="cityTitleLowercase";

            }
        }
    }
}
