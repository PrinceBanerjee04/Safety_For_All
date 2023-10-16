package com.example.safetyforall;

import android.location.LocationListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationSender implements LocationListener {

    DatabaseReference databaseReference;

    private String TAG="LocationSender";

    private Contacts savedContacts;

    private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60; // 1 minute
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10.0f; // 10 meters

    private Context context;
    private LocationManager locationManager;
    private FirebaseUser firebaseUser = null;

    public LocationSender(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Contacts");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getdata();

    }

    private void getdata() {

        if(databaseReference!=null && firebaseUser!=null){
            databaseReference.child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {

                        Log.d(TAG,"Success");
                        DataSnapshot d= task.getResult();
                        savedContacts = d.getValue(Contacts.class);

                    }
                    else {
                        Log.d(TAG,"Failed" +String.valueOf(task.getResult().getValue())+"    "+task.getException());
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    }
                }
            });
        }


    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle permission not granted
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                this
        );
    }

    public void stopLocationUpdates() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        sendLocationViaSms(location.getLatitude(), location.getLongitude());
        stopLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    public void sendLocationViaSms(double latitude, double longitude) {
        SmsManager smsManager = SmsManager.getDefault();
        String link="http://maps.google.com/maps?q=";
        String s = link + latitude + "," + longitude;
        String message = "!! SOS ALERT !! \n In danger. HELP! \n My current location is: " +s;

        Log.d(TAG,"message = "+message);

        if(savedContacts!=null){
            smsManager.sendTextMessage(savedContacts.getC1(), null, message, null, null);
            smsManager.sendTextMessage(savedContacts.getC2(), null, message, null, null);
            smsManager.sendTextMessage(savedContacts.getC3(), null, message, null, null);
        }
        Toast.makeText(context, "Location sent via SMS", Toast.LENGTH_SHORT).show();
    }


}
