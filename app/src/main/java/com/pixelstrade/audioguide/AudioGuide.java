package com.pixelstrade.audioguide;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.kontakt.sdk.android.common.KontaktSDK;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class AudioGuide extends Application {


    private static final String API_KEY = "YVMS1AZhgAEjC1Krg5X5tiI9AhhmpgbP";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());


        //initialize kontak sdk
        initializeDependencies();

        Realm.init(this);

        //initialize Realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }


    //Initializing Kontakt SDK. Insert your API key to allow all samples to work correctly
    private void initializeDependencies() {
        KontaktSDK.initialize(API_KEY);
    }
}
