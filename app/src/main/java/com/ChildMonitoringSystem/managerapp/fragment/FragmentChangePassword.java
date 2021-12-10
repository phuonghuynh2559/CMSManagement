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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.LoginActivity;
import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.OTPActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.HistoryAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.models.HistorySignin;
import com.ChildMonitoringSystem.managerapp.models.UserRequest;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChangePassword extends Fragment {
    private View mView;
    private EditText idETNewPass,idETConfimPass;
    private Button idBTChangeNewPass, idBTOpenLayoutChangePass,idBTOpenLayoutHistory;
    private ImageView idIVBackMenu,idIVNoData;
    private MainActivity mMainActivity;
    private TextView idTVPhoneNumber;
    private MyShareReference myShareReference;
    private LinearLayout idLLChangePass, idLLLoginHistory;
    private RecyclerView idRCVHistory;
    private String phoneNumber;
    private Dialog dialog;
    private HistoryAdapter historyAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        mView  = inflater.inflate(R.layout.fragment_change_password, container,false);
        myShareReference = new MyShareReference(getContext());
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Quản lý tài khoản");

        idTVPhoneNumber = mView.findViewById(R.id.idTVPhoneNumber);
        idETNewPass = mView.findViewById(R.id.idETNewPass);
        idETConfimPass = mView.findViewById(R.id.idETConfimPass);
        idBTChangeNewPass = mView.findViewById(R.id.idBTChangeNewPass);
        idIVBackMenu = mView.findViewById(R.id.idIVBackMenu);
        idIVNoData = mView.findViewById(R.id.idIVNoData);
        idBTOpenLayoutChangePass = mView.findViewById(R.id.idBTOpenLayoutChangePass);
        idBTOpenLayoutHistory = mView.findViewById(R.id.idBTOpenLayoutHistory);
        idLLChangePass = mView.findViewById(R.id.idLLChangePass);
        idLLLoginHistory = mView.findViewById(R.id.idLLLoginHistory);
        idRCVHistory = mView.findViewById(R.id.idRCVHistory);

        phoneNumber = myShareReference.getValueString("phoneNumber");

        historyAdapter = new HistoryAdapter(getContext());
        GridLayoutManager gridLayoutCall = new GridLayoutManager(getContext(), 1);
        idRCVHistory.setLayoutManager(gridLayoutCall);

        idLLChangePass.setVisibility(View.INVISIBLE);
        idLLLoginHistory.setVisibility(View.INVISIBLE);
        openLayoutChangePass();
        openLayoutHistory();
        gotoFramentMenu();
        return mView;
    }

    private void openLayoutChangePass() {
        idBTOpenLayoutChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idLLChangePass.setVisibility(View.VISIBLE);
                idLLLoginHistory.setVisibility(View.INVISIBLE);
                idTVPhoneNumber.setText(phoneNumber);
                changeNewPassWord();
            }
        });
    }

    private void openLayoutHistory() {
        idBTOpenLayoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomProgess.OpenDialog(Gravity.CENTER,dialog);
                idLLChangePass.setVisibility(View.INVISIBLE);
                idLLLoginHistory.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLoginHistory();
                    }
                },1500);

            }
        });
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
    public void getLoginHistory(){
        APIClient.getUserService().getLoginHistory(phoneNumber).enqueue(new Callback<List<HistorySignin>>() {
            @Override
            public void onResponse(Call<List<HistorySignin>> call, Response<List<HistorySignin>> response) {
                if (response.isSuccessful()){
                    List<HistorySignin> mList = response.body();
                    if (mList.size()==0){
                        CustomProgess.CancleDialog(dialog);
                        idIVNoData.setVisibility(View.VISIBLE);
                        idRCVHistory.setVisibility(View.INVISIBLE);
                    }else{
                        CustomProgess.CancleDialog(dialog);
                        idIVNoData.setVisibility(View.INVISIBLE);
                        idRCVHistory.setVisibility(View.VISIBLE);
                        historyAdapter.setData(mList);
                        idRCVHistory.setAdapter(historyAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HistorySignin>> call, Throwable t) {

            }
        });
    }
}
