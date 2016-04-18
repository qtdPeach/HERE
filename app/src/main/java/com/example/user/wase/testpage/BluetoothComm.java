package com.example.user.wase.testpage;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.wase.R;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by user on 2016-04-18.
 */
public class BluetoothComm extends AppCompatActivity {

    //Message from CommService
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_DEVICE_NAME = 3;
    public static final int MESSAGE_TOAST = 4;
    public static final int MESSAGE_WRITE = 5;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter = null;
    private CommService commService = null;

    private TextView state;
    private ListView mConversationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*-- Basic setting for navigating view start--*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_bluetooth_comm);

        //state = (TextView)findViewById(R.id.state);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Button start = (Button)findViewById(R.id.start);
        start.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent discoveringIntent = new Intent(BluetoothComm.this, DiscoverDevice.class);
                startActivityForResult(discoveringIntent, REQUEST_CONNECT_DEVICE);
            }
        });

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (commService == null) try {
                setupService();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        //if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (commService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (commService.getState() == CommService.STATE_NONE) {
                // Start the Bluetooth chat services
                commService.start();
            }
        }
    }

    private void setupService() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.textform_message);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);
        commService = new CommService(this, mHandler);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case CommService.STATE_CONNECTED:
                            findViewById(R.id.in).setVisibility(View.VISIBLE);
                            //state.setText(R.string.title_connected_to);
                            //state.append(mConnectedDeviceName);
                            mConversationArrayAdapter.clear();
                            mConversationArrayAdapter.add(mConnectedDeviceName + " is connected" );
                            //commService.write();
                            //this.sendMessage("connected!");
                            break;
                        case CommService.STATE_CONNECTING:
                            //state.setText(R.string.title_connecting);
                            break;
                        case CommService.STATE_LISTEN:
                        case CommService.STATE_NONE:
                            //state.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_READ:
                    byte[] buf = (byte[]) msg.obj;
                    mConversationArrayAdapter.add(mConnectedDeviceName+":   " + new String(buf).trim());
                    break;
                case MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME); // save the connected device's name
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    CharSequence[] address = data.getExtras().getCharSequenceArray(DiscoverDevice.EXTRA_DEVICE_ADDRESS);

                    int selectedPosition=0;
                    // Get the BLuetoothDevice object
                    for (CharSequence cs : address){
                        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(cs.toString());
                        System.out.println("connect" + selectedPosition);
                        commService.connect(device);//, selectedPosition);
                        selectedPosition++;
                    }
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    try {
                        setupService();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else {
                    // User did not enable Bluetooth or an error occured
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
}
