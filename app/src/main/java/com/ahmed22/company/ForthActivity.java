package com.ahmed22.company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.ahmed22.company.databinding.ActivityForthBinding;
import com.squareup.picasso.Picasso;

public class ForthActivity extends AppCompatActivity {

    ActivityForthBinding forthBinding;

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        forthBinding= DataBindingUtil.setContentView(this,R.layout.activity_forth);
        extras=getIntent().getExtras();

        //check for prevent errors
        if(extras != null){
            forthBinding.firstName.setText(extras.getString("name"));
            forthBinding.age.setText(extras.getString("age"));
            Picasso.get().load(extras.getString("imageUri"))
                    .into(forthBinding.image);
        }
    }
}