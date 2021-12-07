package com.ChildMonitoringSystem.managerapp.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.ChildMonitoringSystem.managerapp.adapter.ContactAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.Contact;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentContact extends Fragment {
    private View view;
    private RecyclerView rcv_contact;
    private ContactAdapter contactAdapter;
    private ImageButton btnBack;
    private MainActivity mMainActivity;
    private ImageView idIVNoData;
    private Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        rcv_contact = view.findViewById(R.id.rcv_contact);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Xem danh bạ điện thoại");
        btnBack = view.findViewById(R.id.btnBack);
        idIVNoData = view.findViewById(R.id.idIVNoData);

        contactAdapter = new ContactAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_contact.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv_contact.addItemDecoration(itemDecoration);
        loadFrameLayout();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFragmentBefore();
            }
        });
        return view;
    }

    private void loadFrameLayout() {
        FragmentInfomationPhone infomationPhone = new FragmentInfomationPhone();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayoutContact, infomationPhone);
        fragmentTransaction.commit();
    }

    private BroadcastReceiver seriPhone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constan.Action.equals(intent.getAction())) {
                String seri = intent.getStringExtra("seriPhone");
                CustomProgess.OpenDialog(Gravity.CENTER,dialog);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getContactPhone(seri);
                    }
                },1000);

            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(Constan.Action);
        requireActivity().registerReceiver(seriPhone, intentFilter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(seriPhone);
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
        FragmentMenu fragmentMenu = new FragmentMenu();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMenu, fragmentMenu);
        fragmentTransaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null) {
            CustomProgess.CancleDialog(dialog);
        }
    }
}