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
import android.widget.TextView;
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
import com.ChildMonitoringSystem.managerapp.adapter.ContentMessAdapter;
import com.ChildMonitoringSystem.managerapp.adapter.InboxAdapter;
import com.ChildMonitoringSystem.managerapp.adapter.InfomationPhoneAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.Inbox;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.models.PhoneNameInbox;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickInfomationPhone;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickItemMessage;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;
import com.ChildMonitoringSystem.managerapp.ui.NotifyProgess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInbox extends NotifyProgess {
    private View mView, viewDialog;
    private RecyclerView rcv_InfoPhone,rcv_Inbox, idRCVContentInbox;
    private InboxAdapter inboxAdapter;
    private InfomationPhoneAdapter infomationPhoneAdapter;
    private ContentMessAdapter contentMessAdapter;
    private ImageView btnBack;
    private MainActivity mMainActivity;
    private TextView idTVPhoneNumberInbox, idTVNameInbox;
    private ImageView idIVBackLogin;
    private Dialog dialog, dialogProcesbar,dialogDownloadTool;
    private ImageView idIVNoData;

    private MyShareReference myShareReference;
    private String phoneNumber;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogProcesbar = new Dialog(getContext());
        dialogDownloadTool = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_inbox, container, false);
        myShareReference = new MyShareReference(getContext());
        phoneNumber = myShareReference.getValueString("phoneNumber");

        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Xem tin nhắn điện thoại");

        rcv_Inbox = mView.findViewById(R.id.rcv_mess_inbox);
        rcv_InfoPhone = mView.findViewById(R.id.rcv_InfoPhone);
        btnBack = mView.findViewById(R.id.btnBack);
        idIVNoData = mView.findViewById(R.id.idIVNoData);

        infomationPhoneAdapter = new InfomationPhoneAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_InfoPhone.setLayoutManager(gridLayoutManager);
        loadListPhoneMonitor(phoneNumber);

        inboxAdapter = new InboxAdapter(getContext());
        GridLayoutManager gridLayoutCall = new GridLayoutManager(getContext(), 1);
        rcv_Inbox.setLayoutManager(gridLayoutCall);

        goToFragmentMenu();
        return mView;
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
                                CustomProgess.OpenDialog(Gravity.CENTER,dialogProcesbar);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                       getInbox(phone.getSERI_PHONE());
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


    private void goToFragmentMenu() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getInbox(String seriPhone) {
        Call<List<PhoneNameInbox>> listCall = APIClient.getUserService().getInboxCall(seriPhone);
        listCall.enqueue(new Callback<List<PhoneNameInbox>>() {
            @Override
            public void onResponse(Call<List<PhoneNameInbox>> call, Response<List<PhoneNameInbox>> response) {
                if (response.isSuccessful()) {
                    List<PhoneNameInbox> list = response.body();
                    if (list.size() == 0) {
                        CustomProgess.CancleDialog(dialogProcesbar);
                        idIVNoData.setVisibility(View.VISIBLE);
                        rcv_Inbox.setVisibility(View.INVISIBLE);
                    } else {
                        CustomProgess.CancleDialog(dialogProcesbar);
                        inboxAdapter.setData(list, new IClickItemMessage() {
                            @Override
                            public void onClickItemMessage(PhoneNameInbox phoneNameInbox) {
                                goToDetail(phoneNameInbox, seriPhone);
                            }
                        });
                        rcv_Inbox.setAdapter(inboxAdapter);
                        idIVNoData.setVisibility(View.INVISIBLE);
                        rcv_Inbox.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), "Không có kết nối với máy chủ.", Toast.LENGTH_SHORT).show();
                    CustomProgess.CancleDialog(dialogProcesbar);
                }
            }

            @Override
            public void onFailure(Call<List<PhoneNameInbox>> call, Throwable t) {
            }
        });
    }

    public void goToDetail(PhoneNameInbox phoneNameInbox, String seri) {
        viewDialog = getLayoutInflater().inflate(R.layout.layout_dialog_show_mess, null);
        dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(viewDialog);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        idTVNameInbox = dialog.findViewById(R.id.idTVNameInbox);
        idTVPhoneNumberInbox = dialog.findViewById(R.id.idTVPhoneNumberInbox);
        idIVBackLogin = dialog.findViewById(R.id.idIVBackLogin);
        idRCVContentInbox = dialog.findViewById(R.id.idRCVContentInbox);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        idRCVContentInbox.setLayoutManager(gridLayoutManager);

        contentMessAdapter = new ContentMessAdapter(getContext());
        idTVNameInbox.setText(phoneNameInbox.getNAME());
        idTVPhoneNumberInbox.setText(phoneNameInbox.getPHONE_NUMBERS());
        idIVBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        getContentInbox(phoneNameInbox.getPHONE_NUMBERS(), seri);
        dialog.show();
    }

    private void getContentInbox(String phoneNameInbox, String seri) {
        Call<List<Inbox>> getContent = APIClient.getUserService().getContentInbox(phoneNameInbox, seri);
        getContent.enqueue(new Callback<List<Inbox>>() {
            @Override
            public void onResponse(Call<List<Inbox>> call, Response<List<Inbox>> response) {
                if (response.isSuccessful()) {
                    List<Inbox> list = response.body();
                    if (list.size() == 0) {
                        Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    } else {
                        contentMessAdapter.setData(list);
                        idRCVContentInbox.setAdapter(contentMessAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Inbox>> call, Throwable t) {

            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        if (dialogProcesbar != null) {
            CustomProgess.CancleDialog(dialogProcesbar);
        }
    }
}
