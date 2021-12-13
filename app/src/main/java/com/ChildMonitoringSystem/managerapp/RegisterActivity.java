package com.ChildMonitoringSystem.managerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.models.CreateAccount;
import com.ChildMonitoringSystem.managerapp.models.User;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
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
    private FirebaseAuth mAuth;
    private String virifyCodeID;

    private  Dialog dialogVirifyOTP;
    private ImageView idIVBackGetOTP;
    private TextView idTVMobile, idTVResendOTP;
    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private Button idBTVerify;
    private String dateCreate, birthDate, name, phoneNumber, pass, confimPass, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(this);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        edt_Name = findViewById(R.id.edt_Name);
        edt_Phone = findViewById(R.id.edt_PhoneRegister);
        edt_Pass = findViewById(R.id.edt_PassRegister);
        edt_ConfimPass = findViewById(R.id.edt_ConfimPass);
        edt_Email = findViewById(R.id.edt_Email);

        idIVBackLogin = findViewById(R.id.idIVBackLogin);

        btn_Register = findViewById(R.id.btn_Register);
        backSignIn();
        createAccount();
    }

    private void backSignIn() {
        idIVBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createAccount() {
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edt_Name.getText().toString().trim();
                phoneNumber = edt_Phone.getText().toString().trim();
                pass = edt_Pass.getText().toString().trim();
                email = edt_Email.getText().toString().trim();
                dateCreate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                birthDate = "";
                phoneNumber = edt_Phone.getText().toString().trim();
                OpenDialog(Gravity.CENTER, dialog);
                if (!checkEditText()) {
                    CancleDialog(dialog);
                } else {
                    checkUser();
                }
            }
        });
    }

    @NonNull
    private Boolean checkEditText() {
        name = edt_Name.getText().toString().trim();
        phoneNumber = edt_Phone.getText().toString().trim();
        pass = edt_Pass.getText().toString().trim();
        confimPass = edt_ConfimPass.getText().toString().trim();
        email = edt_Email.getText().toString().trim();
        if (name.isEmpty()) {
            edt_Name.setError("Tên người dùng trống.");
            edt_Name.requestFocus();
            return false;
        }
        if (phoneNumber.isEmpty()) {
            edt_Phone.setError("Số điện thoại người dùng trống.");
            edt_Phone.requestFocus();
            return false;
        }
        if (phoneNumber.length() < 10) {
            edt_Phone.setError("Số điện thoại không được dưới 10 chữ số.");
            edt_Phone.requestFocus();
            return false;
        }
        if (phoneNumber.length() > 10) {
            edt_Phone.setError("Số điện thoại không vượt quá 10 chữ số.");
            edt_Phone.requestFocus();
            return false;
        }
        if (!CheckPhoneNumbers(phoneNumber)) {
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

    private void checkUser() {
        APIClient.getUserService().getUser(phoneNumber).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        CancleDialog(dialog);
                        Toast.makeText(getApplicationContext(), "Số điện thoại này đã được đăng ký.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d("TAG", "onResponse: "+phoneNumber + "Chưa có sđt");
                    sendOTP(phoneNumber);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void sendOTP(String num) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + num)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(RegisterActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            CancleDialog(dialog);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("TAG", "onVerificationFailed: " + e.getMessage());
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            CancleDialog(dialog);
            virifyCodeID = s;
            opendiglog();
        }
    };

    private void opendiglog() {
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_dialog_virify_code, null);
        dialogVirifyOTP = new Dialog(this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialogVirifyOTP.setContentView(viewDialog);
        Window window = dialogVirifyOTP.getWindow();
        if (window == null)return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        idIVBackGetOTP=dialogVirifyOTP.findViewById(R.id.idIVBackGetOTP);

        idTVMobile=dialogVirifyOTP.findViewById(R.id.idTVMobile);
        idTVResendOTP=dialogVirifyOTP.findViewById(R.id.idTVResendOTP);

        inputCode1=dialogVirifyOTP.findViewById(R.id.inputCode1);
        inputCode2=dialogVirifyOTP.findViewById(R.id.inputCode2);
        inputCode3=dialogVirifyOTP.findViewById(R.id.inputCode3);
        inputCode4=dialogVirifyOTP.findViewById(R.id.inputCode4);
        inputCode5=dialogVirifyOTP.findViewById(R.id.inputCode5);
        inputCode6=dialogVirifyOTP.findViewById(R.id.inputCode6);

        idBTVerify=dialogVirifyOTP.findViewById(R.id.idBTVerify);

        idTVMobile.setText(phoneNumber);
        backToGetOTP();

        setUpOTPInputs();

        virifyOTPCode();

        sendOTPAgain();

        dialogVirifyOTP.show();
    }

    private void backToGetOTP() {
        idIVBackGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogVirifyOTP.dismiss();
            }
        });
    }

    private void virifyOTPCode() {
        idBTVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog(Gravity.CENTER, dialog);
                if (inputCode1.getText().toString().trim().isEmpty()
                        || inputCode2.getText().toString().trim().isEmpty()
                        || inputCode3.getText().toString().trim().isEmpty()
                        || inputCode4.getText().toString().trim().isEmpty()
                        || inputCode5.getText().toString().trim().isEmpty()
                        || inputCode6.getText().toString().trim().isEmpty()) {
                    CancleDialog(dialog);
                    Toast.makeText(RegisterActivity.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                }
                String code = inputCode1.getText().toString()
                        + inputCode2.getText().toString()
                        + inputCode3.getText().toString()
                        + inputCode4.getText().toString()
                        + inputCode5.getText().toString()
                        + inputCode6.getText().toString();
                if (virifyCodeID != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(virifyCodeID, code);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Acount();
                        } else {
                            CancleDialog(dialog);
                            Toast.makeText(RegisterActivity.this, "Mã xác nhận không chính xác.\n Vui lòng nhập lại mã !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void Acount() {
        CreateAccount createAccount = new CreateAccount(phoneNumber, pass, name, birthDate, dateCreate, email);
        APIClient.getUserService().createUser(createAccount).enqueue(new Callback<CreateAccount>() {
            @Override
            public void onResponse(Call<CreateAccount> call, Response<CreateAccount> response) {
                if (response.isSuccessful()) {
                    CancleDialog(dialog);
                    dialogVirifyOTP.dismiss();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1500);
                } else {
                    CancleDialog(dialog);
                    Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản không thành công.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateAccount> call, Throwable t) {

            }
        });
    }

    private void sendOTPAgain() {
        idTVResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialog(Gravity.CENTER,dialog);
                phoneNumber = edt_Phone.getText().toString().trim();
                sendOTP(phoneNumber);
            }
        });
    }

    private void setUpOTPInputs() {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
}