package com.wojder.googleapifun;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetDevInfoActivity extends AppCompatActivity {

    @BindView(R.id.deviceIdName)
    TextView devIdName;

    @BindView(R.id.deviceIdNumber)
    TextView devIdNumber;

    @BindView(R.id.mainView)
    View mLayout;

    @BindView(R.id.bleMacAddress)
    TextView bleMacAddressName;

    @BindView(R.id.bleMacAddressNumber)
    TextView bleMacAddressNumber;

    @BindView(R.id.wifiMacAddressLabel)
    TextView wifiMacAddressLabel;

    @BindView(R.id.wifiMacAddressNumber)
    TextView wifiMacAddressNumber;


    public static final String TAG = GetDevInfoActivity.class.getSimpleName();
    public static final int REQUEST_PHONE_INFO = 0;
    private static final int REQUEST_BL_ADMIN = 1;

    String deviceEmei = null;
    String bleMacAddress = null;
    String wifiMacAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_info);
        ButterKnife.bind(this);

        getDeviceEmeiNumber();
        getDeviceBleMacAddress();
        getDeviceWifiMacAddress();

        devIdNumber.setText(deviceEmei);
        bleMacAddressNumber.setText(bleMacAddress);
        wifiMacAddressNumber.setText(wifiMacAddress);
    }

    private String getDeviceWifiMacAddress() {
        WifiManager wifiMan = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        wifiMacAddress = wifiInf.getMacAddress();

        Log.i(TAG, "Device WiFi mac address is: " + wifiMacAddress);
        return wifiMacAddress;
    }

    private String getDeviceBleMacAddress() {

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        bleMacAddress = adapter.getAddress();

        Log.i(TAG, "Device BL mac address is: " + bleMacAddress);

        return bleMacAddress;
    }

    private String getDeviceEmeiNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                deviceEmei = telephonyManager.getDeviceId();
            } else {
                requestPhoneInfoPermission();
            }
        }
        Log.i(TAG, "Device EMEI is: " + deviceEmei);
        return deviceEmei;
    }

    private void requestPhoneInfoPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            Snackbar.make(mLayout, "Phone access is required",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(GetDevInfoActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_INFO);
                }
            }).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_ADMIN)) {
            Snackbar.make(mLayout, "BL Admin access is required", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(GetDevInfoActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_BL_ADMIN);
                }
            }).show();
        } else {
            Snackbar.make(mLayout, "Permission is N/A, Requesting...", Snackbar.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_INFO);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_BL_ADMIN);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PHONE_INFO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Phone permission was granted. Starting preview", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Phone permission was denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}



