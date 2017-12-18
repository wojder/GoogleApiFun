package com.wojder.googleapifun;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetDevInfoActivity extends AppCompatActivity {
    @BindView(R.id.deviceIdName)
    TextView devIdName;

    @BindView(R.id.deviceIdNumber)
    TextView devIdNumber;

    @BindView(R.id.mainView)
    View mLayout;

    public static final String TAG = GetDevInfoActivity.class.getSimpleName();
    public static final int REQUEST_PHONE_INFO = 0;
    String deviceEmei = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_info);
        ButterKnife.bind(this);

        getDeviceEmeiNumber();

        devIdNumber.setText(deviceEmei);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE)) {
                Snackbar.make(mLayout, "Phone access is required",
                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(GetDevInfoActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_INFO);
                    }
                }).show();
            } else {
                Snackbar.make(mLayout, "Permission is N/A, Requesting...", Snackbar.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_INFO);
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


}
