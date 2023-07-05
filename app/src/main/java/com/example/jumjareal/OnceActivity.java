package com.example.jumjareal;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class OnceActivity extends AppCompatActivity {
    int[] images = new int[]{R.drawable.bichon01, R.drawable.bichon02, R.drawable.bichon03,
            R.drawable.frenh01, R.drawable.frenh02, R.drawable.frenh03,
            R.drawable.welsh01, R.drawable.welsh02};
    int getId = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_once);

        //current coupon count
        TextView coupon = findViewById(R.id.coupon);
        coupon.setText("현재 뽑기권 개수: " + MainActivity.coupon);

        //random item image
        ImageView item = findViewById(R.id.item);
        int imageId = (int) (Math.random() * images.length);
        item.setImageResource(images[imageId]);
        getId = imageId;

        //pickup chest animation
        ImageView gif_chest = findViewById(R.id.gif_chest);
        AnimationDrawable animationDrawable = (AnimationDrawable) gif_chest.getBackground();
        animationDrawable.setOneShot(true);
        animationDrawable.start();

        new Handler().postDelayed(() -> startItemAnimation(item), 1400);

        listener();
    }

    private void startItemAnimation(ImageView item) {
        // 알파 애니메이션 생성
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(item, "alpha", 0f, 1f);
        alphaAnimation.setDuration(1000); // 애니메이션 지속 시간 (1초)

        alphaAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 애니메이션 시작 시 호출되는 코드
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Button btn_share = findViewById(R.id.btn_share),
                        btn_home = findViewById(R.id.btn_home),
                        btn_re = findViewById(R.id.btn_re);
                TextView coupon = findViewById(R.id.coupon);
                btn_share.setVisibility(View.VISIBLE);
                btn_home.setVisibility(View.VISIBLE);
                btn_re.setVisibility(View.VISIBLE);
                coupon.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        alphaAnimation.start(); // 애니메이션 시작
    }

    private void listener() {
        //공유 버튼
        Button btn_share = findViewById(R.id.btn_share), btn_home = findViewById(R.id.btn_home), btn_re = findViewById(R.id.btn_re);
        btn_share.setOnClickListener(view -> {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),images[getId]);
            // 이미지를 공유할 때 사용할 임시 파일 생성
            File imageFile = new File(getCacheDir(), "image.png");
            try {
                OutputStream outputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

// 이미지를 공유하는 인텐트 생성
            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", imageFile);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

// 공유 인텐트 시작
            startActivity(Intent.createChooser(shareIntent, "이미지 공유"));

        });

        btn_home.setOnClickListener(view -> {
            Intent intent = new Intent(OnceActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btn_re.setOnClickListener(view -> {
            if (MainActivity.coupon != 0) {
                Intent intent = new Intent(OnceActivity.this, PickActivity.class);
                startActivity(intent);
                finish();
            } else {
                AlertDialog.Builder dlg = new AlertDialog.Builder(OnceActivity.this);
                dlg.setTitle("뽑기권 부족");
                dlg.setMessage("뽑기권을 모두 소진하셨습니다! 점자 번역 기능을 통해 뽑기권을 더 얻으세요!");
                dlg.setPositiveButton("확인", (dialog, which) -> {
                    Intent intent = new Intent(OnceActivity.this, SubActivity.class);
                    startActivity(intent);
                    finish();
                });
                dlg.show();
            }
        });
    }
}