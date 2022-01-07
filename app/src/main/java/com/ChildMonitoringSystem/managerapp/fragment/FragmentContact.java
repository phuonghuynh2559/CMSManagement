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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.ContactAdapter;
import com.ChildMonitoringSystem.managerapp.adapter.InfomationPhoneAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.Contact;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickInfomationPhone;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;
import com.ChildMonitoringSystem.managerapp.ui.NotifyProgess;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentContact extends NotifyProgess {
    private View view;
    private RecyclerView rcv_contact,rcv_InfoPhone;
    private ContactAdapter contactAdapter;
    private ImageView btnBack;
    private MainActivity mMainActivity;
    private ImageView idIVNoData;
    private Dialog dialog,dialogDownloadTool;
    private MyShareReference myShareReference;
    private String phoneNumber;
    private InfomationPhoneAdapter infomationPhoneAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        dialogDownloadTool = new Dialog(getContext());
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        myShareReference = new MyShareReference(getContext());
        phoneNumber = myShareReference.getValueString("phoneNumber");

        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Xem danh bạ điện thoại");

        rcv_contact = view.findViewById(R.id.rcv_contact);
        rcv_InfoPhone = view.findViewById(R.id.rcv_InfoPhone);

        btnBack = view.findViewById(R.id.btnBack);
        idIVNoData = view.findViewById(R.id.idIVNoData);

        infomationPhoneAdapter = new InfomationPhoneAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_InfoPhone.setLayoutManager(gridLayoutManager);
        loadListPhoneMonitor(phoneNumber);

        contactAdapter = new ContactAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_contact.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv_contact.addItemDecoration(itemDecoration);
        backFragmentBefore();
        return view;
    }

    private void loadListPhoneMonitor(String phoneNumber) {
        APIClient.getUserService().getListInfoPhone(phoneNumber).enqueue(new Callback<List<InfomationPhone>>() {
            @Override
            public void onResponse(Call<List<InfomationPhone>> call, Response<List<InfomationPhone>> response) {
                if (response.isSuccessful()){
                    List<InfomationPhone>mList = response.body();
                    if (mList.size()==0){
                        OpenDialogNotify(Gravity.CENTER,dialogDownloadTool);
                    }else{
                        infomationPhoneAdapter.setData(mList, new IClickInfomationPhone() {
                            @Override
                            public void onClickGoToMenu(InfomationPhone phone) {
                                CustomProgess.OpenDialog(Gravity.CENTER,dialog);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getContactPhone(phone.getSERI_PHONE());
                                    }
                                }, 1000);
                            }

                        });
                        rcv_InfoPhone.setAdapter(infomationPhoneAdapter);
                    }
                }else{
                }
            }

            @Override
            public void onFailure(Call<List<InfomationPhone>> call, Throwable t) {

            }
        });
    }
    private void getContactPhone(String seriPhone) {
        Call<List<Contact>> getListContact = APIClient.getUserService().getListContact(seriPhone);
        getListContact.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if (response.isSuccessful()) {
                    List<Contact> contactList = response.body();
                    if (contactList.size() == 0) {
                        CustomProgess.CancleDialog(dialog);
                        idIVNoData.setVisibility(View.VISIBLE);
                        rcv_contact.setVisibility(View.INVISIBLE);
                    } else {
                        CustomProgess.CancleDialog(dialog);
                        contactAdapter.setData(contactList);
                        rcv_contact.setAdapter(contactAdapter);
                        idIVNoData.setVisibility(View.INVISIBLE);
                        rcv_contact.setVisibility(View.VISIBLE);
                    }
                } else {
                    CustomProgess.CancleDialog(dialog);
                    Toast.makeText(getContext(), "Không có kết nối với máy chủ.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
            }
        });
    }
    public void backFragmentBefore() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
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
