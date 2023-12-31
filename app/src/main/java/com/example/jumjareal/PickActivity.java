package com.example.jumjareal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PickActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        TextView coupon = findViewById(R.id.coupon);
        coupon.setText("현재 열쇠 개수: "+MainActivity.coupon);
        listener();
    }

    private void listener(){
        Button btn_once = findViewById(R.id.btn_once), btn_cancel = findViewById(R.id.btn_cancel);
        btn_once.setOnClickListener(view -> {
            if (MainActivity.coupon != 0) {
                MainActivity.coupon--;
                Intent intent = new Intent(PickActivity.this, OnceActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_out);
                finish();
            }
            else {
                AlertDialog.Builder dlg = new AlertDialog.Builder(PickActivity.this);
                dlg.setTitle("열쇠 부족"); //제목
                dlg.setMessage("열쇠가 부족합니다!"); // 메시지
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인", (dialog, which) -> {
                    Intent intent = new Intent(PickActivity.this, SubActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                });
                dlg.show();
            }
        });

        btn_cancel.setOnClickListener(view -> {
            Intent intent = new Intent(PickActivity.this, SubActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
    }
}