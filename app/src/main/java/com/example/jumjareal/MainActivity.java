package com.example.jumjareal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static int coupon = 0;

    Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listener();
    }

    private void listener(){
        btn_start = findViewById(R.id.btn_start);
        Button btn_pick = findViewById(R.id.btn_pick);

        btn_start.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SubActivity.class);
            startActivity(intent);
            finish();
        });

        btn_pick.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PickActivity.class);
            startActivity(intent);
            finish();
        });
    }

}