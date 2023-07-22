package com.ahmed22.company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.ahmed22.company.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //
        mainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainBinding.button1.setOnClickListener(v -> {
            Intent intent=new Intent(this,SecondActivity.class);
            startActivity(intent);
        });
        //
        //
        mainBinding.button2.setOnClickListener(v -> {
            Intent intent=new Intent(this,ThirdActivity.class);
            startActivity(intent);
        });
    }
}