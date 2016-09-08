package com.example.user.testhireapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.testhireapplication.SqlPackage.SqlScheme;

import java.util.List;




public class StationPickerDialogFragment extends DialogFragment {
    EditText mEditTextSearch;
    RecyclerView mRecyclerView;
    List<Station> mStations;
    private TextView mNoResultsTextView;
    private String mStationsType; //здесь будем инфо о том, какие станции рассматриваются (отправления или прибытия to/from)

    public final static String EXTRA_STATION = "com.example.user.thehireapplication.extra_station";
    private final static String ARG_STATION_TYPE = "stationTypeArg";
    private final static String TAG = "StationPickerDialogFragment";
    private static String mFilterStationsAccToType; //where clause согласно типу станции (to/from)
    DialogAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        setRetainInstance(true);
        mStationsType = bundle.getString(ARG_STATION_TYPE);
        Log.i(TAG, "STATIONS TYPE FOR dialog: " + mStationsType);
        mFilterStationsAccToType = SqlScheme.DbScheme.StationsTable.Cols.STATION_TYPE + " ='" + mStationsType + "'";
        mStations = StationLab.getInstance(getActivity()).getStations(mFilterStationsAccToType);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_station_choose, null);
        builder.setView(view);
        mNoResultsTextView = (TextView) view.findViewById(R.id.station_choose_empty_result_set_text_view);
        mEditTextSearch = (EditText) view.findViewById(R.id.station_choose_search);
        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "beforeTextChanged ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "onTextChanged ");
                mAdapter.mStations = StationLab.getInstance(getActivity()).getStations(SqlScheme.DbScheme.StationsTable.Cols.STATION_TITLE_LOWERCASE + " like '%"
                        + String.valueOf(charSequence).toLowerCase() + "%' and " + mFilterStationsAccToType);
                mAdapter.notifyDataSetChanged();
                if (mAdapter.mStations.size() == 0) {
                    Log.i(TAG, "SIZE ==0! hiding Recylcer");
                    mRecyclerView.setVisibility(View.GONE);
                    mNoResultsTextView.setVisibility(View.VISIBLE);

                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mNoResultsTextView.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "afterTextChanged ");

            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.station_choose_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return builder.create();
    }

    public static StationPickerDialogFragment newInstance(String stationFilter) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_STATION_TYPE, stationFilter);
        StationPickerDialogFragment fragment = new StationPickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private class DialogAdapter extends RecyclerView.Adapter<StationViewHolder> {
        List<Station> mStations;

        public DialogAdapter(List<Station> stations) {
            this.mStations = stations;
        }

        @Override
        public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.dialog_station_picker_fragment_list_row, parent, false);
            return new StationViewHolder(v);
        }

        @Override
        public void onBindViewHolder(StationViewHolder holder, int position) {
            holder.bindStationToViewHolder(mStations.get(position));

        }

        @Override
        public int getItemCount() {
            return mStations.size();
        }
    }

    private class StationViewHolder extends RecyclerView.ViewHolder {
        TextView mStationTitletextView;
        TextView mStationDescriptionTextView;
        Station mStation;

        public StationViewHolder(View itemView) {
            super(itemView);
            mStationTitletextView = (TextView) itemView.findViewById(R.id.station_picker_dialog_fragment_title);

            mStationDescriptionTextView = (TextView) itemView.findViewById(R.id.station_picker_dialog_fragment_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "Row was pressed!");
                    sendResult(Activity.RESULT_OK, mStation);
                }
            });
        }

        private void bindStationToViewHolder(Station station) {
            mStationTitletextView.setText(station.getStationTitle());
            mStationDescriptionTextView.setText(station.getDescriptionString());
            this.mStation = station;
        }
    }

    private void updateUI() {


        mAdapter = new DialogAdapter(mStations);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void sendResult(int resultCode, Station station) {
        if (getTargetFragment() == null) {

        } else {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_STATION, station);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
            this.dismiss();
        }
    }


}
