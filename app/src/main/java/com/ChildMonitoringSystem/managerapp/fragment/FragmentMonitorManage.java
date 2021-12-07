package com.ChildMonitoringSystem.managerapp.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentMonitorManage extends Fragment {
    private View mView;
    private MainActivity mMainActivity;
    private TextView idTVNameSpy,idTVModel,idTVBrand,idTVProduct;
    private Button idBTCancelMonitor;
    private LinearLayout idLLShowInfomation;
    private ImageButton idIBBackMenu;
    private Dialog dialog ;
    public FragmentMonitorManage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_monitor_manage, container, false);
        mMainActivity=(MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Quản Lý Thiết Bị Giám Sát");
        idTVNameSpy = mView.findViewById(R.id.idTVNameSpy);
        idTVModel = mView.findViewById(R.id.idTVModel);
        idTVBrand = mView.findViewById(R.id.idTVBrand);
        idTVProduct = mView.findViewById(R.id.idTVProduct);
        idBTCancelMonitor = mView.findViewById(R.id.idBTCancelMonitor);
        idIBBackMenu = mView.findViewById(R.id.idIBBackMenu);
        idLLShowInfomation = mView.findViewById(R.id.idLLShowInfomation);
        loadFrameLayout();
        goToFragmentMenu();
        idBTCancelMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Chức Năng Đang Phát Triển!", Toast.LENGTH_SHORT).show();
            }
        });
        return mView;
    }
    private BroadcastReceiver seriPhone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constan.Action.equals(intent.getAction())){
                String seri = intent.getStringExtra("seriPhone");
                showInfomationPhone(seri);
            }
        }
    };

    private void showInfomationPhone(String seri) {
        APIClient.getUserService().getInfo(seri).enqueue(new Callback<InfomationPhone>() {
            @Override
            public void onResponse(Call<InfomationPhone> call, Response<InfomationPhone> response) {
                if (response.isSuccessful()){
                    InfomationPhone info = response.body();
                    if (info == null){

                    }else{

                    }
                }
            }

            @Override
            public void onFailure(Call<InfomationPhone> call, Throwable t) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constan.Action);
        requireActivity().registerReceiver(seriPhone,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(seriPhone);
    }
    private void loadFrameLayout(){
        FragmentInfomationPhone infomationPhone = new FragmentInfomationPhone();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayoutMonitorManage,infomationPhone);
        fragmentTransaction.commit();
    }
    private void goToFragmentMenu() {
        idIBBackMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMenu fragmentMenu = new FragmentMenu();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutMenu, fragmentMenu);
                fragmentTransaction.commit();
            }
        });
    }
}