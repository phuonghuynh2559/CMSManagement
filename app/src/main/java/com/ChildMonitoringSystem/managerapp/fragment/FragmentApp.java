package com.ChildMonitoringSystem.managerapp.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.AppAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.App;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentApp extends Fragment {
    private View mView;
    private RecyclerView rcvRecyclerView;
    private AppAdapter appAdapter;
    private ImageView btnBack;
    private MainActivity mMainActivity;
    private ImageView idIVNoData;
    private Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_app, container,false);
        rcvRecyclerView = mView.findViewById(R.id.rcv_app);
        mMainActivity = (MainActivity)getActivity();
        mMainActivity.getToolbar().setTitle("Xem ứng dụng được cài đặt");
        btnBack =mView.findViewById(R.id.btnBack);
        idIVNoData =mView.findViewById(R.id.idIVNoData);
        appAdapter = new AppAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        rcvRecyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvRecyclerView.addItemDecoration(itemDecoration);
        loadFrameLayout();
        goToFragmentMenu();
        return mView;
    }
    private void loadFrameLayout(){
        FragmentInfomationPhone infomationPhone = new FragmentInfomationPhone();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayoutApp,infomationPhone);
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
                        getApp(seri);
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
    private void getApp(String seri) {
        Call<List<App>> getListApp = APIClient.getUserService().getListApp(seri);
        getListApp.enqueue(new Callback<List<App>>() {
            @Override
            public void onResponse(Call<List<App>> call, Response<List<App>> response) {
                if (response.isSuccessful()){
                    List<App>list = response.body();
                    if (list.size()==0){
                        CustomProgess.CancleDialog(dialog);
                        idIVNoData.setVisibility(View.VISIBLE);
                        rcvRecyclerView.setVisibility(View.INVISIBLE);
                    }
                    CustomProgess.CancleDialog(dialog);
                    appAdapter.setData(list);
                    rcvRecyclerView.setAdapter(appAdapter);
                    idIVNoData.setVisibility(View.INVISIBLE);
                    rcvRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    CustomProgess.CancleDialog(dialog);
                    Toast.makeText(getContext(),"Không có kết nối với máy chủ.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<App>> call, Throwable t) {

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
}
