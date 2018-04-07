package com.example.kamil.aplikacja;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnService";
    private static final String myApp = "Aplikacja";
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private final BluetoothAdapter mBluetoothAdapter;
    Context context;
    private AcceptThread acceptThread;

    public BluetoothConnectionService(Context context) {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
    }

    //ten watek nasluchuje na przychodzace polaczenia, dziala dopoki polaczenie zostanie
    //zaakceptowane lub odrzucone
    //zachowuje sie jak server-side client
    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            try{
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(myApp,MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: setting up server using UUID:" + MY_UUID_INSECURE);
            }
            catch (Exception e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage());
            }
            mServerSocket = tmp;
        }

        @Override
        public void run() {
            Log.d(TAG, "run: AcceptThread running");

            BluetoothSocket socket = null;

            Log.d(TAG, "run: RFCOM server socket start....");
            try {
                socket = mServerSocket.accept();
                Log.d(TAG, "run: RFCOM server socket accepted connection");
            }
            catch (IOException e) {
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage());
            }

            if(socket != null){
                connected(socket,mmDevice);//metoda dopiero zostanie stworzona
            }

            Log.i(TAG, "run: End AcceptThread");
        }

        public void cancel(){
            Log.d(TAG, "cancel: Cancelling AcceptThread");

            try {
                mServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread failed: " + e.getMessage() );
            }
        }
    }
}
