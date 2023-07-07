package com.example.jumjareal;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import java.io.*;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class OnceActivity extends AppCompatActivity {
    public static int[] images = new int[]{R.drawable.bichon01, R.drawable.bichon02, R.drawable.bichon03,
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
        coupon.setText("현재 열쇠 개수: " + MainActivity.coupon);

        //random item image
        ImageView item = findViewById(R.id.item);
        int imageId = (int) (Math.random() * images.length);
        item.setImageResource(images[imageId]);
        getId = imageId;

        //save inventory
        InventoryActivity.inventory.add(images[imageId]);

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
                        btn_back = findViewById(R.id.btn_back),
                        btn_re = findViewById(R.id.btn_re);
                TextView coupon = findViewById(R.id.coupon);
                btn_share.setVisibility(View.VISIBLE);
                btn_back.setVisibility(View.VISIBLE);
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
        Button btn_share = findViewById(R.id.btn_share), btn_back = findViewById(R.id.btn_back), btn_re = findViewById(R.id.btn_re);
        btn_share.setOnClickListener(view -> {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),images[getId]);

            String fileName = getString(R.string.app_name) + System.currentTimeMillis() + ".jpeg";

            File file = saveImageIntoFileFromUri(getApplicationContext(), bitmap, fileName, getExternalFilePath(getApplicationContext()));

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);

            Uri photoURI = FileProvider.getUriForFile(this,
                    getPackageName(),
                    file);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
            shareIntent.setType("image/jpg");
            startActivity(Intent.createChooser(shareIntent, "Title"));
        });

        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(OnceActivity.this, SubActivity.class);
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
                dlg.setTitle("열쇠 부족");
                dlg.setMessage("열쇠가 부족합니다!");
                dlg.setPositiveButton("확인", (dialog, which) -> {
                    Intent intent = new Intent(OnceActivity.this, SubActivity.class);
                    startActivity(intent);
                    finish();
                });
                dlg.show();
            }
        });
    }
    public static File saveImageIntoFileFromUri(Context context, Bitmap bitmap, String fileName, String path) {
        File file = new File(path, fileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            switch(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1)){
                case "jpeg":
                case "jpg":
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    break;
                case "png":
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    break;
            }

            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("Utils","saveImageIntoFileFromUri FileNotFoundException : "+ e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Utils","saveImageIntoFileFromUri IOException : "+ e.toString());
        }
        return file;
    }

    public static String getExternalFilePath(Context context) {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/";
        return filePath;
    }
}