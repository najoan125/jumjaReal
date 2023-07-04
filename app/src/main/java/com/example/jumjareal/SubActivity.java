package com.example.jumjareal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {
    StringBuilder userBrailles = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
//        WebView webView;
//        webView = (WebView) findViewById(R.id.translate);
//        webView.setWebViewClient(new WebViewClient());
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
//        webView.loadUrl("https://t.hi098123.com/braille#share=1&text=");

        Button ok = findViewById(R.id.btn_ok);
        ok.setOnClickListener(view -> {
            if (userBrailles.length() != 0) {
                Intent intent = new Intent(SubActivity.this, CompleteActivity.class);
                MainActivity.coupon++;
                startActivity(intent);
                finish();
            }
            else {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SubActivity.this);
                dlg.setTitle("오류"); //제목
                dlg.setMessage("점자를 입력해주세요!"); // 메시지
                dlg.setPositiveButton("확인", (dialog, which) -> {});
//                버튼 클릭시 동작
                dlg.show();
            }
        });
        listener();
    }

    private void listener() {
        Button btn_put = findViewById(R.id.btn_put), btn_remove = findViewById(R.id.btn_remove), btn_cancel = findViewById(R.id.btn_cancel);
        TextView braille = findViewById(R.id.braille), convert = findViewById(R.id.convert);
        btn_put.setOnClickListener(view -> {
            Boolean[] checked = isCheckboxesChecked();
            String converted = convert(checked[0], checked[1], checked[2], checked[3], checked[4], checked[5]);
            braille.setText(braille.getText()+converted);
            userBrailles.append(converted);
            CheckBox[] checkboxes = {
                    findViewById(R.id.checkbox1),
                    findViewById(R.id.checkbox2),
                    findViewById(R.id.checkbox3),
                    findViewById(R.id.checkbox4),
                    findViewById(R.id.checkbox5),
                    findViewById(R.id.checkbox6)
            };
            for (CheckBox checkBox : checkboxes){
                checkBox.setChecked(false);
            }

        });

        btn_cancel.setOnClickListener(view -> {
            Intent intent = new Intent(SubActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btn_remove.setOnClickListener(view -> {
            if (userBrailles.length() > 0) {
                braille.setText(braille.getText().toString().replaceFirst(".$", ""));
                userBrailles.deleteCharAt(userBrailles.length() - 1);
            }
        });
    }

    private String convert(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) {
        String[] Brailles = new String[]{"⠀","⠁","⠂","⠃","⠄","⠅","⠆","⠇","⠈","⠉","⠊","⠋","⠌","⠍","⠎","⠏",
                "⠐","⠑","⠒","⠓","⠔","⠕","⠖","⠗","⠘","⠙","⠚","⠛","⠜","⠝","⠞","⠟",
                "⠠","⠡","⠢","⠣","⠤","⠥","⠦","⠧","⠨","⠩","⠪","⠫","⠬","⠭","⠮","⠯",
                "⠰","⠱","⠲","⠳","⠴","⠵","⠶","⠷","⠸","⠹","⠺","⠻","⠼","⠽","⠾","⠿"};
        if (!a && !b && !c && !d && !e && !f) {
            return Brailles[0];
        }
        else if (a && !b && !c && !d && !e && !f) {
            return Brailles[1];
        }
        else if (!a && b && !c && !d && !e && !f) {
            return Brailles[2];
        }
        else if (a && b && !c && !d && !e && !f) {
            return Brailles[3];
        }
        else if (!a && !b && c && !d && !e && !f) {
            return Brailles[4];
        }
        else if (a && !b && c && !d && !e && !f) {
            return Brailles[5];
        }
        else if (!a && b && c && !d && !e && !f) {
            return Brailles[6];
        }
        else if (a && b && c && !d && !e && !f) {
            return Brailles[7];
        }
        else if (!a && !b && !c && d && !e && !f) {
            return Brailles[8];
        }
        else if (a && !b && !c && d && !e && !f) {
            return Brailles[9];
        }
        else if (!a && b && !c && d && !e && !f) {
            return Brailles[10];
        }
        else if (a && b && !c && d && !e && !f) {
            return Brailles[11];
        }
        else if (!a && !b && c && d && !e && !f) {
            return Brailles[12];
        }
        else if (a && !b && c && d && !e && !f) {
            return Brailles[13];
        }
        else if (!a && b && c && d && !e && !f) {
            return Brailles[14];
        }
        else if (a && b && c && d && !e && !f) {
            return Brailles[15];
        }
        else if (!a && !b && !c && !d && e && !f) {
            return Brailles[16];
        }
        else if (a && !b && !c && !d && e && !f) {
            return Brailles[17];
        }
        else if (!a && b && !c && !d && e && !f) {
            return Brailles[18];
        }
        else if (a && b && !c && !d && e && !f) {
            return Brailles[19];
        }
        else if (!a && !b && c && !d && e && !f) {
            return Brailles[20];
        }
        else if (a && !b && c && !d && e && !f) {
            return Brailles[21];
        }
        else if (!a && b && c && !d && e && !f) {
            return Brailles[22];
        }
        else if (a && b && c && !d && e && !f) {
            return Brailles[23];
        }
        else if (!a && !b && !c && d && e && !f) {
            return Brailles[24];
        }
        else if (a && !b && !c && d && e && !f) {
            return Brailles[25];
        }
        else if (!a && b && !c && d && e && !f) {
            return Brailles[26];
        }
        else if (a && b && !c && d && e && !f) {
            return Brailles[27];
        }
        else if (!a && !b && c && d && e && !f) {
            return Brailles[28];
        }
        else if (a && !b && c && d && e && !f) {
            return Brailles[29];
        }
        else if (!a && b && c && d && e && !f) {
            return Brailles[30];
        }
        else if (a && b && c && d && e && !f) {
            return Brailles[31];
        }
        else if (!a && !b && !c && !d && !e && f) {
            return Brailles[32];
        }
        else if (a && !b && !c && !d && !e && f) {
            return Brailles[33];
        }
        else if (!a && b && !c && !d && !e && f) {
            return Brailles[34];
        }
        else if (a && b && !c && !d && !e && f) {
            return Brailles[35];
        }
        else if (!a && !b && c && !d && !e && f) {
            return Brailles[36];
        }
        else if (a && !b && c && !d && !e && f) {
            return Brailles[37];
        }
        else if (!a && b && c && !d && !e && f) {
            return Brailles[38];
        }
        else if (a && b && c && !d && !e && f) {
            return Brailles[39];
        }
        else if (!a && !b && !c && d && !e && f) {
            return Brailles[40];
        }
        else if (a && !b && !c && d && !e && f) {
            return Brailles[41];
        }
        else if (!a && b && !c && d && !e && f) {
            return Brailles[42];
        }
        else if (a && b && !c && d && !e && f) {
            return Brailles[43];
        }
        else if (!a && !b && c && d && !e && f) {
            return Brailles[44];
        }
        else if (a && !b && c && d && !e && f) {
            return Brailles[45];
        }
        else if (!a && b && c && d && !e && f) {
            return Brailles[46];
        }
        else if (a && b && c && d && !e && f) {
            return Brailles[47];
        }
        else if (!a && !b && !c && !d && e && f) {
            return Brailles[48];
        }
        else if (a && !b && !c && !d && e && f) {
            return Brailles[49];
        }
        else if (!a && b && !c && !d && e && f) {
            return Brailles[50];
        }
        else if (a && b && !c && !d && e && f) {
            return Brailles[51];
        }
        else if (!a && !b && c && !d && e && f) {
            return Brailles[52];
        }
        else if (a && !b && c && !d && e && f) {
            return Brailles[53];
        }
        else if (!a && b && c && !d && e && f) {
            return Brailles[54];
        }
        else if (a && b && c && !d && e && f) {
            return Brailles[55];
        }
        else if (!a && !b && !c && d && e && f) {
            return Brailles[56];
        }
        else if (a && !b && !c && d && e && f) {
            return Brailles[57];
        }
        else if (!a && b && !c && d && e && f) {
            return Brailles[58];
        }
        else if (a && b && !c && d && e && f) {
            return Brailles[59];
        }
        else if (!a && !b && c && d && e && f) {
            return Brailles[60];
        }
        else if (a && !b && c && d && e && f) {
            return Brailles[61];
        }
        else if (!a && b && c && d && e && f) {
            return Brailles[62];
        }
        else if (a && b && c && d && e && f) {
            return Brailles[63];
        }
        return null;
    }

    private Boolean[] isCheckboxesChecked() {
        ArrayList<Boolean> checked = new ArrayList<>();
        CheckBox[] checkboxes = {
                findViewById(R.id.checkbox1),
                findViewById(R.id.checkbox2),
                findViewById(R.id.checkbox3),
                findViewById(R.id.checkbox4),
                findViewById(R.id.checkbox5),
                findViewById(R.id.checkbox6)
        };
        for (CheckBox checkBox : checkboxes) {
            checked.add(checkBox.isChecked());
        }
        return checked.toArray(new Boolean[0]);
    }
}