package com.example.gkudva.android_nytimes_client.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.gkudva.android_nytimes_client.R;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
    }
}
