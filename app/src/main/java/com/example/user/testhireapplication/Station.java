package com.example.user.testhireapplication;

import java.io.Serializable;


public class Station implements Serializable {
    String mCountryTitle, mDistrictTitle, mCityId, mCityTitle, mRegionTitle, mStationId, mStationTitle, mStationType, mDescriptionString;


    public Station(String countryTitle, String districtTitle, String cityId, String cityTitle, String regionTitle, String stationId, String stationTitle, String stationType) {
        mCountryTitle = countryTitle;
        mDistrictTitle = districtTitle;
        mCityId = cityId;
        mCityTitle = cityTitle;
        mRegionTitle = regionTitle;
        mStationId = stationId;
        mStationTitle = stationTitle;
        mStationType = stationType;
        mDescriptionString = mCountryTitle + convertStringForDescription(mCityTitle) + convertStringForDescription(mRegionTitle)
                + convertStringForDescription(mDistrictTitle); //можно сделать как угодно отображение детальной инфо по станции, здесь самое простое
    }

    public String getDescriptionString() {
        return mDescriptionString;
    }

    public String getCountryTitle() {
        return mCountryTitle;
    }

    public String getDistrictTitle() {
        return mDistrictTitle;
    }

    public String getCityId() {
        return mCityId;
    }

    public String getCityTitle() {
        return mCityTitle;
    }

    public String getRegionTitle() {
        return mRegionTitle;
    }

    public String getStationId() {
        return mStationId;
    }

    public String getStationTitle() {
        return mStationTitle;
    }

    public String getStationType() {
        return mStationType;
    }


    public String convertStringForDescription(String string) {
        return (( string.equals("") || string.equals(" ")) ? "" : "," + string);


    }

}
