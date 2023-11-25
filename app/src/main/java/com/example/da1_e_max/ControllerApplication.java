package com.example.da1_e_max;

import android.app.Application;
import android.content.Context;

import com.example.da1_e_max.constant.Constant;
import com.example.da1_e_max.prefs.DataStoreManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ControllerApplication extends Application {

    private FirebaseDatabase mFirebaseDatabase;

    public static ControllerApplication get(Context context) {
        return (ControllerApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance(Constant.FIREBASE_URL);
        DataStoreManager.init(getApplicationContext());
    }

    public DatabaseReference getFoodDatabaseReference() {
        return mFirebaseDatabase.getReference("/food");
    }

    public DatabaseReference getFeedbackDatabaseReference() {
        return mFirebaseDatabase.getReference("/feedback");
    }

    public DatabaseReference getBookingDatabaseReference() {
        return mFirebaseDatabase.getReference("/booking");
    }
}