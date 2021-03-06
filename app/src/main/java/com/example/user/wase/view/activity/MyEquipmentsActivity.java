package com.example.user.wase.view.activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.wase.R;
import com.example.user.wase.model.Equipment;
import com.example.user.wase.model.MyHereAgent;
import com.example.user.wase.model.MyRecord;
import com.example.user.wase.model.MyRoutine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymbaek on 2016-04-18.
 */
public class MyEquipmentsActivity extends AppCompatActivity {

    public static final String TAG = "MyEquipmentsActivity";
    public static final String TAG_DB = "MyEquipmentsDBTag";

    public List<MyHereAgent> myHereAgents;
    private HERE_DeviceListAdapter equipListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private ListViewAdapter adapter;
    private ArrayList<BluetoothDevice> mLEdeviceList;
    private boolean mScanning;
    private Handler mHandler;


    private MyHereAgent selectedNewAgent;

    private ListView lvEquipList;

    private ArrayList<MyHereAgent> pairedEquipList;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    //Toolbar
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myequipments);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("My HERE Agents");
        toolbar.setNavigationIcon(R.mipmap.ic_menu_arrow_back);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        myHereAgents = new ArrayList<MyHereAgent>();
        selectedNewAgent = new MyHereAgent();

        actionBar.setTitle("My HERE Agents");
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) findViewById(R.id.setting_myeq_list_registered);
        adapter = new ListViewAdapter();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "You need BLE support device", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "You need BLUETOOTH support device", Toast.LENGTH_SHORT).show();
            finish();
        }

        equipListAdapter = new HERE_DeviceListAdapter();
        lvEquipList = (ListView) findViewById(R.id.setting_myeq_list_bluetooth);
        lvEquipList.setAdapter(equipListAdapter);

        scanLeDevice(true);

        lvEquipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "lvEquipList.setOnItemClickListener", Toast.LENGTH_SHORT).show();

                String majorId = parseMajorId(mLEdeviceList.get(position).getName());
                String minorId = parseMinorId(mLEdeviceList.get(position).getName());
                int eqType = getTypeByMinorId(minorId);

                selectedNewAgent.setMyeqMacId(mLEdeviceList.get(position).getAddress());
                selectedNewAgent.setMyeqType(eqType);
                selectedNewAgent.setMyeqBeaconMajorId(majorId);
                selectedNewAgent.setMyeqBeaconMinorId(minorId);

                Log.d("parseIDs", "mLEdeviceList.get(position).getName(): " + mLEdeviceList.get(position).getName());

                Log.d("parseIDs", "Major ID: " + majorId);
                Log.d("parseIDs", "Major ID: " + minorId);
                Log.d("parseIDs", "Agent type: " + eqType);

                showAddAgentDialog();

                scanLeDevice(false);
            }
        });

    }


    public void mOnClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class ListViewAdapter extends BaseAdapter{



        public ListViewAdapter() {
            super();
            if(MainActivity.hereDB.getAllMyHereAgents() !=null)
                myHereAgents = MainActivity.hereDB.getAllMyHereAgents();

            TextView textView = (TextView) findViewById(R.id.setting_myeq_tv_registered);
            if (myHereAgents.size() != 0) {
                textView.setVisibility(View.GONE);
            }
        }

        @Override
        public int getCount() {
            return myHereAgents.size();
        }

        @Override
        public Object getItem(int position) {
            return myHereAgents.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listitem_equipment_simple,parent,false);
            }
            ImageView eqTypeImage = (ImageView) convertView.findViewById(R.id.equiplist_img);
            TextView eqName = (TextView) convertView.findViewById(R.id.equiplist_name);
            TextView eqId = (TextView) convertView.findViewById(R.id.equiplist_id);
            TextView eqSensorType = (TextView) convertView.findViewById(R.id.equiplist_sensorid);

            switch (myHereAgents.get(pos).getMyeqType()) {
                case 1:
                    eqTypeImage.setImageResource(R.drawable.eq_01_dumbbell);
                    break;
                case 2:
                    eqTypeImage.setImageResource(R.drawable.eq_02_pushupbar);
                    break;
                case 3:
                    eqTypeImage.setImageResource(R.drawable.eq_03_jumprope);
                    break;
                case 4:
                    eqTypeImage.setImageResource(R.drawable.eq_04_hoolahoop);
                    break;
                case 5:
                    eqTypeImage.setImageResource(R.drawable.icon_bluetooth_device);
                default:
                    break;
            }

            eqName.setText(myHereAgents.get(pos).getMyeqName());
            eqId.setText(myHereAgents.get(pos).getMyeqMacId());
            eqSensorType.setText(myHereAgents.get(pos).getMyeqBeaconMajorId() + "-" + myHereAgents.get(pos).getMyeqBeaconMinorId());
            //eqSensorType.setText(registeredAgents.get(pos).getMyeqType());

            return convertView;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        scanLeDevice(false);
    }

    @Override
    public void onResume() {

        super.onResume();
//        if(registeredAgents!=null)
//            registeredAgents.clear();
//        registeredAgents = MainActivity.hereDB.getAllMyHereAgents();

        Log.d(TAG, "onResume");
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        scanLeDevice(true);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
        scanLeDevice(false);
        equipListAdapter.clear();
    }

    @Override
    public void onRestart(){
        super.onRestart();
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

        public HERE_DeviceListAdapter() {
            super();
            pairedEquipList = new ArrayList<MyHereAgent>();
            mLEdeviceList = new ArrayList<BluetoothDevice>();

        }

        public void addDevice(BluetoothDevice device) {
            if(!mLEdeviceList.contains(device)) {
                mLEdeviceList.add(device);
                if(!pairedEquipList.contains(device.getAddress())){
                    String deviceAddress = device.getAddress();
                    String deviceName = device.getName();
                    String deviceMajorId = parseMajorId(device.getName());
                    String deviceMinorId = parseMinorId(device.getName());
                    int deviceType = getTypeByMinorId(deviceMinorId);

                    pairedEquipList.add(new MyHereAgent(deviceAddress, deviceName, deviceType, deviceMajorId, deviceMinorId));
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

            final Context context = viewGroup.getContext();

            if (view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listitem_equipment_simple,viewGroup,false);
            }

            ImageView eqTypeImage = (ImageView)view.findViewById(R.id.equiplist_img);
            TextView eqName = (TextView)view.findViewById(R.id.equiplist_name);
            TextView eqId = (TextView)view.findViewById(R.id.equiplist_id);
            TextView eqSensorId = (TextView)view.findViewById(R.id.equiplist_sensorid);

            switch (pairedEquipList.get(i).getMyeqType()) {
                case MyHereAgent.TYPE_DUMBEL:
                    eqTypeImage.setImageResource(R.drawable.eq_01_dumbbell);
                    break;
                case MyHereAgent.TYPE_PUSH_UP:
                    eqTypeImage.setImageResource(R.drawable.eq_02_pushupbar);
                    break;
                case MyHereAgent.TYPE_JUMP_ROPE:
                    eqTypeImage.setImageResource(R.drawable.eq_03_jumprope);
                    break;
                case MyHereAgent.TYPE_HOOLA_HOOP:
                    eqTypeImage.setImageResource(R.drawable.eq_04_hoolahoop);
                    break;
                default:
                    eqTypeImage.setImageResource(R.drawable.icon_bluetooth_device);
                    break;
            }

            eqName.setText(pairedEquipList.get(i).getMyeqName());
            eqId.setText(pairedEquipList.get(i).getMyeqMacId());
            eqSensorId.setText(pairedEquipList.get(i).getMyeqBeaconMajorId() + "-" + pairedEquipList.get(i).getMyeqBeaconMinorId());
            //eqSensorId.setText(pairedEquipList.get(i).getEquipmentSensorID());

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            equipListAdapter.addDevice(device);
                            equipListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    private void showAddAgentDialog() {
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_agent, null);

        EditText et_addagent_macid = (EditText) dialogView.findViewById(R.id.dialog_agent_macid);
        final EditText et_addagent_name = (EditText) dialogView.findViewById(R.id.dialog_agent_name);
        EditText et_addagent_majorid = (EditText) dialogView.findViewById(R.id.dialog_agent_majorid);
        EditText et_addagent_minorid = (EditText) dialogView.findViewById(R.id.dialog_agent_minorid);

        et_addagent_macid.setText(selectedNewAgent.getMyeqMacId());
        et_addagent_majorid.setText(selectedNewAgent.getMyeqBeaconMajorId());
        et_addagent_minorid.setText(selectedNewAgent.getMyeqBeaconMinorId());

        AlertDialog.Builder builder = new AlertDialog.Builder(MyEquipmentsActivity.this);
        builder.setTitle("Add a new HERE agent");
        builder.setView(dialogView);
        builder.setPositiveButton("Add agent", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (selectedNewAgent.getMyeqBeaconMajorId() == "") {
                    Toast.makeText(getApplicationContext(), "This device is not compatible for HERE", Toast.LENGTH_SHORT).show();
                } else {
                    selectedNewAgent.setMyeqName(et_addagent_name.getText().toString());
                    MainActivity.hereDB.insertHereAgent(selectedNewAgent);
                    Toast.makeText(getApplicationContext(), "An agent is added into DB", Toast.LENGTH_SHORT).show();

                    myHereAgents.clear();
                    if (MainActivity.hereDB.getAllMyHereAgents() != null)
                        myHereAgents = MainActivity.hereDB.getAllMyHereAgents();
                    adapter.notifyDataSetChanged();

                    TextView textView = (TextView) findViewById(R.id.setting_myeq_tv_registered);
                    if (myHereAgents.size() != 0) {
                        textView.setVisibility(View.GONE);
                    }
                }



                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private String parseMajorId(String deviceName) {
        if (deviceName != null) {
            if (deviceName.contains("-")) {
                int separatorLoc = deviceName.indexOf("-");
                return deviceName.substring(0, separatorLoc);
            } else {
                return "";
            }

        } else {
            return "";
        }
    }

    private String parseMinorId(String deviceName) {
        if (deviceName != null) {
            if (deviceName.contains("-")) {
                int separatorLoc = deviceName.indexOf("-");
                return deviceName.substring(separatorLoc + 1, deviceName.length());
            } else {
                return "";
            }

        } else {
            return "";
        }
    }

    private int getTypeByMinorId(String minorId) {
        if (minorId != null) {
            if (minorId.contains("DB") || minorId.contains("Dumbbell") || minorId.contains("Dumbbel") || minorId.contains("Dumbel")) {
                return 1;
            }else if (minorId.contains("PU") || minorId.contains("Pushupbar")) {
                return 2;
            }else if (minorId.contains("JR") || minorId.contains("Jumprope")) {
                return 3;
            }else if (minorId.contains("HH") || minorId.contains("Hoolahoop")) {
                return 4;
            } else {
                return 0;
            }

        } else {
            return 0;
        }
    }
}
