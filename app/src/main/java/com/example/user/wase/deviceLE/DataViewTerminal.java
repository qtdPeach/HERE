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
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.user.wase.R;
import com.example.user.wase.utility.PeakDetector;
import com.example.user.wase.utility.SinusoidalDetector;
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

    public PeakDetector dumbel;
    public SinusoidalDetector hoop;

    private Vibrator vib;


    //UI components
    private Spinner selector;
    private ViewFlipper vf;
    private TextView srView;
    private TextView exercisePhase;
    private GraphView[] gv;
    private EditText rawData;

    LineGraphSeries<DataPoint>[] series;
    private Runnable invalidator;
    private final Handler mHandler = new Handler();

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
    public static final char INDEX_ACC = 'a';
    public static final char INDEX_GYRO = 'g';
    public static final char INDEX_MAG = 'm';
    public static final char INDEX_FORCE = 'f';

    public static final String END_BIT = "@";

    private final int numberOfMeasures = 10;
    private String remained;
    private char remainedType;
    private long startTime;
    private float[] values;

    float[] offsets;

    boolean isSetOffset;
    CountDownTimer cdt, srChecker;
    float[] roughLPF;
    float[][] LPFbuffer;
    final int LPFwindow = 20;
    int[] latestIdx;
    private final float radToDeg = (float)(180/ Math.PI);
    private int countGyro = 0;
    private int countAcc = 0;

    private int samplingCount;
    private float samplingRate;
    private float[][] longtermAverageBuffer;
    private float[] longtermAverage;
    private int[] longTermIndex;
    private final int longterm = 5*100; //approx. 5s

    private int dumbelCount = 0;
    private boolean isRisingPeak, isFallingPeak;
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

        dumbel = new PeakDetector(0);
        hoop = new SinusoidalDetector(27*25.4, 386*8 );
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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

        srView = (TextView)findViewById(R.id.sampling_rate);
        exercisePhase = (TextView)findViewById(R.id.state);

        vf = (ViewFlipper)findViewById(R.id.flipper);

        gv = new GraphView[numberOfMeasures];
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
            gv[i].getViewport().setMaxX(5); //second

            gv[i].getViewport().setYAxisBoundsManual(true);
            gv[i].getViewport().setMinY(-150);
            gv[i].getViewport().setMaxY(150);

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
        gv[6].getViewport().setMinY(-20);
        gv[6].getViewport().setMaxY(400);
        gv[7].getViewport().setMinY(-90);
        gv[7].getViewport().setMaxY(90);
        gv[8].getViewport().setMinY(-180);
        gv[8].getViewport().setMaxY(180);
        gv[9].getViewport().setMinY(-100);
        gv[9].getViewport().setMaxY(100);

        // Set up the custom title
        rawData = (EditText) findViewById(R.id.rawData);
        rawData.setMaxLines(20);

        samplingCount = 0;
        samplingRate = 0;
        longtermAverageBuffer = new float[numberOfMeasures][longterm];
        longtermAverage = new float[longterm];
        longTermIndex= new int[numberOfMeasures];
        for(int i = 0; i < numberOfMeasures; i++){
            longTermIndex[i] = 0;
            longtermAverage[i] = 0;
            for(int k = 0; k <longterm; k++){
                longtermAverageBuffer[i][k] = 0;
            }
        }


        roughLPF = new float[numberOfMeasures];
        LPFbuffer= new float[numberOfMeasures][LPFwindow];
        latestIdx = new int[numberOfMeasures];
        for(int i = 0; i < numberOfMeasures; i++){
            latestIdx[i] = 0;
            roughLPF[i] = 0;
            for(int k = 0; k < LPFwindow; k++){
                LPFbuffer[i][k] = 0;
            }
        }

        values = new float[numberOfMeasures];
        offsets = new float[numberOfMeasures];
        isSetOffset = false;
        cdt = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isSetOffset = false;
                for(int i = 0; i < 3; i++){
                    offsets[i] = offsets[i]/countAcc;
                }
                for(int i = 3; i < 6; i++){
                    offsets[i] = offsets[i]/countGyro;
                }
            }
        };

        srChecker = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                samplingRate = (float)samplingCount;
                srView.setText(String.format("%.3f", samplingRate));
                samplingCount = 0;

            }

            @Override
            public void onFinish() {
                isFallingPeak = false;
                isRisingPeak = false;
            }
        };
        srChecker.start();

        startTime = System.currentTimeMillis();
        invalidator = new Runnable() {
            @Override
            public void run() {

                double x = (double) (System.currentTimeMillis() - startTime)/1000;
                //series[vf.getDisplayedChild()].appendData(new DataPoint((double) (System.currentTimeMillis() - startTime) / 1000, (double) values[vf.getDisplayedChild()]), true, 1000);
                for(int i = 0 ; i < 7; i++){
                    series[i].appendData(new DataPoint(x, (double) values[i]), true, 300);
                }

            }
        };
        //mHandler.postDelayed(invalidator, 1500);
        isRisingPeak = false;
        isFallingPeak = false;
    }

    private void setOffset(){
        isSetOffset = true;
        countAcc = 0;
        countGyro = 0;
        for(int i = 0; i < 6; i++){
            offsets[i] = 0;
        }
        cdt.start();
    }


    private float LPF(float raw, int what){
        latestIdx[what] = (latestIdx[what]+1) % LPFwindow;
        roughLPF[what] -= LPFbuffer[what][latestIdx[what]];
        LPFbuffer[what][latestIdx[what]] = raw;
        roughLPF[what] += raw;
        return roughLPF[what]/LPFwindow;
    }
    private float longTermAvg(float raw, int what){
        longTermIndex[what] = (longTermIndex[what]+1) % longterm;
        longtermAverage[what] -= longtermAverageBuffer[what][longTermIndex[what]];
        longtermAverageBuffer[what][longTermIndex[what]] = raw;
        longtermAverage[what] += raw;
        return longtermAverage[what]/longterm;
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
        mHandler.removeCallbacks(invalidator);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        mHandler.removeCallbacks(invalidator);
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

        float ax,ay,az;
        int what = -1;
        if (raw != null) {

            rawData.append(raw);
            String[] data = raw.split(" ");
            try {
                for (int i = 0; i < data.length; i++) {
                    if (data[i].equals("a")) {
                        ax = (float)Integer.parseInt(data[++i])/10  - offsets[0];
                        values[0] =  LPF(ax,0) - longTermAvg(ax, 0);
                        ay = (float)Integer.parseInt(data[++i])/10 - offsets[1];
                        values[1] =  LPF(ay,1) - longTermAvg(ay, 1);
                        az = (float)Integer.parseInt(data[++i])/10 - offsets[2];
                        values[2] =  LPF(az,2) - longTermAvg(az, 2);
                        what = 0;
                    } else if (data[i].equals("g")) {
                        ax = (float)Integer.parseInt(data[++i]) - offsets[3];
                        values[3] =  LPF(ax, 3) - longTermAvg(ax, 3);
                        ay = (float)Integer.parseInt(data[++i])- offsets[4];
                        values[4] =  LPF(ay, 4) - longTermAvg(ay, 4);
                        az = (float)Integer.parseInt(data[++i])- offsets[5];
                        values[5] =  LPF(az, 5) - longTermAvg(az, 5);
                        if(values[5] > 20 ){
                            exercisePhase.setText(dumbelCount/2 + ":  Rising");
                            dumbel.setDetectingMode(1);
                        }
                        else if(values[5] < -20){
                            exercisePhase.setText(dumbelCount/2 + ":  Falling");
                            dumbel.setDetectingMode(-1);
                        }
                        else {
                            exercisePhase.setText(dumbelCount/2 + ":  Static");
                            dumbel.setDetectingMode(0);
                        }

                        switch (vf.getDisplayedChild()) {
                            case 2:
                            case 5:
                                switch (dumbel.peakDetection(new DataPoint(0, values[vf.getDisplayedChild()]))) {
                                    case 1:
                                        isRisingPeak = true;
                                        if (isFallingPeak) {
                                            dumbelCount++;
                                            vib.vibrate(100);
                                        }
                                        isFallingPeak = false;
                                        break;
                                    case -1:
                                        isFallingPeak = true;
                                        if (isRisingPeak) {
                                            dumbelCount++;
                                            vib.vibrate(100);
                                        }
                                        isRisingPeak = false;
                                        break;
                                }
                                break;
                            default:

                                if(hoop.periodMonitor(new DataPoint((double)(System.currentTimeMillis() - startTime), values[vf.getDisplayedChild()]))) {
                                    exercisePhase.setText("Count: "+hoop.getCount() + " - is Periodic");

                                }else {
                                    exercisePhase.setText("is not Periodic");
                                }
                                break;

                        }
                        samplingCount++;

                    }
                }
            }catch(NumberFormatException e){};
            values[6] = (float)Math.sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2] );
            invalidator.run();

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
            String[] data = raw[0].split(END_BIT);
            for(int i =0 ; i < data.length; i++){
                switch(data[i].charAt(0)){
                    case INDEX_ACC:
                        break;
                    case INDEX_GYRO:
                        break;
                    case INDEX_MAG:
                        break;
                    case INDEX_FORCE:
                        break;
                    default:
                        break;
                }
            }

            return remained;
        }


    }


}
