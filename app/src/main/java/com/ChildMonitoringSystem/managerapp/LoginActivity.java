package com.ChildMonitoringSystem.managerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.models.HistoryCall;
import com.ChildMonitoringSystem.managerapp.models.HistorySignin;
import com.ChildMonitoringSystem.managerapp.models.User;
import com.ChildMonitoringSystem.managerapp.models.UserRequest;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Connection;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends CustomProgess {
    private EditText edt_Phone, edt_Pass;
    private LinearLayout idLLRegister;
    private Button btn_Login;
    private TextView tvForgotPassword;
    private Dialog dialog;

    private MyShareReference myShareReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(this);

        myShareReference = new MyShareReference(getApplicationContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        edt_Phone = findViewById(R.id.edit_Phone);
        edt_Pass = findViewById(R.id.edit_Pass);
        idLLRegister = findViewById(R.id.idLLRegister);
        tvForgotPassword = findViewById(R.id.textviewForgotPassword);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToForgotActivity();
            }
        });
        btn_Login = findViewById(R.id.btn_Login);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_Phone.getText())) {
                    edt_Phone.setError("Chưa điền số điện thoại!");
                } else {
                    if (TextUtils.isEmpty(edt_Pass.getText())){
                        edt_Pass.setError("Chưa điền mật khẩu!");
                    }else{
                        OpenDialog(Gravity.CENTER, dialog);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                login();
                            }
                        },2000);
                    }
                }
            }
        });
        idLLRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void goToForgotActivity() {
        Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
        startActivity(intent);
        finish();
    }

    public void login() {
        UserRequest userRequest = new UserRequest();
        userRequest.setPHONE_NUMBERS(edt_Phone.getText().toString());
        userRequest.setPASSWORD_USERS(edt_Pass.getText().toString());
        String phoneNumber = edt_Phone.getText().toString();
        Call<User> loginRespont = APIClient.getUserService().userLogin(userRequest);
        loginRespont.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    assert user != null;
                    loginHistory(user.getPHONE_NUMBERS());
                    CancleDialog(dialog);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("data", user));
                    myShareReference.putValueString("phoneNumber",phoneNumber);
                    finish();

                } else {
                    CancleDialog(dialog);
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                CancleDialog(dialog);
            }
        });
    }

    public void loginHistory(String phone) {
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        String model = Build.MODEL;
        String brand = Build.BRAND;
        String text ="Thiết bị "+ brand+" "+ model;
        HistorySignin historySignin = new HistorySignin(phone, date, text);
        APIClient.getUserService().HistorySignIN(historySignin).enqueue(new Callback<HistorySignin>() {
            @Override
            public void onResponse(Call<HistorySignin> call, Response<HistorySignin> response) {
            }

            @Override
            public void onFailure(Call<HistorySignin> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            CancleDialog(dialog);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            CancleDialog(dialog);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}