package com.ChildMonitoringSystem.managerapp.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.CallLogAdapter;
import com.ChildMonitoringSystem.managerapp.adapter.InfomationPhoneAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.HistoryCall;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickHistoryCall;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickInfomationPhone;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCallLog extends Fragment {
    private View mView;
    private RecyclerView rcv_CallLog, rcv_InfoPhone;
    private CallLogAdapter callLogAdapter;
    private InfomationPhoneAdapter infomationPhoneAdapter;
    private ImageView btnBack;

    private MainActivity mMainActivity;
    private ImageView idIVNoData;
    private Dialog dialog;
    private MyShareReference myShareReference;
    private String phoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_history_call, container, false);

        myShareReference = new MyShareReference(getContext());
        phoneNumber = myShareReference.getValueString("phoneNumber");

        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Xem lịch sử cuộc gọi");

        rcv_CallLog = mView.findViewById(R.id.rcv_history_call);
        rcv_InfoPhone = mView.findViewById(R.id.rcv_InfoPhone);
        btnBack = mView.findViewById(R.id.btnBack);
        idIVNoData = mView.findViewById(R.id.idIVNoData);

        infomationPhoneAdapter = new InfomationPhoneAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_InfoPhone.setLayoutManager(gridLayoutManager);
        loadListPhoneMonitor(phoneNumber);

        callLogAdapter = new CallLogAdapter(getContext());
        GridLayoutManager gridLayoutCall = new GridLayoutManager(getContext(), 1);
        rcv_CallLog.setLayoutManager(gridLayoutCall);

        goToFragmentMenu();
        return mView;
    }

    private void loadListPhoneMonitor(String phoneNumber) {
        APIClient.getUserService().getListInfoPhone(phoneNumber).enqueue(new Callback<List<InfomationPhone>>() {
            @Override
            public void onResponse(Call<List<InfomationPhone>> call, Response<List<InfomationPhone>> response) {
                if (response.isSuccessful()) {
                    List<InfomationPhone> mList = response.body();
                    if (mList.size() == 0) {
                        Toast.makeText(getContext(), "Không có máy giám sát nào!", Toast.LENGTH_SHORT).show();
                    } else {
                        infomationPhoneAdapter.setData(mList, new IClickInfomationPhone() {
                            @Override
                            public void onClickGoToMenu(InfomationPhone phone) {
                                CustomProgess.OpenDialog(Gravity.CENTER, dialog);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getCallLog(phone.getSERI_PHONE());
                                    }
                                }, 1000);
                            }

                        });
                        rcv_InfoPhone.setAdapter(infomationPhoneAdapter);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<InfomationPhone>> call, Throwable t) {

            }
        });
    }

    private void goToFragmentMenu() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCallLog(String seriPhone) {
        Call<List<HistoryCall>> listCall = APIClient.getUserService().getCallLog(seriPhone);
        listCall.enqueue(new Callback<List<HistoryCall>>() {
            @Override
            public void onResponse(Call<List<HistoryCall>> call, Response<List<HistoryCall>> response) {
                if (response.isSuccessful()) {
                    List<HistoryCall> list = response.body();
                    if (list.size() == 0) {
                        CustomProgess.CancleDialog(dialog);
                        idIVNoData.setVisibility(View.VISIBLE);
                        rcv_CallLog.setVisibility(View.INVISIBLE);
                    } else {
                        CustomProgess.CancleDialog(dialog);
                        callLogAdapter.setData(list, new IClickHistoryCall() {
                            @Override
                            public void onClickHistoryCall(HistoryCall historyCall) {
                                //openDialogHistoryCall(Gravity.CENTER,historyCall);
                            }
                        });
                        rcv_CallLog.setAdapter(callLogAdapter);
                        idIVNoData.setVisibility(View.INVISIBLE);
                        rcv_CallLog.setVisibility(View.VISIBLE);
                    }
                } else {
                    CustomProgess.CancleDialog(dialog);
                }
            }

            @Override
            public void onFailure(Call<List<HistoryCall>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null) {
            CustomProgess.CancleDialog(dialog);
        }
    }

    private void openDialogHistoryCall(int center, HistoryCall historyCall) {

    }
}
