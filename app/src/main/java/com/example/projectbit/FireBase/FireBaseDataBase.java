package com.example.projectbit.FireBase;

import android.app.Application;

import com.firebase.client.Firebase;

public class FireBaseDataBase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
