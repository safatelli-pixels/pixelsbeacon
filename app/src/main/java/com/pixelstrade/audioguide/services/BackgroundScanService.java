package com.pixelstrade.audioguide.services;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.configuration.ActivityCheckConfiguration;
import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.SpaceListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;
import com.pixelstrade.audioguide.R;
import com.pixelstrade.audioguide.models.Article;
import com.pixelstrade.audioguide.realm.RealmController;
import com.pixelstrade.audioguide.ui.activitys.DetailsArticleActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;

import static android.arch.lifecycle.Lifecycle.State.STARTED;

public class BackgroundScanService extends Service {


    public static final String TAG = "BackgroundScanService";
    public static final String ACTION_DEVICE_DISCOVERED = "DeviceDiscoveredAction";
    public static final String EXTRA_DEVICE = "DeviceExtra";
    public static final String EXTRA_DEVICES_COUNT = "DevicesCountExtra";
    Context context ;

    private static final long TIMEOUT = TimeUnit.SECONDS.toMillis(30);
    String uidDevice = "" ;

    private final Handler handler = new Handler();
    private ProximityManager proximityManager;
    public static boolean isRunning = false; // Flag indicating if service is already running.
    private int devicesCount; // Total discovered devices count
    Boolean isInRegionChanged = false ;
    int region = 0 ;
    Boolean isNotified = false ;
    AlertDialog alert ;
    @Override
    public void onCreate() {
        super.onCreate();

        setupProximityManager();

    }

    private void setupProximityManager() {
        //Create proximity manager instance
        proximityManager = ProximityManagerFactory.create(this);

        //Configure proximity manager basic options
        proximityManager.configuration()
                //Using ranging for continuous scanning or MONITORING for scanning with intervals
                .scanPeriod(ScanPeriod.RANGING)
                .activityCheckConfiguration(ActivityCheckConfiguration.DEFAULT)

                //Using BALANCED for best performance/battery ratio
               .deviceUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(180))
                .scanMode(ScanMode.BALANCED);

        //Setting up iBeacon and Eddystone listeners
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.setSpaceListener(new SpaceListener() {
            @Override
            public void onRegionEntered(IBeaconRegion region) {
                Log.e("onRegion","entered");
                Toast.makeText(BackgroundScanService.this," region entered", Toast.LENGTH_LONG).show();
                //isInRegion = true ;
            }

            @Override
            public void onRegionAbandoned(IBeaconRegion region) {
                Log.e("onRegion","abandoned");
                Toast.makeText(BackgroundScanService.this,"abandoned region", Toast.LENGTH_LONG).show();
                isInRegionChanged= true ;

            }

            @Override
            public void onNamespaceEntered(IEddystoneNamespace namespace) {
                Log.e("onRegionNameSpace","entered");
                Toast.makeText(BackgroundScanService.this," name entered", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNamespaceAbandoned(IEddystoneNamespace namespace) {
                Log.e("onRegionNameSpace","abandoned");
                Toast.makeText(BackgroundScanService.this," name abandoned", Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Check if service is already active


        startScanning();
        isRunning = true;
        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                Toast.makeText(BackgroundScanService.this,"Scan start",Toast.LENGTH_LONG).show();
                proximityManager.startScanning();
                devicesCount = 0;
            }
        });
        //stopAfterDelay();
    }

    private void stopAfterDelay() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                proximityManager.disconnect();
                stopSelf();

            }
        }, TIMEOUT);
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
              //  Toast.makeText(BackgroundScanService.this,"discovered"+ ibeacon.getProximity().toString(), Toast.LENGTH_LONG).show();

               // Log.i(TAG, "onIBeaconDiscovered: " + ibeacon.getProximityUUID()+ "-"+ System.currentTimeMillis());

                Article article = null ;

                if( ibeacon!= null)
                {
                   article = RealmController.getInstance().getArticleByBeacon(ibeacon.getProximityUUID().toString()
                            , String.valueOf(ibeacon.getMajor())
                            , String.valueOf(ibeacon.getMinor()));
                }


              //  Article article = RealmController.getInstance().getArticleByBeacon(ibeacon.getProximityUUID().toString(), "4660", "22136");


                if( article!= null)
                {
                    Realm realm = RealmController.getInstance().getRealm();
                    realm.beginTransaction();
                    //update proximity ibeacon
                    article.getiBeacon().setDistance(String.valueOf(ibeacon.getDistance()));
                    realm.copyToRealmOrUpdate(article);
                    realm.commitTransaction();





                }

                if (article != null && ibeacon.getProximity().toString().equals(article.getiBeacon().getProximity()))
                //create Notification
                {
                    triggerNotification(ibeacon, article.getId(),article.getName(),"Nouvel oeuvre detecté ");

                    Realm realm = RealmController.getInstance().getRealm();
                    realm.beginTransaction();
                    //update proximity ibeacon
                    article.getiBeacon().setProximity(ibeacon.getProximity().toString());
                    realm.copyToRealmOrUpdate(article);
                    realm.commitTransaction();

                }


            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> ibeacons, IBeaconRegion region) {
                super.onIBeaconsUpdated(ibeacons, region);

               // Toast.makeText(BackgroundScanService.this,"updated", Toast.LENGTH_LONG).show();


                //  Log.i(TAG, "onIBeaconUpdated: " + ibeacons.size());





                Log.i(TAG, "onIBeaconUpdated: " + ibeacons.get(0).getProximityUUID() +"-"+ System.currentTimeMillis());
             //   Log.i(TAG, "onIBeaconUpdated: " + ibeacons.get(1).getProximityUUID() +"-"+ System.currentTimeMillis());

                for (IBeaconDevice iBeaconDevice : ibeacons) {







                   // Log.e("proximityBeacon",iBeaconDevice.getDistance()+" "+iBeaconDevice.getProximity().toString());

                    Article article = RealmController.getInstance().getArticleByBeacon(iBeaconDevice.getProximityUUID().toString()
                            , String.valueOf(iBeaconDevice.getMajor())
                            , String.valueOf(iBeaconDevice.getMinor()));
                 //   Article article = RealmController.getInstance().getArticleByBeacon(iBeaconDevice.getProximityUUID().toString(), "8663", "15801");


               /*     Realm realm = RealmController.getInstance().getRealm();
                    realm.beginTransaction();
                    article.getiBeacon().setProximity(iBeaconDevice.getProximity().toString());
                    realm.copyToRealmOrUpdate(article);
                    realm.commitTransaction();*/


                    if (article != null) {

                        Realm realmm = RealmController.getInstance().getRealm();
                    realmm.beginTransaction();
                        article.getiBeacon().setDistance(String.valueOf(iBeaconDevice.getDistance()));
                    realmm.copyToRealmOrUpdate(article);
                    realmm.commitTransaction();




                         if ( article.getiBeacon().getProximity().equals(iBeaconDevice.getProximity().toString()) )
                        {
                            //Toast.makeText(BackgroundScanService.this, "updated : "+ iBeaconDevice.getProximity().toString()+""+ "-"+ article.getiBeacon().getProximity(),Toast.LENGTH_LONG).show();
                            triggerNotification(iBeaconDevice, article.getId(),article.getName(),"Nouvel oeuvre detecté ");

                        }

                     /*   if ((article.getiBeacon().getProximity().equals("FAR")||article.getiBeacon().getProximity().equals("NEAR")) && iBeaconDevice.getProximity().toString().equals("IMMEDIATE"))
                        {

                           // triggerNotification(iBeaconDevice, article.getId(),article.getName());
                        }*/

                        if ( (!iBeaconDevice.getProximity().toString().equals(article.getiBeacon().getProximity()) ) && !iBeaconDevice.getProximity().toString().equals("NEAR"))
                        { //update proximity ibeacon


                            Realm realm = RealmController.getInstance().getRealm();
                            realm.beginTransaction();
                            article.getiBeacon().setProximity(iBeaconDevice.getProximity().toString());
                            realm.copyToRealmOrUpdate(article);
                            realm.commitTransaction();
                           // Toast.makeText(BackgroundScanService.this, "not found updated : "+ article.getiBeacon().getProximity(),Toast.LENGTH_LONG).show();

                        }
                    }


                }

            }

            @Override
            public void onIBeaconLost(IBeaconDevice ibeacon, IBeaconRegion region) {
                super.onIBeaconLost(ibeacon, region);

                Log.e(TAG, "onIBeaconLose: " + ibeacon.toString());
            }
        };
    }


    private void triggerNotification(IBeaconDevice ibeacon, int idArticle,String name,String message) {


        Intent intent = new Intent(BackgroundScanService.this, DetailsArticleActivity.class);


        intent.putExtra("id", idArticle);

        PendingIntent contentIntent = PendingIntent.getActivity(BackgroundScanService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(BackgroundScanService.this);


        b.setAutoCancel(true)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(ibeacon.getName())
                .setContentText(message+ name)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.FLAG_AUTO_CANCEL)
                .setContentIntent(contentIntent);


        NotificationManager notificationManager = (NotificationManager) BackgroundScanService.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), b.build());


        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "my_channel_01";
            NotificationChannel channel = null;
            int importance = NotificationManager.IMPORTANCE_HIGH;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        importance);
               Notification notification = new NotificationCompat.Builder(this, channelId)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(ibeacon.getName())
                      // .setContentIntent(PendingIntent.getActivity(BackgroundScanService.this, 0, intent, 0))
                      // .setContentIntent(PendingIntent.getActivity(BackgroundScanService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))

                        .setContentIntent(contentIntent)
                        .setContentText(message + name).build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;


                startForeground(1, notification);
            }
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(channel);
                }
                b.setChannelId(channelId);
            }

          //  notificationManager.notify(1, b.build());


        }





    @Override
    public void onDestroy() {
       /* isRunning = false;
        handler.removeCallbacksAndMessages(null);
        if (proximityManager != null) {
            proximityManager.disconnect();
            proximityManager = null;
        }*/
        //  Toast.makeText(BackgroundScanService.this, "Scanning service stopped.", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private void showAlertDialog() {

        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
        kl.disableKeyguard();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK| PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();


        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Title");


        alert = builder.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        alert.show();


    }
}
