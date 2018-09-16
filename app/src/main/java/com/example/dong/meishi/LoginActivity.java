package com.example.dong.meishi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_account;
    private EditText edit_password;
    private CheckBox chk_remember_pass;
    private Button btn_login;
    private Button btn_cancle;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        edit_account = findViewById(R.id.edit_account);
        edit_password = findViewById(R.id.edit_password);
        chk_remember_pass = findViewById(R.id.chk_rememberPass);
        btn_login = findViewById(R.id.btn_login);
        btn_cancle = findViewById(R.id.btn_cancle);
        btn_login.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isremember = pref.getBoolean("remember_password",false);
        if (isremember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            edit_account.setText(account);
            edit_password.setText(password);
            chk_remember_pass.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                String account = edit_account.getText().toString();
                String password = edit_password.getText().toString();
                if ("admin".equals(account) && "123456".equals(password)){
                    editor = pref.edit();
                    if (chk_remember_pass.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",account);
                        editor.putString("password",password);
                    }else {
                        editor.clear();
                    }
                    editor.apply();
                    //convert activity
                    Toast.makeText(LoginActivity.this,"successed",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancle:
                finish();
        }
    }
}
