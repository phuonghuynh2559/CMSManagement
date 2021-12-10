package com.ChildMonitoringSystem.managerapp.fragment;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.ImagesAdapter;
import com.ChildMonitoringSystem.managerapp.adapter.InfomationPhoneAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.Images;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickImageListener;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickInfomationPhone;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentImages extends Fragment {
    private View mView;
    private RecyclerView rcvImage, rcv_InfoPhone;
    private ImagesAdapter imagesAdapter;
    private InfomationPhoneAdapter infomationPhoneAdapter;
    private ImageView btnBack;
    private ImageView imgViewDL;
    private Button idBTGetImage;

    private MainActivity mMainActivity;
    private Dialog dialog;

    private ImageView idIVNoData;
    private Dialog dialogProcesbar, dialogNotify;

    private int num = 5;

    private MyShareReference myShareReference;
    private String phoneNumber;
    private String namePhone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogProcesbar = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_image, container, false);

        myShareReference = new MyShareReference(getContext());
        phoneNumber = myShareReference.getValueString("phoneNumber");
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Xem hình ảnh điện thoại");

        rcvImage = mView.findViewById(R.id.rcv_image_main);
        rcv_InfoPhone = mView.findViewById(R.id.rcv_InfoPhone);

        btnBack = mView.findViewById(R.id.btnBack);
        idIVNoData = mView.findViewById(R.id.idIVNoData);
        idBTGetImage = mView.findViewById(R.id.idBTGetImage);

        infomationPhoneAdapter = new InfomationPhoneAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_InfoPhone.setLayoutManager(gridLayoutManager);
        loadListPhoneMonitor(phoneNumber);

        imagesAdapter = new ImagesAdapter(getContext());
        GridLayoutManager gridLayoutManagerImage = new GridLayoutManager(getContext(), 3);
        rcvImage.setLayoutManager(gridLayoutManagerImage);

        idBTGetImage.setVisibility(View.INVISIBLE);
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
                                CustomProgess.OpenDialog(Gravity.CENTER, dialogProcesbar);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getImages(phone.getSERI_PHONE(), 5);
                                    }
                                }, 1000);
                                namePhone = phone.getNAME_SPY();
                                addImage(phone.getSERI_PHONE());
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
                FragmentMenu fragmentMenu = new FragmentMenu();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutMenu, fragmentMenu);
                fragmentTransaction.commit();
            }
        });
    }

    private void addImage(String seriPhone) {

        idBTGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = num + 5;
                CustomProgess.OpenDialog(Gravity.CENTER, dialogProcesbar);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getImages(seriPhone, num);
                    }
                }, 1000);

            }
        });
    }

    private void getImages(String seriPhone, int num) {
        APIClient.getUserService().getListImages(seriPhone, num).enqueue(new Callback<List<Images>>() {
            @Override
            public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                if (response.isSuccessful()) {
                    List<Images> mList = response.body();
                    if (mList.size() == 0) {
                        CustomProgess.CancleDialog(dialogProcesbar);
                        rcvImage.setVisibility(View.INVISIBLE);
                        idIVNoData.setVisibility(View.VISIBLE);
                        idBTGetImage.setVisibility(View.INVISIBLE);
                    } else {
                        CustomProgess.CancleDialog(dialogProcesbar);
                        rcvImage.setVisibility(View.VISIBLE);
                        idIVNoData.setVisibility(View.INVISIBLE);
                        idBTGetImage.setVisibility(View.VISIBLE);
                        imagesAdapter.setData(mList, new IClickImageListener() {
                            @Override
                            public void onClickItemImage(Images image) {
                                openDialogImages(image);
                            }
                        });
                        rcvImage.setAdapter(imagesAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Images>> call, Throwable t) {

            }
        });
    }

    private void openDialogNotify(int center, List<Images> list) {
        dialogNotify = new Dialog(getContext());
        dialogNotify.setContentView(R.layout.layout_dialog_notification);
        Window window = dialogNotify.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = center;
        window.setAttributes(windowLayoutParams);
        dialogNotify.setCancelable(false);
        Button idBTNotifyOK = dialogNotify.findViewById(R.id.idBTNotifyOK);
        Button idBTNotifyNo = dialogNotify.findViewById(R.id.idBTNotifyNo);
        idBTNotifyOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNotify.dismiss();
                CustomProgess.CancleDialog(dialogProcesbar);
                imagesAdapter.setData(list, new IClickImageListener() {
                    @Override
                    public void onClickItemImage(Images image) {
                        openDialogImages(image);
                    }
                });
                rcvImage.setAdapter(imagesAdapter);
                idIVNoData.setVisibility(View.INVISIBLE);
                rcvImage.setVisibility(View.VISIBLE);
            }
        });
        idBTNotifyNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNotify.dismiss();
            }
        });
        dialogNotify.show();
    }

    private void openDialogImages(Images image) {

        View viewDialog = getLayoutInflater().inflate(R.layout.layout_dialog_image, null);
        dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(viewDialog);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        TextView tvDayImage = dialog.findViewById(R.id.tvDayImageDL);
        imgViewDL = dialog.findViewById(R.id.imageDL);
        ImageView btnBack = dialog.findViewById(R.id.btnBackDL);
        LinearLayout btnDownload = dialog.findViewById(R.id.btnDownloadDL);
        tvDayImage.setText(image.getDATE_IMAGE());
        String path = "http://117.2.159.103:8080/Files/" + image.getSERI_PHONE() + "/Images/" + image.getIMAGES_NAME();
        Glide.with(getContext()).load(path).into(imgViewDL);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starDowndloadFile(path);
            }
        });
        dialog.show();
    }

    private void starDowndloadFile(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Tải về");
        request.setDescription("Đang tải về ...");
        Toast.makeText(getContext(), "Đang tải về!", Toast.LENGTH_SHORT).show();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, namePhone + "_ImageDownload_" + String.valueOf(System.currentTimeMillis()));
        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialogProcesbar != null) {
            CustomProgess.CancleDialog(dialogProcesbar);
        }
    }
}