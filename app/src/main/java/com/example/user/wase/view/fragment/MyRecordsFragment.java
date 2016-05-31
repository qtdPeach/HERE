package com.example.user.wase.view.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.wase.R;
import com.example.user.wase.device.EquipmentRecordTerminal;
import com.example.user.wase.deviceLE.DataViewTerminal;
import com.example.user.wase.model.Equipment;
import com.example.user.wase.model.EquipmentRecord;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ymbae on 2016-04-18.
 */
public class MyRecordsFragment extends Fragment{
    private HERE_DeviceListAdapter equipListAdapter;
    private ListView lvEquipList;
    private ArrayList<Equipment> equipmentList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        List<Double> workoutData =  new ArrayList<Double>();

        //here! add list values
        workoutData.add(300.0);workoutData.add(500.0);workoutData.add(100.0);workoutData.add(300.0);workoutData.add(800.0);workoutData.add(400.0);workoutData.add(700.0);

        View currentView = inflater.inflate(R.layout.fragment_myrecords, container, false);
        GraphView gv = (GraphView) currentView.findViewById(R.id.week_bar_graph);
        //GraphView gv = (GraphView) getView().findViewById(R.id.week_bar_graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(1,workoutData.get(0)),
                new DataPoint(2,workoutData.get(1)),
                new DataPoint(3,workoutData.get(2)),
                new DataPoint(4,workoutData.get(3)),
                new DataPoint(5,workoutData.get(4)),
                new DataPoint(6,workoutData.get(5)),
                new DataPoint(7,workoutData.get(6))});

        gv.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX) + "kcal";
                }
            }
        });

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(gv);
        staticLabelsFormatter.setHorizontalLabels(new String[]{getDay(calendar.get(Calendar.DAY_OF_WEEK) - 6), getDay(calendar.get(Calendar.DAY_OF_WEEK) - 5), getDay(calendar.get(Calendar.DAY_OF_WEEK) - 4), getDay(calendar.get(Calendar.DAY_OF_WEEK) - 3), getDay(calendar.get(Calendar.DAY_OF_WEEK) - 2), getDay(calendar.get(Calendar.DAY_OF_WEEK) - 1), getDay(calendar.get(Calendar.DAY_OF_WEEK))});
        gv.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLUE);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(135, 206, 235);
            }
        });
        gv.addSeries(series);

        equipListAdapter = new HERE_DeviceListAdapter();
        lvEquipList = (ListView) currentView.findViewById(R.id.equipment_record_list);
        lvEquipList.setAdapter(equipListAdapter);
        //equipment added
        equipListAdapter.addDevice(new Equipment("EQ01", "DUMBBELL", "Sensor-Q03-87A", "2016-04-18", 2));
        equipListAdapter.addDevice(new Equipment("EQ02", "HOOLA-HOOP", "Accelerometer-X-3", "2016-04-18", 1));
        equipListAdapter.addDevice(new Equipment("EQ03", "PLANK", "FORCE-ss-2033", "2016-04-15", 1));
        equipListAdapter.addDevice(new Equipment("EQ04", "JUMP-ROPE", "ZEROZERO", "2016-04-16", 0));

        equipListAdapter.notifyDataSetChanged();

        lvEquipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Equipment device = equipmentList.get(position);
                if (device == null) return;
                final Intent intent = new Intent(getActivity(), EquipmentRecordTerminal.class);

                //deliver data --> should be modified
                intent.putExtra(EquipmentRecordTerminal.EXTRAS_DEVICE_NAME, device.getEquipmentName());
                intent.putExtra(EquipmentRecordTerminal.EXTRAS_DEVICE_RECORD, device.getEquipmentID());

                startActivity(intent);
            }
        });



        return currentView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    String getDay (int date){
        int modulo = date%7;
        if (modulo < 0) modulo+=7;
        switch(modulo){
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tue";
            case 4:
                return "Wed";
            case 5:
                return "Thu";
            case 6:
                return "Fri";
            case 0:
                return "Sat";
        }
        return "unknown";
    }

    private class HERE_DeviceListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;

        public HERE_DeviceListAdapter() {
            super();
            equipmentList = new ArrayList<Equipment>();
            mInflator = getActivity().getLayoutInflater();
        }

        public void addDevice(Equipment device) {
            equipmentList.add(device);
        }

        @Override
        public int getCount() {
            return equipmentList.size();
        }

        @Override
        public Object getItem(int position) {
            return equipmentList.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            // General ListView optimization code.
            if (view == null) {
                int res = 0;
                res = R.layout.listitem_equipment;
                view = mInflator.inflate(res, viewGroup, false);

            }

            ImageView eqTypeImage = (ImageView)view.findViewById(R.id.equiplist_img);
            TextView eqName = (TextView)view.findViewById(R.id.equiplist_name);
            TextView eqId = (TextView)view.findViewById(R.id.equiplist_id);
            TextView eqSensorId = (TextView)view.findViewById(R.id.equiplist_sensorid);

            switch (equipmentList.get(i).getEquipmentType()) {
                case 0:
                    eqTypeImage.setImageResource(R.mipmap.ic_setting_update_alarm);
                    break;
                case 1:
                    eqTypeImage.setImageResource(R.mipmap.ic_setting_best_interest);
                    break;
                case 2:
                    eqTypeImage.setImageResource(R.mipmap.ic_setting_user_information);
                    break;
                case 3:
                    break;
                default:
                    break;
            }

            eqName.setText(equipmentList.get(i).getEquipmentName());
            eqId.setText(equipmentList.get(i).getEquipmentID());
            eqSensorId.setText(equipmentList.get(i).getEquipmentSensorID());

            return view;
        }
    }
}
