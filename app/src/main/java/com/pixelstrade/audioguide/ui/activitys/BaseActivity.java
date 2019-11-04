package com.pixelstrade.audioguide.ui.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.pixelstrade.audioguide.R;
import com.pixelstrade.audioguide.services.BackgroundScanService;
import com.pixelstrade.audioguide.ui.fragments.BaseFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.pixelstrade.audioguide.ui.fragments.BaseFragment.FRAGMENT_DIALOG;


public class BaseActivity extends AppCompatActivity {

    private ProximityManager proximityManager;
    public static final String TAG = "ProximityManager";

    private static final int REQUEST_LOCATION_PERMISSION = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //start scanning ibeacon
            //start service scan ibeacon
            if (!BackgroundScanService.isRunning)
            {
               // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                  //  ContextCompat.startForegroundService(this,new Intent(this, BackgroundScanService.class));
               // }
               // else
                   // startForegroundService(new Intent(this, BackgroundScanService.class));
              //  ContextCompat.startForegroundService(this, new Intent(this, BackgroundScanService.class));
                startService( new Intent(this, BackgroundScanService.class));
            }



        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            BaseFragment.ConfirmationDialogFragment
                    .newInstance(R.string.location_permission_confirmation,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION_PERMISSION,
                            R.string.location_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

    }





    @Override
    protected void onStop() {
        super.onStop();
       // stopService(new Intent(this, BackgroundScanService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService(new Intent(this, BackgroundScanService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {



            case REQUEST_LOCATION_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.location_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                // No need to start camera here; it is handled by onResume
                break;

        }
    }

}
