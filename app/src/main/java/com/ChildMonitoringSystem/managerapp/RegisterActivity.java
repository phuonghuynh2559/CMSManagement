package com.ChildMonitoringSystem.managerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.models.CreateAccount;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends CustomProgess {
    private EditText edt_Name, edt_Phone, edt_Pass, edt_ConfimPass, edt_Email;
    private Button btn_Register;
    private ImageView idIVBackLogin;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(this);
        setContentView(R.layout.activity_register);
        edt_Name = findViewById(R.id.edt_Name);
        edt_Phone = findViewById(R.id.edt_PhoneRegister);
        edt_Pass = findViewById(R.id.edt_PassRegister);
        edt_ConfimPass = findViewById(R.id.edt_ConfimPass);
        edt_Email = findViewById(R.id.edt_Email);

        idIVBackLogin = findViewById(R.id.idIVBackLogin);

        idIVBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_Register = findViewById(R.id.btn_Register);
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog(Gravity.CENTER, dialog);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String name = edt_Name.getText().toString().trim();
                        String phone = edt_Phone.getText().toString().trim();
                        String pass = edt_Pass.getText().toString().trim();
                        String confimPass = edt_ConfimPass.getText().toString().trim();
                        String email = edt_Email.getText().toString().trim();
                        String dateCreate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                        String birthDate = "";
                        CreateAccount createAccount = new CreateAccount(phone,pass,name,birthDate,dateCreate,email);
                        if (!checkEditText()){
                            CancleDialog(dialog);
                        }else{
                            APIClient.getUserService().createUser(createAccount).enqueue(new Callback<CreateAccount>() {
                                @Override
                                public void onResponse(Call<CreateAccount> call, Response<CreateAccount> response) {
                                    if (response.isSuccessful()){
                                        CancleDialog(dialog);
                                        Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        CancleDialog(dialog);
                                        Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản không thành công.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CreateAccount> call, Throwable t) {

                                }
                            });
                        }
                    }
                }, 2000);
            }
        });
    }
    private Boolean checkEditText() {
        String name = edt_Name.getText().toString().trim();
        String phone = edt_Phone.getText().toString().trim();
        String pass = edt_Pass.getText().toString().trim();
        String confimPass = edt_ConfimPass.getText().toString().trim();
        String email = edt_Email.getText().toString().trim();
        if (name.isEmpty()) {
            edt_Name.setError("Tên người dùng trống.");
            edt_Name.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
            edt_Phone.setError("Số điện thoại người dùng trống.");
            edt_Phone.requestFocus();
            return false;
        }
        if (phone.length() < 10) {
            edt_Phone.setError("Số điện thoại không được dưới 10 chữ số.");
            edt_Phone.requestFocus();
            return false;
        }
        if (phone.length() > 10) {
            edt_Phone.setError("Số điện thoại không vượt quá 10 chữ số.");
            edt_Phone.requestFocus();
            return false;
        }
        if (!CheckPhoneNumbers(phone)) {
            edt_Phone.setError("Số điện thoại không tồn tại.");
            edt_Phone.requestFocus();
            return false;
        }
        if (pass.length() < 5) {
            edt_Pass.setError("Mật khẩu của người dùng phải lớn hơn 5 ký tự.");
            edt_Pass.requestFocus();
            return false;
        }
        if (pass.isEmpty()) {
            edt_Pass.setError("Mật khẩu người dùng trống.");
            edt_Pass.requestFocus();
            return false;
        }
        if (!confimPass.equals(pass)) {
            edt_ConfimPass.setError("Xác nhận mật khẩu không đúng.");
            edt_ConfimPass.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            edt_Email.setError("Email người dùng trống");
            edt_Email.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_Email.setError("Email người dùng không hợp lệ");
            edt_Email.requestFocus();
            return false;
        }
        return true;
    }
    private boolean CheckPhoneNumbers(String num) {
        boolean check = Pattern.matches("^[0][[3]|[7]|[8]|[9]]{1}[1-9]{1}[0-9]{7}$", num);
        return check;
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
}