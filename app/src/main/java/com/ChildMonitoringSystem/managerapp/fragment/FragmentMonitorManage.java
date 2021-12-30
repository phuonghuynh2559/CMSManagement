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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.InfomationPhoneAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickInfomationPhone;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentMonitorManage extends Fragment {
    private View mView;
    private MainActivity mMainActivity;
    private TextView idTVNameSpy,idTVModel,idTVBrand,idTVProduct, idTVDateMonitor,idTVPERMISSION_DENIED;
    private RelativeLayout idLLShowInfomation;
    private ImageView idIBBackMenu, idIVNoData;
    private RecyclerView rcv_MonitorManager;
    private Dialog dialog ;
    private InfomationPhoneAdapter infomationPhoneAdapter;
    private MyShareReference myShareReference;
    private String phoneNumber;
    public FragmentMonitorManage() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_monitor_manage, container, false);
        myShareReference = new MyShareReference(getContext());
        phoneNumber = myShareReference.getValueString("phoneNumber");

        mMainActivity=(MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Quản Lý Thiết Bị Giám Sát");

        idTVNameSpy = mView.findViewById(R.id.idTVNameSpy);
        idTVModel = mView.findViewById(R.id.idTVModel);
        idTVBrand = mView.findViewById(R.id.idTVBrand);
        idTVProduct = mView.findViewById(R.id.idTVProduct);
        idTVDateMonitor = mView.findViewById(R.id.idTVDateMonitor);
        idTVPERMISSION_DENIED = mView.findViewById(R.id.idTVPERMISSION_DENIED);
        rcv_MonitorManager = mView.findViewById(R.id.rcv_MonitorManager);

        idIBBackMenu = mView.findViewById(R.id.idIBBackMenu);
        idIVNoData = mView.findViewById(R.id.idIVNoData);
        idLLShowInfomation = mView.findViewById(R.id.idLLShowInfomation);
        infomationPhoneAdapter = new InfomationPhoneAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_MonitorManager.setLayoutManager(gridLayoutManager);
        loadListPhoneMonitor(phoneNumber);
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
                                        getInfomationPhone(phone.getSERI_PHONE());
                                    }
                                }, 1000);
                            }

                        });
                        rcv_MonitorManager.setAdapter(infomationPhoneAdapter);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<InfomationPhone>> call, Throwable t) {

            }
        });
    }

    private void getInfomationPhone(String seri_phone) {
        APIClient.getUserService().getInfomationPhone(seri_phone).enqueue(new Callback<InfomationPhone>() {
            @Override
            public void onResponse(Call<InfomationPhone> call, Response<InfomationPhone> response) {
                if (response.isSuccessful()){
                    InfomationPhone infomationPhone = response.body();
                    if (infomationPhone == null){
                        CustomProgess.CancleDialog(dialog);
                        idIVNoData.setVisibility(View.VISIBLE);
                        idLLShowInfomation.setVisibility(View.GONE);
                    }else{
                        CustomProgess.CancleDialog(dialog);
                        idIVNoData.setVisibility(View.GONE);
                        idLLShowInfomation.setVisibility(View.VISIBLE);
                        idTVNameSpy.setText(infomationPhone.getNAME_SPY());
                        idTVModel.setText(infomationPhone.getMODEL());
                        idTVBrand.setText(infomationPhone.getBRAND());
                        idTVProduct.setText(infomationPhone.getPRODUCT());
                        idTVDateMonitor.setText(infomationPhone.getDATE_SPY());
                        idTVPERMISSION_DENIED.setText(infomationPhone.getPERMISSION_DENIED());
                    }
                }
            }

            @Override
            public void onFailure(Call<InfomationPhone> call, Throwable t) {

            }
        });
    }
    private void ShowInfomationPhone(InfomationPhone infomationPhone){
        idTVNameSpy.setText(infomationPhone.getNAME_SPY());
        idTVModel.setText(infomationPhone.getMODEL());
        idTVBrand.setText(infomationPhone.getBRAND());
        idTVProduct.setText(infomationPhone.getPRODUCT());
        idTVDateMonitor.setText(infomationPhone.getDATE_SPY());
        idTVPERMISSION_DENIED.setText(infomationPhone.getPERMISSION_DENIED());
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