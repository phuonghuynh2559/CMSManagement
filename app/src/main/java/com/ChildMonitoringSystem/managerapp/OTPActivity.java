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
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.models.UserRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {
//  OTP Main
    private String phoneNumber;
    private FirebaseAuth mAuth;
    private EditText inputMobile;
    private Button buttonGetOTP;
    private ProgressBar progressBar;
    private String virifyCodeID;

    // Virify otp
    private ImageView btnBackLogin, idIVBackGetOTP;
    private TextView idTVMobile,idTVResendOTP;
    private EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    private Button idBTVerify;
    private ProgressBar idPBVirifyOTP;
    //Change passsword
    private TextView idTVPhoneNumber;
    private EditText idETNewPass, idETConfimPass;
    private Button idBTChangeNewPass;

    private View viewDialog, viewDialogChangePass;
    private Dialog dialogVirifyOTP, dialogChangePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        mAuth = FirebaseAuth.getInstance();

        inputMobile = findViewById(R.id.inputMobile);
        buttonGetOTP = findViewById(R.id.buttonGetOTP);
        progressBar = findViewById(R.id.progressBar);
        btnBackLogin = findViewById(R.id.btnBackLogin);
        backLogin();
        getOTP();

    }
    private void backLogin(){
        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void getOTP(){
        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputMobile.getText().toString().trim().isEmpty()){
                    Toast.makeText(OTPActivity.this,"Nhập số điện thoại",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);
                buttonGetOTP.setVisibility(View.GONE);
                phoneNumber = inputMobile.getText().toString().trim();
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+84"+ phoneNumber)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(OTPActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            progressBar.setVisibility(View.GONE);
            buttonGetOTP.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("TAG", "onVerificationFailed: "+e.getMessage());
        }

        @Override
        public void onCodeSent(@NonNull  String s, @NonNull  PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressBar.setVisibility(View.GONE);
            buttonGetOTP.setVisibility(View.VISIBLE);
            virifyCodeID = s;
            opendiglog();
        }
    };
    private void opendiglog( ) {
        viewDialog = getLayoutInflater().inflate(R.layout.layout_dialog_virify_code, null);
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

        idPBVirifyOTP=dialogVirifyOTP.findViewById(R.id.idPBVirifyOTP);
        backToGetOTP();
        setUpOTPInputs();
        virifyOTPCode();
        getOTPCodeAgain();


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
    private void setUpOTPInputs(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
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
                if (!s.toString().trim().isEmpty()){
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
                if (!s.toString().trim().isEmpty()){
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
                if (!s.toString().trim().isEmpty()){
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
                if (!s.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void virifyOTPCode() {
        idBTVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCode1.getText().toString().trim().isEmpty()
                        ||inputCode2.getText().toString().trim().isEmpty()
                        ||inputCode3.getText().toString().trim().isEmpty()
                        ||inputCode4.getText().toString().trim().isEmpty()
                        ||inputCode5.getText().toString().trim().isEmpty()
                        ||inputCode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(OTPActivity.this,"Please enter valid code",Toast.LENGTH_SHORT).show();
                }
                String code = inputCode1.getText().toString()
                        +inputCode2.getText().toString()
                        +inputCode3.getText().toString()
                        +inputCode4.getText().toString()
                        +inputCode5.getText().toString()
                        +inputCode6.getText().toString();
                if (virifyCodeID!=null){
                    idPBVirifyOTP.setVisibility(View.VISIBLE);
                    idBTVerify.setVisibility(View.GONE);
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
                            openDialogChagePassword();
                        } else {
                            Toast.makeText(OTPActivity.this,"Mã xác nhận không chính xác.\n Vui lòng nhập lại mã !",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void openDialogChagePassword() {
        dialogVirifyOTP.dismiss();
        viewDialogChangePass = getLayoutInflater().inflate(R.layout.layout_dialog_forgot_pass, null);
        dialogChangePass = new Dialog(this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialogChangePass.setContentView(viewDialogChangePass);
        Window window = dialogChangePass.getWindow();
        if (window == null)return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        idTVPhoneNumber = dialogChangePass.findViewById(R.id.idTVPhoneNumber);

        idETNewPass = dialogChangePass.findViewById(R.id.idETNewPass);
        idETConfimPass = dialogChangePass.findViewById(R.id.idETConfimPass);

        idBTChangeNewPass = dialogChangePass.findViewById(R.id.idBTChangeNewPass);

        idTVPhoneNumber.setText(phoneNumber);

        changePassword();

        dialogChangePass.show();
    }

    private void changePassword() {
        idBTChangeNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = idETNewPass.getText().toString().trim();
                String confimPass = idETConfimPass.getText().toString().trim();
                if (newPass.isEmpty()){
                    idETNewPass.setError("Mật khẩu trống.");
                    idETNewPass.requestFocus();
                    return;
                }
                if (confimPass.isEmpty()){
                    idETConfimPass.setError("Xác nhận mật khẩu trống.");
                    idETConfimPass.requestFocus();
                    return;
                }
                if (!newPass.equals(confimPass)){
                    idETConfimPass.setError("Xác nhận mật khẩu không đúng.");
                    idETConfimPass.requestFocus();
                    return;
                }
                UserRequest userRequest = new UserRequest(phoneNumber,newPass);
                APIClient.getUserService().changePass(userRequest).enqueue(new Callback<UserRequest>() {
                    @Override
                    public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },2000);
                    }

                    @Override
                    public void onFailure(Call<UserRequest> call, Throwable t) {

                    }
                });
            }
        });

    }
    private void getOTPCodeAgain() {
        idTVResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = inputMobile.getText().toString().trim();
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+84"+ phoneNumber)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(OTPActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallBackAgain)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBackAgain = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("TAG", "onVerificationFailed: "+e.getMessage());
        }
        @Override
        public void onCodeSent(@NonNull  String NewVerificationID, @NonNull  PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(NewVerificationID, forceResendingToken);
            progressBar.setVisibility(View.GONE);
            buttonGetOTP.setVisibility(View.VISIBLE);
            virifyCodeID = NewVerificationID;
        }
    };
}