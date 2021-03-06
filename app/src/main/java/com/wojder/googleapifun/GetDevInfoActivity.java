package com.wojder.googleapifun;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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

    @BindView(R.id.currentDateFromBuild)
    TextView currentTimeFromBuild;

    @BindView(R.id.currentDateFromDate)
    TextView currentDateFromDateClass;


    public static final String TAG = GetDevInfoActivity.class.getSimpleName();
    public static final String BUILD_TAG = "BuildTag";
    public static final int REQUEST_PHONE_INFO = 0;
    private static final int REQUEST_BL_ADMIN = 1;
    SimpleDateFormat simpleDateFormat;

    public String deviceEmei = null;
    String bleMacAddress = null;
    String wifiMacAddress = null;
    String timeFromBuildClass = null;
    String timeFromDateClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_info);
        ButterKnife.bind(this);

        getDeviceEmeiNumber();
        getDeviceBleMacAddress();
        getDeviceWifiMacAddress();
        getCurrentDateFromBuild();

        devIdNumber.setText(deviceEmei);
        bleMacAddressNumber.setText(bleMacAddress);
        wifiMacAddressNumber.setText(wifiMacAddress);
        currentTimeFromBuild.setText(getCurrentDateFromBuild());
        currentDateFromDateClass.setText(getCurrentDateFromDate());

        Log.i(BUILD_TAG, "///////////////////////////////////////////////////////");
        Log.i(BUILD_TAG, "Info from Build class, board: " + Build.BOARD);
        Log.i(BUILD_TAG, "Info from Build class, device: " + Build.DEVICE);
        Log.i(BUILD_TAG, "Info from Build class, hardware: " + Build.HARDWARE);
        Log.i(BUILD_TAG, "Info from Build class, display: " + Build.DISPLAY);
        Log.i(BUILD_TAG, "Info from Build class, fingerPrint: " + Build.FINGERPRINT);
        Log.i(BUILD_TAG, "Info from Build class, BRAND: " + Build.BRAND);
        Log.i(BUILD_TAG, "Info from Build class, PRODUCT: " + Build.PRODUCT);
        Log.i(BUILD_TAG, "Info from Build class, Radio Version: " + Build.getRadioVersion());
        Log.i(BUILD_TAG, "Info from Build class, TYPE: " + Build.TYPE);
        Log.i(BUILD_TAG, "Info from Build class, USER: " + Build.USER);
        Log.i(BUILD_TAG, "Info from Build class, BOOTLOADER: " + Build.BOOTLOADER);
        Log.i(BUILD_TAG, "Info from Build class, TIME: " + Build.TIME);
        Log.i(BUILD_TAG, "Info from Build class, SUPPORTED_ABIS: " + Arrays.toString(Build.SUPPORTED_ABIS));
    }

    public String getCurrentDateFromDate() {

        Date dateFromDateClass = new Date();
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm z");
        //timeFromDateClass = simpleDateFormat.format(dateFromDateClass);

        timeFromDateClass = DateFormat.getDateTimeInstance(
                DateFormat.LONG, DateFormat.LONG).format(dateFromDateClass);

        return timeFromDateClass;
    }

    private String getCurrentDateFromBuild() {
        Long timestampFromDeviceBuild = Build.TIME;

        String currentTime = DateUtils.formatDateTime(this, timestampFromDeviceBuild, DateUtils.FORMAT_SHOW_TIME);
        String currentDate = DateUtils.formatDateTime(this, timestampFromDeviceBuild, DateUtils.FORMAT_SHOW_DATE);

        Log.i(TAG, "Current Date and Time from Build class is: " + currentDate + " |||| " + currentTime);

        timeFromBuildClass = currentTime + " ||| " + currentDate;

        return timeFromBuildClass;
    }

    private String getDeviceWifiMacAddress() {
        WifiManager wifiMan = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiMan != null) {
            WifiInfo wifiInf = wifiMan.getConnectionInfo();
            wifiMacAddress = wifiInf.getMacAddress();
        }
        Log.i(TAG, "Device WiFi mac address is: " + wifiMacAddress);
        return wifiMacAddress;
    }

    private String getDeviceBleMacAddress() {

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        bleMacAddress = adapter.getAddress();

        Log.i(TAG, "Device BL mac address is: " + bleMacAddress);

        return bleMacAddress;
    }

    public String getDeviceEmeiNumber() {
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



