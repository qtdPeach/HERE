/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.user.wase.view.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.wase.R;
import com.example.user.wase.deviceLE.DataViewTerminal;
import com.example.user.wase.model.Equipment;

import java.util.ArrayList;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class EquipmentScanner extends Fragment {
    private HERE_DeviceListAdapter equipListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothDevice> mLEdeviceList;
    private boolean mScanning;
    private Handler mHandler;

    private ListView lvEquipList;

    private ArrayList<Equipment> pairedEquipList;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();




        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), "You need BLE support device", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "You need BLUETOOTH support device", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        equipListAdapter = new HERE_DeviceListAdapter();
        View view = inflater.inflate(R.layout.fragment_devicelist, container, false);
        lvEquipList = (ListView) view.findViewById(R.id.fragment1_equiplist);
        lvEquipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                scanLeDevice(false);

                final Equipment device = pairedEquipList.get(position);
                if (device == null) return;
                final Intent intent = new Intent(getActivity(), DataViewTerminal.class);
                intent.putExtra(DataViewTerminal.EXTRAS_DEVICE_NAME, device.getEquipmentName());
                intent.putExtra(DataViewTerminal.EXTRAS_DEVICE_ADDRESS, device.getEquipmentID());
                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
                startActivity(intent);
            }
        });
        lvEquipList.setAdapter(equipListAdapter);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        scanLeDevice(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            getActivity().finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
        equipListAdapter.clear();
    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Adapter for holding devices found through scanning.
    private class HERE_DeviceListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;

        public HERE_DeviceListAdapter() {
            super();
            pairedEquipList = new ArrayList<Equipment>();
            mLEdeviceList = new ArrayList<BluetoothDevice>();
            mInflator = getActivity().getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLEdeviceList.contains(device)) {
                mLEdeviceList.add(device);
                if(!pairedEquipList.contains(device.getAddress())){
                    pairedEquipList.add(new Equipment(device.getAddress(), device.getName(), "Sensor-Q03-87A", "2016-04-18", 2));
                }
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLEdeviceList.get(position);
        }

        public void clear() {
            mLEdeviceList.clear();
        }

        @Override
        public int getCount() {
            return mLEdeviceList.size();
        }

        @Override
        public Object getItem(int i) {
            return mLEdeviceList.get(i);
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

            switch (pairedEquipList.get(i).getEquipmentType()) {
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

            eqName.setText(pairedEquipList.get(i).getEquipmentName());
            eqId.setText(pairedEquipList.get(i).getEquipmentID());
            eqSensorId.setText(pairedEquipList.get(i).getEquipmentSensorID());

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    equipListAdapter.addDevice(device);
                    equipListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

}