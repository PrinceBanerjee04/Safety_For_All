package com.example.safetyforall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

//    TabLayout tabLayout;
//    ViewPager2 viewPager2;
//    Tab_Adapter tab_adapter;

    private FirebaseUser auth=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance().getCurrentUser();

        if(auth!=null){
            Intent intent=new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
            finish();
        }

    }


}