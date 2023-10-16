package com.example.safetyforall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class SignUpFragment extends Fragment {

    TextView LogIn;
    EditText email,rg_password,rg_rePassword;
    Button SignIn;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    boolean passwordVisible;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        LogIn=view.findViewById(R.id.BtnLogIn);
        email=view.findViewById(R.id.EditTextMobileNumber);
        rg_password=view.findViewById(R.id.EditTextPassword);
        rg_rePassword=view.findViewById(R.id.EditTextReEnterPassword);
        SignIn=view.findViewById(R.id.BtnSignIn);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Creating your account...");
        progressDialog.setCancelable(false);

        database= FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();
        auth= FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment(), "LogInFragment").commit();
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobNo=email.getText().toString();
                String rg_pass=rg_password.getText().toString();
                String rg_rePass=rg_rePassword.getText().toString();

                if(TextUtils.isEmpty(mobNo) || TextUtils.isEmpty(rg_pass) || TextUtils.isEmpty(rg_rePass)){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"Please Enter Valid Information",Toast.LENGTH_SHORT).show();
                } else if (!mobNo.matches(emailPattern)) {
                    progressDialog.dismiss();
                    email.setError("Type a valid mobile number");
                } else if (rg_pass.length()<6) {
                    progressDialog.dismiss();
                    rg_password.setError("Password must be more than 6 characters");
                } else if (!rg_pass.equals(rg_rePass)) {
                    progressDialog.dismiss();
                    rg_password.setError("The Password Doesn't Match");
                }else {
                    auth.createUserWithEmailAndPassword(mobNo,rg_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.show();
                                Toast.makeText(getActivity(), "Signup successful", Toast.LENGTH_SHORT).show();
                                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment(), "MainActivity").commit();
                                Intent i = new Intent(getActivity(), MainActivity3.class);
                                startActivity(i);
                                ((Activity) getActivity()).overridePendingTransition(0, 0);
                            }
                            else{
                                Toast.makeText(getActivity(), "Signup Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        rg_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=rg_password.getRight()-rg_password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=rg_password.getSelectionEnd();
                        if(passwordVisible){
                            rg_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            rg_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }else{
                            rg_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            rg_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        rg_password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        rg_rePassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=rg_rePassword.getRight()-rg_password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection1=rg_password.getSelectionEnd();
                        if(passwordVisible){
                            rg_rePassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            rg_rePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }else{
                            rg_rePassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            rg_rePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        rg_rePassword.setSelection(selection1);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}