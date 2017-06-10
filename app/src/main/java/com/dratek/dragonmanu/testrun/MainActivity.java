package com.dratek.dragonmanu.testrun;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import static android.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;

public class MainActivity extends AppCompatActivity {

    String sendMessage;
    String defaulton;
    String defaultoff;
    String message;

    BluetoothDevice thedevice;

    private BluetoothAdapter BTAdapter;
    private BluetoothGatt mGatt;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private Handler mHandler = new Handler();
    private boolean mScanning;

    private static final long SCAN_PERIOD = 10000;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        Button defaultStateButton = (Button) findViewById(R.id.defaultstate);
        Button switchButton = (Button) findViewById(R.id.switchstate);
        Button wifiButton = (Button) findViewById(R.id.wifipass);
        Button ipportButton = (Button) findViewById(R.id.ipport);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BTAdapter = bluetoothManager.getAdapter();

        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 0);
        }
        else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = BTAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
            }
        }

        scanLeDevice(true);

        defaultStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DefaultActivity.class);
                startActivityForResult(i,1);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SwitchActivity.class);
                startActivityForResult(i,2);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), WifiActivity.class);
                startActivityForResult(i,3);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        ipportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IpportActivity.class);
                startActivityForResult(i,4);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
    }

    private void scanLeDevice(final boolean enable) {

        final BluetoothLeScanner bluetoothLeScanner = BTAdapter.getBluetoothLeScanner();
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothLeScanner.stopScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            Log.d("device","sadasda");

            mScanning = true;
            bluetoothLeScanner.startScan(mLeScanCallback);
            Log.d("device","staretd");

        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan(mLeScanCallback);
        }
    }
    private ScanCallback mLeScanCallback =
            new ScanCallback()  {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    //thedevice = result.getDevice();
                    //Log.d("device",thedevice.getName());
                    if(result.getDevice().getName()!=null)
                        if (result.getDevice().getName().equals("CC41-A"))
                            connectToDevice(thedevice);
                    super.onScanResult(callbackType, result);
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    for (ScanResult result : results) {
                        //thedevice = result.getDevice();
                        //Log.d("device",thedevice.getName());
                        if(result.getDevice().getName()!=null)
                            if (result.getDevice().getName().equals("CC41-A"))
                                connectToDevice(result.getDevice());
                    }
                    super.onBatchScanResults(results);
                }

                @Override
                public void onScanFailed(int errorCode) {
                    Log.d("device","failed");

                    super.onScanFailed(errorCode);
                }
            };

    public void connectToDevice(BluetoothDevice device) {
        message = null;
        Log.d("device","connected");
        mGatt = device.connectGatt(this, false, mGattCallback);
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            //Log.i("onServicesDiscovered", services.toString());
            BluetoothGattCharacteristic commandChar = services.get(3).getCharacteristics().get(0);
            Log.i("onServicesDiscovered", commandChar.getUuid().toString());

            //data to be sent
            String data = "upst"+defaulton+"\n";
            byte[] byteArray = data.getBytes();
            commandChar.setValue(byteArray);
            gatt.writeCharacteristic(commandChar);

            //Receive updates if message is changed on BLE device
            gatt.setCharacteristicNotification(services.get(3).getCharacteristics().get(0),true);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
            if (characteristic.getValue()!= null && characteristic.getValue().length >0) {
                Log.d("DEVICELIST", "reading"+ new String(characteristic.getValue())+ characteristic.getValue().length);
            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Log.i("onCharacteristicChange", characteristic.toString());
            if (characteristic.getValue()!= null && characteristic.getValue().length >0) {
                Log.d("DEVICELIST", "read from BLE : "+ new String(characteristic.getValue())); // currently outputs the message read on the logcat
                message = new String(characteristic.getValue());

                /*
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        MainActivity.this);
                alertDialog.setMessage(new String(characteristic.getValue()));
                alertDialog.setTitle("Message received");
                alertDialog.show();
                */
            }
        }
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(status != BluetoothGatt.GATT_SUCCESS){
                Log.d("onCharacteristicWrite", "Failed write, retrying");
                gatt.writeCharacteristic(characteristic);
            }
            Log.d("onCharacteristicWrite", new String(characteristic.getValue()));
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        /*
        public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                                  boolean enabled) {
            if (BTAdapter == null || mGatt == null) {
                Log.d("warning", "BluetoothAdapter not initialized");
                return;
            }
            mGatt.setCharacteristicNotification(characteristic, enabled);

            // This is specific to Heart Rate Measurement.
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mGatt.writeDescriptor(descriptor);

        }
        */

    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            defaulton = data.getExtras().getString("defaulton");
            defaultoff = data.getExtras().getString("defaultoff");
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("defaulton", defaulton);
            editor.putString("defaultoff",defaultoff);
            editor.commit();
            Toast.makeText(MainActivity.this,"Changed defaults", Toast.LENGTH_SHORT).show();
        }
        else if(resultCode == RESULT_OK && requestCode != 0) {
            sendMessage = data.getExtras().getString("message");
            Toast.makeText(MainActivity.this,sendMessage, Toast.LENGTH_LONG).show();
            sendtoBLE(sendMessage);
        }
        else if(resultCode == RESULT_CANCELED && requestCode == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Sorry")
                    .setMessage("Bluetooth must be enabled for app to work")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_delete)
                    .show();
        }
    }

    private void sendtoBLE(String sendMessage) {

    }

    private void doExit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mGatt!=null) {
                    mGatt.disconnect();
                    scanLeDevice(false);
                    BTAdapter.cancelDiscovery();
                    BTAdapter.disable();
                    mGatt.close();
                }
                MainActivity.super.onBackPressed();
            }
        });

        alertDialog.setNegativeButton("No", null);

        alertDialog.setMessage("Do you really want to exit?");
        alertDialog.setTitle("DragonTooth");
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        doExit();
    }
}

