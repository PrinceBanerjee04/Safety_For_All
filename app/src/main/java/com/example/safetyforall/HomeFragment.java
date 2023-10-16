package com.example.safetyforall;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class HomeFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE=1;
    private LocationSender locationSender;
    private Button btnSendLocation;

   //ProgressDialog progressDialog;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home, container, false);
        locationSender=new LocationSender(requireContext());
        btnSendLocation=view.findViewById(R.id.SOS);


//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Sending Alert...");
//        progressDialog.setCancelable(false);

        btnSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressDialog.show();
                startLocationUpdates();
            }
        });

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_CODE);
        } else {
            // Permissions granted, start location updates
            btnSendLocation.setEnabled(true);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void startLocationUpdates(){
        btnSendLocation.setEnabled(false);
        locationSender.startLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                startLocationUpdates();
            }else{
                Toast.makeText(requireContext(), "Location and SMS permissions are required", Toast.LENGTH_SHORT).show();
                showPermissionAlertDialog();
            }
        }
    }

    private void showPermissionAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        builder.setTitle("Permission Required").setMessage("Location and SMS permissions are required to send the live location via SMS.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requireActivity().finish();
            }
        }).setCancelable(false).show();
    }

    public void onDestroy(){
        super.onDestroy();
        locationSender.stopLocationUpdates();
        //progressDialog.dismiss();
    }




}