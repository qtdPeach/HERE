package com.example.user.wase.deviceLE;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import com.example.user.wase.R;
import com.example.user.wase.data.SingleData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by PCPC on 2016-05-23.
 */
public class DataViewTerminal extends Activity {
    private final static String TAG = "Data Terminal";

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    //UI components
    private Spinner selector;
    private ViewFlipper vf;
    private GraphView[] gv;
    private EditText rawData;
    LineGraphSeries<DataPoint>[] series;


    //Bluetooth components
    private String mDeviceName;
    private String mDeviceAddress;

    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    public final static UUID HM_RX_TX =
            UUID.fromString(HERE_GattAttributes.HM_RX_TX);


    //data components
    private String remained;
    private char remainedType;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                appendData(intent.getStringExtra(mBluetoothLeService.EXTRA_DATA));
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view_terminal);
        linkComponents();

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);


        //getActionBar().setTitle(mDeviceName);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);

        }

    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();


        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, HERE_GattAttributes.lookup(uuid, "unknown"));

            // If the service exists for HM 10 Serial, say so.
            if(HERE_GattAttributes.lookup(uuid, "unknown") == "HM 10 Serial") {
            } else {
            }
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            // get characteristic when UUID matches RX/TX UUID
            characteristicTX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
            characteristicRX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);

            if(characteristicTX != null && mBluetoothLeService != null) {
                characteristicTX.setValue(new String("0,0,0").getBytes());
                mBluetoothLeService.writeCharacteristic(characteristicTX);
                mBluetoothLeService.setCharacteristicNotification(characteristicRX,true);
            }

        }

    }
    private void linkComponents(){

        selector = (Spinner)findViewById(R.id.spinner);
        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vf.setDisplayedChild(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vf = (ViewFlipper)findViewById(R.id.flipper);

        gv = new GraphView[10];
        gv[0] =  (GraphView) findViewById(R.id.graphX);
        gv[1] =  (GraphView) findViewById(R.id.graphY);
        gv[2] =  (GraphView) findViewById(R.id.graphZ);
        gv[3] =  (GraphView) findViewById(R.id.graphGX);
        gv[4] =  (GraphView) findViewById(R.id.graphGY);
        gv[5] =  (GraphView) findViewById(R.id.graphGZ);
        gv[6] =  (GraphView) findViewById(R.id.graphAzi);
        gv[7] =  (GraphView) findViewById(R.id.graphRoll);
        gv[8] =  (GraphView) findViewById(R.id.graphPch);
        gv[9] =  (GraphView) findViewById(R.id.graphPch);
        series = new LineGraphSeries[10];

        int[] lineColors = {Color.BLUE, Color.GREEN, Color.RED, Color.argb(255, 80, 80, 255), Color.argb(255, 80, 255, 80), Color.argb(255, 255, 80, 80), Color.argb(255, 160, 160, 255), Color.argb(255, 160, 255, 160), Color.argb(255, 255, 160, 160), Color.YELLOW};
        for(int i = 0; i < 9; i++) {
            series[i] = new LineGraphSeries<DataPoint>();
            series[i].setColor(lineColors[i]);

            gv[i].addSeries(series[i]);
            gv[i].getViewport().setScrollable(true);

            gv[i].getViewport().setXAxisBoundsManual(true);
            gv[i].getViewport().setMinX(0);
            gv[i].getViewport().setMaxX(10000);

            gv[i].getViewport().setYAxisBoundsManual(true);
            gv[i].getViewport().setMinY(-10);
            gv[i].getViewport().setMaxY(10);

            if(i < 3){
                gv[i].getViewport().setBackgroundColor(Color.WHITE);
                gv[i].getGridLabelRenderer().setGridColor(Color.GRAY);
                gv[i].getGridLabelRenderer().reloadStyles();
            }
            else if(i < 6){
                gv[i].getViewport().setBackgroundColor(Color.GRAY);
                gv[i].getGridLabelRenderer().setGridColor(Color.BLACK);
                gv[i].getGridLabelRenderer().reloadStyles();
            }
            else{
                gv[i].getViewport().setBackgroundColor(Color.BLACK);
                gv[i].getGridLabelRenderer().setGridColor(Color.DKGRAY);
                gv[i].getGridLabelRenderer().reloadStyles();
            }
        }
        gv[6].getViewport().setMinY(-180);
        gv[6].getViewport().setMaxY(180);
        gv[7].getViewport().setMinY(-90);
        gv[7].getViewport().setMaxY(90);
        gv[8].getViewport().setMinY(-180);
        gv[8].getViewport().setMaxY(180);
        gv[9].getViewport().setMinY(-100);
        gv[9].getViewport().setMaxY(100);

        // Set up the custom title
        rawData = (EditText) findViewById(R.id.rawData);
        rawData.setMaxLines(20);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);

            if(mConnected) {
                mBluetoothLeService.writeCharacteristic(characteristicTX);
                mBluetoothLeService.setCharacteristicNotification(characteristicRX,true);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }


    private void appendData(String raw) {

        if (raw != null) {

            rawData.append(raw);
            if(rawData.getLineCount() > rawData.getMaxLines()){
                rawData.setText("");
            }

        }
    }



    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    private class RawDataBinder extends AsyncTask<String, Character, String> {

        private char startBit;
        private boolean isCheckStartBit;

        @Override
        protected String doInBackground(String... raw){
            String[] data = raw[0].split(SingleData.END_BIT);
            for(int i =0 ; i < data.length; i++){
                switch(data[i].charAt(0)){
                    case SingleData.INDEX_ACC:
                        break;
                    case SingleData.INDEX_GYRO:
                        break;
                    case SingleData.INDEX_MAG:
                        break;
                    case SingleData.INDEX_FORCE:
                        break;
                    default:
                        break;
                }
            }

            return remained;
        }


    }


}
