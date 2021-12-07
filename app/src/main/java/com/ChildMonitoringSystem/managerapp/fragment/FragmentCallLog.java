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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.CallLogAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.HistoryCall;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickHistoryCall;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCallLog extends Fragment {
    private View mView;
    private RecyclerView rcv_CallLog;
    private CallLogAdapter callLogAdapter;
    private ImageButton btnBack;

    private MainActivity mMainActivity;
    private ImageView idIVNoData;
    private Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_history_call, container,false);
        rcv_CallLog = mView.findViewById(R.id.rcv_history_call);
        mMainActivity = (MainActivity)getActivity();
        mMainActivity.getToolbar().setTitle("Xem lịch sử cuộc gọi");

        btnBack =mView.findViewById(R.id.btnBack);
        idIVNoData =mView.findViewById(R.id.idIVNoData);

        callLogAdapter = new CallLogAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        rcv_CallLog.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcv_CallLog.addItemDecoration(itemDecoration);
        loadFrameLayout();
        goToFragmentMenu();
        return mView;
    }
    private void loadFrameLayout(){
        FragmentInfomationPhone infomationPhone = new FragmentInfomationPhone();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayoutImage,infomationPhone);
        fragmentTransaction.commit();
    }
    private void goToFragmentMenu() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMenu fragmentMenu = new FragmentMenu();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentMenu);
                fragmentTransaction.commit();
            }
        });
    }
    private BroadcastReceiver seriPhone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constan.Action.equals(intent.getAction())){
                String seri = intent.getStringExtra("seriPhone");
                CustomProgess.OpenDialog(Gravity.CENTER,dialog);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCallLog(seri);
                    }
                },1000);

            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(Constan.Action);
        requireActivity().registerReceiver(seriPhone,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(seriPhone);
    }
    private void getCallLog(String seriPhone) {
        Call<List<HistoryCall>> listCall = APIClient.getUserService().getCallLog(seriPhone);
        listCall.enqueue(new Callback<List<HistoryCall>>() {
            @Override
            public void onResponse(Call<List<HistoryCall>> call, Response<List<HistoryCall>> response) {
                if (response.isSuccessful()){
                    List<HistoryCall> list = response.body();
                    if (list.size()==0){
                        CustomProgess.CancleDialog(dialog);
                        idIVNoData.setVisibility(View.VISIBLE);
                        rcv_CallLog.setVisibility(View.INVISIBLE);
                    }else{
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
                }else {
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
