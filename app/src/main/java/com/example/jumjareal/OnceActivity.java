package com.example.jumjareal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class OnceActivity extends AppCompatActivity {
    int[] images = new int[]{R.drawable.bichon01, R.drawable.bichon02, R.drawable.bichon03,
                            R.drawable.frenh01, R.drawable.frenh02, R.drawable.frenh03,
                            R.drawable.welsh01, R.drawable.welsh02};
    int getId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_once);
        TextView coupon = findViewById(R.id.coupon);
        coupon.setText("현재 뽑기권 개수: "+MainActivity.coupon);

        ImageView img_picked = findViewById(R.id.img_picked);
        int imageId = (int)(Math.random() * images.length);
        img_picked.setBackgroundResource(images[imageId]);
        getId = imageId;
        listener();
    }

    private void listener(){
        Button btn_share = findViewById(R.id.btn_share), btn_home = findViewById(R.id.btn_home), btn_re = findViewById(R.id.btn_re);
        btn_share.setOnClickListener(view ->  {
            Bitmap b = BitmapFactory.decodeResource(getResources(),images[getId]);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), b, "Title", null);
            Uri imageUri =  Uri.parse(path);
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(share, "Select"));
        });

        btn_home.setOnClickListener(view -> {
            Intent intent = new Intent(OnceActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btn_re.setOnClickListener(view -> {
            if (MainActivity.coupon != 0){
                Intent intent = new Intent(OnceActivity.this, PickActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                AlertDialog.Builder dlg = new AlertDialog.Builder(OnceActivity.this);
                dlg.setTitle("뽑기권 부족"); //제목
                dlg.setMessage("뽑기권을 모두 소진하셨습니다! 점자 번역 기능을 통해 뽑기권을 더 얻으세요!"); // 메시지
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(OnceActivity.this, SubActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dlg.show();
            }
        });
    }
}