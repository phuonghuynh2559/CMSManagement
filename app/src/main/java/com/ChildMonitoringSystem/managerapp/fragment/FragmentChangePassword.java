package com.ChildMonitoringSystem.managerapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.ChildMonitoringSystem.managerapp.LoginActivity;
import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.OTPActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.models.UserRequest;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChangePassword extends Fragment {
    private View mView;
    private EditText idETNewPass,idETConfimPass;
    private Button idBTChangeNewPass;
    private ImageView idIVBackMenu;
    private MainActivity mMainActivity;
    private TextView idTVPhoneNumber;
    private String phoneNumber;
    private Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        mView  = inflater.inflate(R.layout.fragment_change_password, container,false);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Quản lý tài khoản");
        idTVPhoneNumber = mView.findViewById(R.id.idTVPhoneNumber);
        idETNewPass = mView.findViewById(R.id.idETNewPass);
        idETConfimPass = mView.findViewById(R.id.idETConfimPass);
        idBTChangeNewPass = mView.findViewById(R.id.idBTChangeNewPass);
        idIVBackMenu = mView.findViewById(R.id.idIVBackMenu);

        phoneNumber = mMainActivity.getPhoneNumber();

        idTVPhoneNumber.setText(phoneNumber);
        changeNewPassWord();
        gotoFramentMenu();
        return mView;
    }

    private void gotoFramentMenu() {

        idIVBackMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentMenu fragmentMenu = new FragmentMenu();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentMenu);
                fragmentTransaction.commit();
            }
        });
    }

    private void changeNewPassWord(){

        idBTChangeNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CustomProgess.OpenDialog(Gravity.CENTER,dialog);
                String newPass = idETNewPass.getText().toString().trim();
                if (!checkEdittex()){
                    CustomProgess.CancleDialog(dialog);
                }
                UserRequest userRequest = new UserRequest(phoneNumber,newPass);
                APIClient.getUserService().changePass(userRequest).enqueue(new Callback<UserRequest>() {
                    @Override
                    public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getContext(), "Đổi thành công", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d("TAG", "onResponse: "+userRequest.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRequest> call, Throwable t) {

                    }
                });
            }
        });

    }
    private Boolean checkEdittex(){
        String newPass = idETNewPass.getText().toString().trim();
        String confimPass = idETConfimPass.getText().toString().trim();
        if (newPass.isEmpty()) {
            idETNewPass.setError("Bạn chưa điền thông tin.");
            idETNewPass.requestFocus();
            return false;
        }
        if (confimPass.isEmpty()) {
            idETConfimPass.setError("Bạn chưa điền thông tin.");
            idETConfimPass.requestFocus();
            return false;
        }
        if (!confimPass.equals(newPass)){
            idETConfimPass.setError("Xác nhận mật khẩu không đúng.");
            idETConfimPass.requestFocus();
            return false;
        }
        return true;
    }
}
