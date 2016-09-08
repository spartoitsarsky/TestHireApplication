package com.example.user.testhireapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by v.borovkov on 07.09.2016.
 * <p/>
 * Отображение станций и даты отправления
 */
public class ScheduleFragment extends Fragment {
    private EditText mEditTextFrom, mEditTextTo;
    private Station mStationFrom, mStationTo;
    private Button mPickDateButton;
    private Date mDepartureDate;


    private final static String TAG = "ScheduleFragment";
    private final static String STATION_CHOOSER = "com.example.user.testhireapplication.request_station";
    private static final int REQUEST_STATION_FROM = 0;//код запроса станции отправки
    private static final int REQUEST_STATION_TO = 1;//код запроса станции назначения
    private static final int REQUEST_DATE = 2;//код запроса станции назначения
    private final static String DATE_DIALOG = "com.exmample.user.thehireapplication.date_dialog";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_fragment, container, false);
        mEditTextFrom = (EditText) v.findViewById(R.id.schedule_text_from);
        mEditTextFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "EditeTextFrom is pressed");
                FragmentManager fm = getFragmentManager();
                StationPickerDialogFragment dialog = StationPickerDialogFragment.newInstance("from");
                dialog.setTargetFragment(ScheduleFragment.this, REQUEST_STATION_FROM);
                dialog.show(fm, STATION_CHOOSER);

            }
        });
        mEditTextTo = (EditText) v.findViewById(R.id.schedule_text_to);
        mEditTextTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "EditeTextTo is pressed");
                FragmentManager fm = getFragmentManager();
                StationPickerDialogFragment dialog = StationPickerDialogFragment.newInstance("to");
                dialog.setTargetFragment(ScheduleFragment.this, REQUEST_STATION_TO);
                dialog.show(fm, STATION_CHOOSER);

            }
        });

        mPickDateButton = (Button) v.findViewById(R.id.schedule_departure_date_button);
        mPickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DialogFragment dialog = DatePickerDialogFragment.newInstance(new Date());
                dialog.setTargetFragment(ScheduleFragment.this, REQUEST_DATE);
                dialog.show(fm, DATE_DIALOG);
            }
        });
        return v;
    }

    public static ScheduleFragment newInstance() {

        Bundle args = new Bundle();

        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_STATION_FROM) {
            mStationFrom = (Station) data.getSerializableExtra(StationPickerDialogFragment.EXTRA_STATION);
            Log.i(TAG, "received stationFrom " + mStationFrom.getStationTitle());
            mEditTextFrom.setText(mStationFrom.getStationTitle());

        } else if (requestCode == REQUEST_STATION_TO) {
            mStationTo = (Station) data.getSerializableExtra(StationPickerDialogFragment.EXTRA_STATION);
            Log.i(TAG, "received stationTo " + mStationTo.getStationTitle());
            mEditTextTo.setText(mStationTo.getStationTitle());

        } else if (requestCode == REQUEST_DATE) {
            mDepartureDate = (Date) data.getSerializableExtra(DatePickerDialogFragment.EXTRA_DATE);
            Log.i(TAG, "Received " + mDepartureDate.toString());

            mPickDateButton.setText(convertDate(mDepartureDate));
        }
    }

    private String convertDate(Date date) {
        Locale locale = new Locale("ru", "RU");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM  y года  ", locale);
        return simpleDateFormat.format(date);

    }
}
