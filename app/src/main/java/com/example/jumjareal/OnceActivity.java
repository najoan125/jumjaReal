package com.example.jumjareal;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class OnceActivity extends AppCompatActivity {
    public static int[] images = new int[]{R.drawable.pick1, R.drawable.pick2, R.drawable.pick3, R.drawable.pick4, R.drawable.pick5,
            R.drawable.pick6, R.drawable.pick7, R.drawable.pickrare1};

    String[] messages = new String[]{"전체 시각장애인 중 전맹은 소수이며, 대부분은 명암과 물체의 형태를 구분할 수 있습니다.",
    "선천적 시각장애인의 대부분은 시각적인 묘사를 이해할 수 있습니다.",
    "어두운 곳에서 책을 읽어도 눈이 나빠지지는 않습니다.",
    "당근 등이 눈에 좋은 것은 사실이지만, 매우 적은 양만이 필요합니다.",
    "안내견에게 말을 걸거나 이름을 불러서는 안 됩니다.",
    "시각장애인에게 도움을 주기 전에는 말로 먼저 물어봐야 합니다.",
    "시각장애인을 돕기 위해 잡아끌거나 당겨서는 안 됩니다. 꼭 말로 먼저 물어봐 주세요."};
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
        Random random = new Random();
        int select = random.nextInt(100);
        int imageId;
        if (select == 99){
            imageId = 7;
            item.setImageResource(images[imageId]);
        } else{
            imageId = (int) (Math.random() * images.length-1);
            item.setImageResource(images[imageId]);
        }
        getId = imageId;

        //save inventory
        InventoryActivity.inventory.add(images[imageId]);

        //pickup chest animation
        ImageView gif_chest = findViewById(R.id.gif_chest);
        AnimationDrawable animationDrawable = (AnimationDrawable) gif_chest.getBackground();
        animationDrawable.setOneShot(true);
        animationDrawable.start();

        gif_chest.postDelayed(() -> startItemAnimation(item), 1400);

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
                TextView coupon = findViewById(R.id.coupon), tip = findViewById(R.id.tip), legendary = findViewById(R.id.legendary);
                btn_share.setVisibility(View.VISIBLE);
                btn_back.setVisibility(View.VISIBLE);
                btn_re.setVisibility(View.VISIBLE);
                coupon.setVisibility(View.VISIBLE);
                tip.setText(messages[(int) (Math.random() * messages.length)]);
                if (getId == 7){
                    legendary.setVisibility(View.VISIBLE);
                }
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
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images[getId]);

            String fileName = getString(R.string.app_name) + System.currentTimeMillis() + ".jpeg";

            File file = saveImageIntoFileFromUri(bitmap, fileName, getExternalFilePath());

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
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });

        btn_re.setOnClickListener(view -> {
            if (MainActivity.coupon != 0) {
                Intent intent = new Intent(OnceActivity.this, PickActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else {
                AlertDialog.Builder dlg = new AlertDialog.Builder(OnceActivity.this);
                dlg.setTitle("열쇠 부족");
                dlg.setMessage("열쇠가 부족합니다!");
                dlg.setPositiveButton("확인", (dialog, which) -> {
                    Intent intent = new Intent(OnceActivity.this, SubActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                });
                dlg.show();
            }
        });
    }

    public static File saveImageIntoFileFromUri(Bitmap bitmap, String fileName, String path) {
        File file = new File(path, fileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            switch (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1)) {
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
            Log.e("Utils", "saveImageIntoFileFromUri FileNotFoundException : " + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Utils", "saveImageIntoFileFromUri IOException : " + e);
        }
        return file;
    }

    public static String getExternalFilePath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";
    }
}