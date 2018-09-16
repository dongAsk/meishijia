package com.example.dong.meishi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dong.meishi.httpUtil.HttpUtil;
import com.example.dong.meishi.httpUtil.ParseJSON;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_account;
    private EditText edit_password;
    private CheckBox chk_remember_pass;
    private Button btn_login;
    private Button btn_cancle;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ImageView bingPicImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.layout_login);

        bingPicImg = findViewById(R.id.bing_pic_img);
        edit_account = findViewById(R.id.edit_account);
        edit_password = findViewById(R.id.edit_password);
        chk_remember_pass = findViewById(R.id.chk_rememberPass);
        btn_login = findViewById(R.id.btn_login);
        btn_cancle = findViewById(R.id.btn_cancle);
        btn_login.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = pref.getString("bing_pic",null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }


        boolean isremember = pref.getBoolean("remember_password",false);
        if (isremember){
            String username = pref.getString("account","");
            String password = pref.getString("password","");
            edit_account.setText(username);
            edit_password.setText(password);
            chk_remember_pass.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                final String username = edit_account.getText().toString();
                final String password = edit_password.getText().toString();
//////////////////////////////////////////////////////////////////////////////////////////
                String address = "http://47.93.252.249/login.php";
                HttpUtil.sendHttpWithOkhttp_POST(address, username, password, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        final boolean result = ParseJSON.parseJSONWithJSONObject(responseData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result){
                                    editor = pref.edit();
                                    if (chk_remember_pass.isChecked()){
                                        editor.putBoolean("remember_password",true);
                                        editor.putString("account",username);
                                        editor.putString("password",password);
                                    }else {
                                        editor.clear();
                                    }
                                    editor.apply();
                                    //convert activity
                                    Toast.makeText(LoginActivity.this,"successed",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                break;
//////////////////////////////////////////////////////////////////////////////////////////
//
//                if ("admin".equals(username) && "123456".equals(password)){
//                    editor = pref.edit();
//                    if (chk_remember_pass.isChecked()){
//                        editor.putBoolean("remember_password",true);
//                        editor.putString("account",username);
//                        editor.putString("password",password);
//                    }else {
//                        editor.clear();
//                    }
//                    editor.apply();
//                    //convert activity
//                    Toast.makeText(LoginActivity.this,"successed",Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                    startActivity(intent);
//
//                }else{
//                    Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
//                }
//                break;
/////////////////////////////////////////////////////////////////////////////////////////////
            case R.id.btn_cancle:
                finish();
        }
    }

    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendHttpWithOkhttp(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(LoginActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
