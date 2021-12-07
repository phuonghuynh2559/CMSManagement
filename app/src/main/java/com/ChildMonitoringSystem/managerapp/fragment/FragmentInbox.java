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
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.Inbox;
import com.ChildMonitoringSystem.managerapp.models.PhoneNameInbox;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickItemMessage;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInbox extends Fragment {
    private View mView, viewDialog;
    private RecyclerView rcvInbox, idRCVContentInbox;
    private InboxAdapter inboxAdapter;
    private ContentMessAdapter contentMessAdapter;
    private ImageButton btnBack;
    private MainActivity mMainActivity;
    private TextView idTVPhoneNumberInbox, idTVNameInbox;
    private ImageView idIVBackLogin;
    private Dialog dialog;
    private Dialog dialogProcesbar;
    private ImageView idIVNoData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogProcesbar = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_inbox, container, false);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Xem tin nhắn điện thoại");

        rcvInbox = mView.findViewById(R.id.rcv_mess_inbox);
        btnBack = mView.findViewById(R.id.btnBack);
        idIVNoData = mView.findViewById(R.id.idIVNoData);

        inboxAdapter = new InboxAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvInbox.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvInbox.addItemDecoration(itemDecoration);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constan.Action);
        requireActivity().registerReceiver(seriPhone, intentFilter);
        loadFrameLayout();
        goToFragmentMenu();
        return mView;
    }

    private void loadFrameLayout() {
        FragmentInfomationPhone infomationPhone = new FragmentInfomationPhone();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayoutInbox, infomationPhone);
        fragmentTransaction.commit();
    }

    private void goToFragmentMenu() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMenu fragmentMenu = new FragmentMenu();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutMenu, fragmentMenu);
                fragmentTransaction.commit();
            }
        });
    }

    private BroadcastReceiver seriPhone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constan.Action.equals(intent.getAction())) {
                String seri = intent.getStringExtra("seriPhone");
                CustomProgess.OpenDialog(Gravity.CENTER, dialogProcesbar);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getInbox(seri);
                    }
                }, 1000);

            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(seriPhone);
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
                        rcvInbox.setVisibility(View.INVISIBLE);
                    } else {
                        CustomProgess.CancleDialog(dialogProcesbar);
                        inboxAdapter.setData(list, new IClickItemMessage() {
                            @Override
                            public void onClickItemMessage(PhoneNameInbox phoneNameInbox) {
                                goToDetail(phoneNameInbox, seriPhone);
                            }
                        });
                        rcvInbox.setAdapter(inboxAdapter);
                        idIVNoData.setVisibility(View.INVISIBLE);
                        rcvInbox.setVisibility(View.VISIBLE);
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
