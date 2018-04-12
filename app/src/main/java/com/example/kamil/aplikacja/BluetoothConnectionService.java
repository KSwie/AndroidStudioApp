package com.example.kamil.aplikacja;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnService";
    private static final String myApp = "Aplikacja";
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private final BluetoothAdapter mBluetoothAdapter;
    Context context;
    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog progressDialog;
    private ConnectedThread connectedThread;

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
                connected(socket,mmDevice);
            }

            Log.i(TAG, "run: End AcceptThread");
        }

        public void cancel(){
            Log.d(TAG, "cancel: Cancelling AcceptThread");

            try {
                mServerSocket.close();
            }
            catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread failed: " + e.getMessage() );
            }
        }
    }

    //Ten wątek jest uruchamiany podczas próby nawiązania połączenia wychodzącego
    //z urządzeniem
    private class ConnectThread extends Thread{
        private BluetoothSocket bluetoothSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            Log.d(TAG, "ConnectThread: Started");
            mmDevice = device;
            deviceUUID = uuid;
        }

        @Override
        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "run: ConnectThred");

            try {
                Log.d(TAG, "run: trying to create socket");
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            }
            catch (IOException e) {
                Log.e(TAG, "run: Could not create: "+e.getMessage() );
            }

            bluetoothSocket = tmp;
            mBluetoothAdapter.cancelDiscovery();
            try {
                bluetoothSocket.connect();
                Log.d(TAG, "run: ConnectThread connected");
            }
            catch (IOException e) {
                try {
                    bluetoothSocket.close();
                }
                catch (IOException e1) {
                    Log.e(TAG, "run: unable to close socket: " + e1.getMessage() );
                }

                Log.d(TAG, "run: could not connect" );
            }

            connected(bluetoothSocket,mmDevice);
        }

        public void cancel(){
            try {
                Log.d(TAG, "cancel: closing socket");
                bluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close of socket failed" + e.getMessage() );
            }
        }
    }

    public synchronized void start(){
        Log.d(TAG, "start: method started");

        if(connectThread != null){
            connectThread.cancel();
            connectThread = null;
        }

        if(acceptThread == null){
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startClient: started");

        progressDialog = ProgressDialog.show(context,"Connecting Bluetooth","Please wait...",true);
        connectThread = new ConnectThread(device,uuid);
        connectThread.start();
    }

    private class ConnectedThread extends Thread{
        private BluetoothSocket bluetoothSocket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG, "ConnectedThread: started");

            bluetoothSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            progressDialog.dismiss();

            try {
                tmpIn = bluetoothSocket.getInputStream();
                tmpOut = bluetoothSocket.getOutputStream();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];

            int bytes;

            while(true){
                try {
                    bytes = inputStream.read(buffer);
                    String message = new String(buffer,0,bytes);
                    Log.d(TAG, "run: incoming message: " + message);
                }
                catch (IOException e) {
                    Log.e(TAG, "write: error reading inputStream: " + e.getMessage() );
                    break;
                }

            }
        }

        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: writing to outputStream: " + text);

            try {
                outputStream.write(bytes);
            }
            catch (IOException e) {
                Log.e(TAG, "write: error writing to outputStream: " + e.getMessage() );
            }
        }

        public void cancel(){
            try {
                bluetoothSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connected(BluetoothSocket socket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: starting");

        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
    }

    public void write(byte[] out){
        ConnectedThread tmp;

        Log.d(TAG, "write: write called");

        connectedThread.write(out);
    }
}

