package com.wojder.googleapifun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestrictedAreaActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.buttonGetDevId)
    Button getDeviceIdButton;
    @BindView(R.id.buttonRefreshDbValue)
    Button refreshDbButton;
    @BindView(R.id.valueFromDB)
    TextView dataBaseValue;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    public static final String TAG = "RestrictedArea";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restricted);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor= sharedPreferences.edit();
        final String valueFromDatabase = sharedPreferences.getString("database", "");

        getDeviceIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestrictedAreaActivity.this, GetDevInfoActivity.class));
        }});

        refreshDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "value from Firebase Database is: " + valueFromDatabase);
                dataBaseValue.setText(valueFromDatabase);
                }});

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(RestrictedAreaActivity.this, SigningActivity.class));
            }
        });
    }
}
