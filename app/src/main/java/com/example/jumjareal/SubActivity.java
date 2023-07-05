package com.example.jumjareal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

public class SubActivity extends AppCompatActivity {
    WebView webView;
    final Bundle bundle = new Bundle();
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            AlertDialog.Builder dlg = new AlertDialog.Builder(SubActivity.this);
            dlg.setTitle("점자 뜻"); // 제목
            dlg.setMessage("입력한 점자의 뜻: " + bundle.getString("translated")); // 메시지
            dlg.setPositiveButton("확인", (dialog, which) -> {
                Intent intent = new Intent(SubActivity.this, CompleteActivity.class);
                MainActivity.coupon++;
                startActivity(intent);
                finish();
            });
            dlg.setCancelable(false);
            dlg.show();
        }
    };


    public class MyJavascriptInterface {
        @JavascriptInterface
        public void getHtml(String html) {
            new Thread() {
                @Override
                public void run() {
                    if (!html.isEmpty()) {
                        Document doc = Jsoup.parse(html);
                        Element element = doc.select("#plain div").first();
                        bundle.putString("translated", Objects.requireNonNull(element).text());
                        Message msg = handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                }
            }.start();
        }
    }

    StringBuilder userBrailles = new StringBuilder();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        webView = findViewById(R.id.translate);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavascriptInterface(), "Android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
            }
        });
        listener();
    }

    @SuppressLint("SetTextI18n")
    private void listener() {
        Button btn_put = findViewById(R.id.btn_put), btn_remove = findViewById(R.id.btn_remove), btn_cancel = findViewById(R.id.btn_cancel);
        TextView braille = findViewById(R.id.braille);

        //입력 버튼
        btn_put.setOnClickListener(view -> {
            Boolean[] checked = isCheckboxesChecked();
            String converted = convert(checked[0], checked[1], checked[2], checked[3], checked[4], checked[5]);
            braille.setText(braille.getText() + converted);
            userBrailles.append(converted);
            CheckBox[] checkboxes = {
                    findViewById(R.id.checkbox1),
                    findViewById(R.id.checkbox2),
                    findViewById(R.id.checkbox3),
                    findViewById(R.id.checkbox4),
                    findViewById(R.id.checkbox5),
                    findViewById(R.id.checkbox6)
            };
            for (CheckBox checkBox : checkboxes) {
                checkBox.setChecked(false);
            }
        });

        //취소 버튼
        btn_cancel.setOnClickListener(view -> {
            Intent intent = new Intent(SubActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        //지우기 버튼
        btn_remove.setOnClickListener(view -> {
            if (userBrailles.length() > 0) {
                braille.setText(braille.getText().toString().replaceFirst(".$", ""));
                userBrailles.deleteCharAt(userBrailles.length() - 1);
            }
        });

        //확인 버튼
        Button ok = findViewById(R.id.btn_ok);
        ok.setOnClickListener(view -> {
            if (userBrailles.length() != 0) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SubActivity.this);
                dlg.setTitle("로드 중..."); //제목
                dlg.setMessage("로딩 중입니다. 잠시만 기다려주세요..."); // 메시지
                dlg.setCancelable(false);
                dlg.show();
                try {
                    webView.loadUrl("https://t.hi098123.com/braille#share=1&text=" + URLEncoder.encode(userBrailles.toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SubActivity.this);
                dlg.setTitle("오류"); //제목
                dlg.setMessage("점자를 입력해주세요!"); // 메시지
                dlg.setPositiveButton("확인", (dialog, which) -> {
                });
                dlg.show();
            }
        });
    }

    private String convert(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) {
        String[] Brailles = new String[]{"⠀", "⠁", "⠂", "⠃", "⠄", "⠅", "⠆", "⠇", "⠈", "⠉", "⠊", "⠋", "⠌", "⠍", "⠎", "⠏",
                "⠐", "⠑", "⠒", "⠓", "⠔", "⠕", "⠖", "⠗", "⠘", "⠙", "⠚", "⠛", "⠜", "⠝", "⠞", "⠟",
                "⠠", "⠡", "⠢", "⠣", "⠤", "⠥", "⠦", "⠧", "⠨", "⠩", "⠪", "⠫", "⠬", "⠭", "⠮", "⠯",
                "⠰", "⠱", "⠲", "⠳", "⠴", "⠵", "⠶", "⠷", "⠸", "⠹", "⠺", "⠻", "⠼", "⠽", "⠾", "⠿"};
        if (!a && !b && !c && !d && !e && !f) {
            return Brailles[0];
        } else if (a && !b && !c && !d && !e && !f) {
            return Brailles[1];
        } else if (!a && b && !c && !d && !e && !f) {
            return Brailles[2];
        } else if (a && b && !c && !d && !e && !f) {
            return Brailles[3];
        } else if (!a && !b && c && !d && !e && !f) {
            return Brailles[4];
        } else if (a && !b && c && !d && !e && !f) {
            return Brailles[5];
        } else if (!a && b && c && !d && !e && !f) {
            return Brailles[6];
        } else if (a && b && c && !d && !e && !f) {
            return Brailles[7];
        } else if (!a && !b && !c && d && !e && !f) {
            return Brailles[8];
        } else if (a && !b && !c && d && !e && !f) {
            return Brailles[9];
        } else if (!a && b && !c && d && !e && !f) {
            return Brailles[10];
        } else if (a && b && !c && d && !e && !f) {
            return Brailles[11];
        } else if (!a && !b && c && d && !e && !f) {
            return Brailles[12];
        } else if (a && !b && c && d && !e && !f) {
            return Brailles[13];
        } else if (!a && b && c && d && !e && !f) {
            return Brailles[14];
        } else if (a && b && c && d && !e && !f) {
            return Brailles[15];
        } else if (!a && !b && !c && !d && e && !f) {
            return Brailles[16];
        } else if (a && !b && !c && !d && e && !f) {
            return Brailles[17];
        } else if (!a && b && !c && !d && e && !f) {
            return Brailles[18];
        } else if (a && b && !c && !d && e && !f) {
            return Brailles[19];
        } else if (!a && !b && c && !d && e && !f) {
            return Brailles[20];
        } else if (a && !b && c && !d && e && !f) {
            return Brailles[21];
        } else if (!a && b && c && !d && e && !f) {
            return Brailles[22];
        } else if (a && b && c && !d && e && !f) {
            return Brailles[23];
        } else if (!a && !b && !c && d && e && !f) {
            return Brailles[24];
        } else if (a && !b && !c && d && e && !f) {
            return Brailles[25];
        } else if (!a && b && !c && d && e && !f) {
            return Brailles[26];
        } else if (a && b && !c && d && e && !f) {
            return Brailles[27];
        } else if (!a && !b && c && d && e && !f) {
            return Brailles[28];
        } else if (a && !b && c && d && e && !f) {
            return Brailles[29];
        } else if (!a && b && c && d && e && !f) {
            return Brailles[30];
        } else if (a && b && c && d && e && !f) {
            return Brailles[31];
        } else if (!a && !b && !c && !d && !e) {
            return Brailles[32];
        } else if (a && !b && !c && !d && !e) {
            return Brailles[33];
        } else if (!a && b && !c && !d && !e) {
            return Brailles[34];
        } else if (a && b && !c && !d && !e) {
            return Brailles[35];
        } else if (!a && !b && c && !d && !e) {
            return Brailles[36];
        } else if (a && !b && c && !d && !e) {
            return Brailles[37];
        } else if (!a && b && c && !d && !e) {
            return Brailles[38];
        } else if (a && b && c && !d && !e) {
            return Brailles[39];
        } else if (!a && !b && !c && d && !e) {
            return Brailles[40];
        } else if (a && !b && !c && d && !e) {
            return Brailles[41];
        } else if (!a && b && !c && d && !e) {
            return Brailles[42];
        } else if (a && b && !c && d && !e) {
            return Brailles[43];
        } else if (!a && !b && c && d && !e) {
            return Brailles[44];
        } else if (a && !b && c && d && !e) {
            return Brailles[45];
        } else if (!a && b && c && d && !e) {
            return Brailles[46];
        } else if (a && b && c && d && !e) {
            return Brailles[47];
        } else if (!a && !b && !c && !d) {
            return Brailles[48];
        } else if (a && !b && !c && !d) {
            return Brailles[49];
        } else if (!a && b && !c && !d) {
            return Brailles[50];
        } else if (a && b && !c && !d) {
            return Brailles[51];
        } else if (!a && !b && c && !d) {
            return Brailles[52];
        } else if (a && !b && c && !d) {
            return Brailles[53];
        } else if (!a && b && c && !d) {
            return Brailles[54];
        } else if (a && b && c && !d) {
            return Brailles[55];
        } else if (!a && !b && !c) {
            return Brailles[56];
        } else if (a && !b && !c) {
            return Brailles[57];
        } else if (!a && b && !c) {
            return Brailles[58];
        } else if (a && b && !c) {
            return Brailles[59];
        } else if (!a && !b) {
            return Brailles[60];
        } else if (a && !b) {
            return Brailles[61];
        } else if (!a) {
            return Brailles[62];
        } else {
            return Brailles[63];
        }
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