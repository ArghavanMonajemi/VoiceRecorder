package com.android.voicerecorder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.voicerecorder.R;
import com.android.voicerecorder.fragments.RecorderFragment;

public class MainActivity extends AppCompatActivity {

    FrameLayout container;
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
    private static final int PERMISSIONS_REQ_CODE = 17;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstanceState = savedInstanceState;
        container = findViewById(R.id.main_activity_container);
        checkPermissions();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, permissions[2]) == PackageManager.PERMISSION_GRANTED)
            setFragment();
        else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQ_CODE);
        }
    }

    private void setFragment() {
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, new RecorderFragment(), getResources().getString(R.string.recorder_fragment)).commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    if (grantResults[2] == PackageManager.PERMISSION_GRANTED)
                        setFragment();
                    else
                        ActivityCompat.requestPermissions(this, new String[]{permissions[2]}, PERMISSIONS_REQ_CODE);
                else
                    ActivityCompat.requestPermissions(this, new String[]{permissions[1]}, PERMISSIONS_REQ_CODE);
            else
                ActivityCompat.requestPermissions(this, new String[]{permissions[0]}, PERMISSIONS_REQ_CODE);
        }
    }
}