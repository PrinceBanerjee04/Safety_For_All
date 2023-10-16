package com.example.safetyforall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ContactFragment extends Fragment {

    private String TAG="ContactFragment";

    EditText etC1, etC2, etC3;
    Button AddC, DltC;
    DatabaseReference databaseReference;

    FirebaseUser firebaseUser;

    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        etC1 = view.findViewById(R.id.EditTextC1);
        etC2 = view.findViewById(R.id.EditTextC2);
        etC3 = view.findViewById(R.id.EditTextC3);
        AddC = view.findViewById(R.id.BtnAddContacts);
        DltC = view.findViewById(R.id.BtnDltContacts);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Contacts");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

            AddC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertContactsData();
                    Intent i = new Intent(getActivity(), MainActivity2.class);
                    startActivity(i);
                    ((Activity) getActivity()).overridePendingTransition(0, 0);
                }
            });

            DltC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateContactsData();
                    Intent i = new Intent(getActivity(), MainActivity2.class);
                    startActivity(i);
                    ((Activity) getActivity()).overridePendingTransition(0, 0);
                }
            });

    }

    private void insertContactsData() {
        String c1 = etC1.getText().toString();
        String c2 = etC2.getText().toString();
        String c3 = etC3.getText().toString();

        Contacts contacts = new Contacts(c1, c2, c3);

        //databaseReference.push().setValue(firebaseUser.getUid()).setValue(contacts);

        databaseReference.child(firebaseUser.getUid()).setValue(contacts);
        Toast.makeText(getActivity(), "Contacts Successfully Added", Toast.LENGTH_SHORT).show();
    }

    private void updateContactsData() {
        String c1 = etC1.getText().toString();
        String c2 = etC2.getText().toString();
        String c3 = etC3.getText().toString();

        Contacts contacts = new Contacts(c1, c2, c3);

        //databaseReference.push().setValue(firebaseUser.getUid()).setValue(contacts);

        databaseReference.child(firebaseUser.getUid()).setValue(contacts);
        Toast.makeText(getActivity(), "Contacts Successfully Updated", Toast.LENGTH_SHORT).show();
    }
}