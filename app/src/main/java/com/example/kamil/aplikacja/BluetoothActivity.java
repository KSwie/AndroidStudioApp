package com.example.kamil.aplikacja;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothActivity";
    BluetoothAdapter mBluetoothAdapter;
    Button btnOnOff;
    Button disEnabDiscoverabilityBtn;
    public ArrayList<String> mBTDevices = new ArrayList<>();
    ListView listViewDevices;
    boolean isRegistered1 = false;
    boolean isRegistered2 = false;
    boolean isRegistered3 = false;

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: state off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceive: state turning off");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceive: state on");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceive: state turning on");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)){
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "onReceive: discoverability enabled");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "onReceive: discoverability DISABLED, able to receive connection");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "onReceive: discoverability disabled, not able to receive connection");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "onReceive: connecting...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "onReceive: connected");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device.getName() + "\n" + device.getAddress());
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                listViewDevices.setAdapter(new ArrayAdapter<>(BluetoothActivity.this,
                        android.R.layout.simple_list_item_1,mBTDevices));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isRegistered1){
            unregisterReceiver(mBroadcastReceiver1);
        }

        if(isRegistered2){
            unregisterReceiver(mBroadcastReceiver2);
        }

        if(isRegistered3){
            unregisterReceiver(mBroadcastReceiver3);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnOnOff = findViewById(R.id.BTonOfBtn);
        disEnabDiscoverabilityBtn = findViewById(R.id.discoverabilityBtn);
        listViewDevices = findViewById(R.id.listViewBT);
        mBTDevices = new ArrayList<>();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableBT();
            }
        });

    }

    public void enableDisableBT(){
        if(mBluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Telefon nie posiada BLluetooth");
        }

        if(!mBluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);

            IntentFilter BTfilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1,BTfilter);
            isRegistered1 = true;
        }

        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();

            IntentFilter BTfilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1,BTfilter);
            isRegistered1 = true;
        }
    }

    public void enableDiscoverability(View view) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent);

        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);
        isRegistered2 = true;
    }

    public void discoverDevicesClick(View view) {
        mBTDevices.clear();
        if(!mBluetoothAdapter.isEnabled()){
            Toast.makeText(this,"Moduł Bluetooth jest wyłączony!",Toast.LENGTH_LONG).show();
        }
        else{
            if(mBluetoothAdapter.isDiscovering()){
                mBluetoothAdapter.cancelDiscovery();

                checkBTPermission();//SPRAWDZA DOSTEPNOSC BT

                mBluetoothAdapter.startDiscovery();
                IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mBroadcastReceiver3,intentFilter);
                isRegistered3 = true;
            }

            if(!mBluetoothAdapter.isDiscovering()){
                checkBTPermission();

                mBluetoothAdapter.startDiscovery();
                IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mBroadcastReceiver3,intentFilter);
                isRegistered3 = true;
            }
        }
    }

    //wymagana metoda dla urzadzen z systemem LOLLIPOP, API 23+
    //sprawdza czy wersja androida jest wyzsza niz lollipop, jezeli jest to uzyskuje pozwolenie
    private void checkBTPermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if(permissionCheck != 0 ){
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                ,1001);
            }
            else{
                Log.d(TAG, "checkBTPermission: No need to check permission. SDK version id higher than LOLLIPOP");
            }
        }
    }
}
