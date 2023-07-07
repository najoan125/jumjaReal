package com.example.jumjareal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class CompleteActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        TextView couponQuantity = findViewById(R.id.coupon);
        couponQuantity.setText("현재 열쇠 개수: "+MainActivity.coupon);
        listener();
    }

    private void listener(){
        Button btn_go;

        btn_go = findViewById(R.id.btn_go);
        btn_go.setOnClickListener(view ->{
            Intent intent = new Intent(CompleteActivity.this, SubActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
    }
}