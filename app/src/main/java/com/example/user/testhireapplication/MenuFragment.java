package com.example.user.testhireapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.user.testhireapplication.Utils.HttpUtilities;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.TreeMap;


public class MenuFragment extends Fragment {
    Button mScheduleButton, mAboutButton, mLoadSampleDataButton;
    MenuClickListener mListener;
    StationLab mStationLab;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mSpEditor;
    private static final String DATA_LOADED_FLAG = "dataLoaded";

    private static final String TAG = "MenuFragment";


    public interface MenuClickListener {
        void proceedToSchedule();

        void proceedToAbout();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStationLab = StationLab.getInstance(getActivity());
        /*Log.i(TAG, "LAB SIZE: " + mStationLab.getStations().size());*/
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSpEditor = mSharedPreferences.edit();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.menu_fragment, container, false);
        mScheduleButton = (Button) v.findViewById(R.id.menu_schedule_button);
        mScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.proceedToSchedule();
            }
        });
        mScheduleButton.setEnabled(false);
        mAboutButton = (Button) v.findViewById(R.id.menu_about_button);
        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.proceedToAbout();
            }
        });
        mLoadSampleDataButton = (Button) v.findViewById(R.id.menu_fill_data_button);
        mLoadSampleDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DataLoader(getActivity()).execute();

            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            mListener = (MenuClickListener) a;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    public static MenuFragment newInstance() {

        Bundle args = new Bundle();

        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*
    * Тяжелый процесс, состоящий из загрузки JSon из сети, его парсинга и вставки в БД, после успешного окончания разблокируется интерфейс
    * */
    class DataLoader extends AsyncTask<Void, Void, TreeMap<Integer, String>> {
        private ListActivity activity;
        private ProgressDialog dialog;

        public DataLoader(Context context) {
            this.activity = activity;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Данные загружаются и обрабатываются. Подождите.");
            this.dialog.show();
        }

        @Override
        protected TreeMap<Integer, String> doInBackground(Void... params) {
            TreeMap<Integer, String> errorData = new TreeMap<>();
            try {
                String url = getResources().getString(R.string.json_url);
                Log.i(TAG, "url : " + url);
                String result = new HttpUtilities().getUrlString(url);
                Log.i(TAG, "result : " + result);
                JSONObject jsonObject = new JSONObject(result);
                try {
                    parseItems(jsonObject);
                } catch (SQLException e) {
                    errorData.put(-3, "SQl exception!");
                }
                errorData.put(0, "Success!");

            } catch (IOException e) {

                e.printStackTrace();
                errorData.put(-1, "Invalid http beahavior");
            } catch (JSONException e) {
                errorData.put(-2, "Invalid json behavior");
                e.printStackTrace();
            }
            return errorData;
        }

        @Override
        protected void onPostExecute(TreeMap<Integer, String> map) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPostExecute(map);
            Log.i(TAG, "errorCode: " + map.firstEntry().getKey());
            int errorCode = map.firstEntry().getKey();
            String errorString = (map.firstEntry().getValue() != null ? map.firstEntry().getValue() : "unknown error!");
            switch (errorCode) {
                case 0:
                    mSpEditor.putBoolean(DATA_LOADED_FLAG, true);
                    mSpEditor.commit();
                    Toast.makeText(getActivity(), "Successfully completed!", Toast.LENGTH_SHORT).show();
                    updateUI();
                    break;
                default:
                    Toast.makeText(getActivity(), "Error " + errorString, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void parseItems(JSONObject jsonBody) throws JSONException, SQLException {
        JSONArray citiesFrom = jsonBody.getJSONArray("citiesFrom");
        Log.i(TAG, "cities_from count:" + citiesFrom.length());
        JSONArray citiesTo = jsonBody.getJSONArray("citiesTo");
        Log.i(TAG, "cities_to count:" + citiesTo.length());

         /*
        Заполняем массив объектами Station
        */

        fillArrayWithStations(citiesTo, mStationLab.returnList(), "to");
        fillArrayWithStations(citiesFrom, mStationLab.returnList(), "from");

        for (Station station : mStationLab.returnList()) {
            Log.i(TAG, station.getStationId() + " " + station.getCityTitle() + " " + station.getCountryTitle() + " " + station.getStationType());
            //вставка в бд
            mStationLab.placeToDB(station);

        }

    }

    public void fillArrayWithStations(JSONArray jsonArray, List array, String type) throws JSONException {

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONArray stationArray = jsonArray.getJSONObject(i).getJSONArray("stations");
            for (int i1 = 0; i1 < stationArray.length(); i1++) {

                String countryTitle = stationArray.getJSONObject(i1).getString("countryTitle");
                String districtTitle = stationArray.getJSONObject(i1).getString("districtTitle");
                String cityId = stationArray.getJSONObject(i1).getString("cityId");
                String cityTitle = stationArray.getJSONObject(i1).getString("cityTitle");
                String regionTitle = stationArray.getJSONObject(i1).getString("regionTitle");
                String stationId = stationArray.getJSONObject(i1).getString("stationId");
                String stationTitle = stationArray.getJSONObject(i1).getString("stationTitle");
                String stationType = type;
                Station station = new Station(countryTitle, districtTitle, cityId, cityTitle, regionTitle, stationId, stationTitle, type);
                array.add(station);


            }

        }

    }

    void updateUI() {

        if (mSharedPreferences.getBoolean(DATA_LOADED_FLAG, false)) {
            mScheduleButton.setEnabled(true);
            mAboutButton.setEnabled(true);
            mLoadSampleDataButton.setEnabled(false);
        } else {
            mScheduleButton.setEnabled(false);
            mAboutButton.setEnabled(false);
            mLoadSampleDataButton.setEnabled(true);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
