package com.ChildMonitoringSystem.managerapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;

public class FragmentUserManual extends Fragment {

    private RelativeLayout idRLContact, idRLAudio, idRLApp, idRLLocation, idRLVideo, idRLImage, idRLMessage, idRLCallLog;
    private Dialog dlContact,dlCallLog, dlMEss, dlImage, dlViddeo, dlLocation,dlApp, dlAudio;
    private ImageView idIVOut;
    private MainActivity mMainActivity;
    public FragmentUserManual() {
    }

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_user_manual, container, false);
        mMainActivity = (MainActivity)getActivity();
        mMainActivity.getToolbar().setTitle("Hổ trợ người dùng");
        idRLContact = mView.findViewById(R.id.idRLContact);
        idRLCallLog = mView.findViewById(R.id.idRLCallLog);
        idRLMessage = mView.findViewById(R.id.idRLMessage);
        idRLImage = mView.findViewById(R.id.idRLImage);
        idRLVideo = mView.findViewById(R.id.idRLVideo);
        idRLLocation = mView.findViewById(R.id.idRLLocation);
        idRLApp = mView.findViewById(R.id.idRLApp);
        idRLAudio = mView.findViewById(R.id.idRLAudio);

        idIVOut = mView.findViewById(R.id.idIVOut);
        evvenListener();
        return mView;
    }
    private void evvenListener(){
        idRLContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSPContact();
            }
        });
        idRLCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSPCallLog();
            }
        });
        idRLMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSPMessage();
            }
        });
        idRLImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSPImage();
            }
        });
        idRLVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSPVideo();
            }
        });
        idRLLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSPLocation();
            }
        });
        idRLApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSPApp();
            }
        });
        idRLAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSPAudio();
            }
        });
        idIVOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void openDialogSPContact() {
        dlContact = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dlContact.setContentView(R.layout.layout_support_contact);
        Window window = dlContact.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        idIVOut = dlContact.findViewById(R.id.idIVOut);
        idIVOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlContact.dismiss();
            }
        });

        dlContact.show();
    }
    private void openDialogSPCallLog() {
        dlCallLog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dlCallLog.setContentView(R.layout.layout_support_call_log);
        Window window = dlCallLog.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        idIVOut = dlCallLog.findViewById(R.id.idIVOut);
        idIVOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlCallLog.dismiss();
            }
        });

        dlCallLog.show();
    }

    private void openDialogSPMessage() {
        dlMEss = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dlMEss.setContentView(R.layout.layout_support_message);
        Window window = dlMEss.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        idIVOut = dlMEss.findViewById(R.id.idIVOut);
        idIVOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlMEss.dismiss();
            }
        });

        dlMEss.show();
    }
    private void openDialogSPImage() {
        dlImage = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dlImage.setContentView(R.layout.layout_support_image);
        Window window = dlImage.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        idIVOut = dlImage.findViewById(R.id.idIVOut);
        idIVOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlImage.dismiss();
            }
        });

        dlImage.show();
    }
    private void openDialogSPVideo() {
        dlViddeo = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dlViddeo.setContentView(R.layout.layout_support_video);
        Window window = dlViddeo.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        idIVOut = dlViddeo.findViewById(R.id.idIVOut);
        idIVOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlViddeo.dismiss();
            }
        });

        dlViddeo.show();
    }

    private void openDialogSPLocation() {
        dlLocation = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dlLocation.setContentView(R.layout.layout_support_location);
        Window window = dlLocation.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        idIVOut = dlLocation.findViewById(R.id.idIVOut);
        idIVOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlLocation.dismiss();
            }
        });

        dlLocation.show();
    }

    private void openDialogSPApp() {
        dlApp = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dlApp.setContentView(R.layout.layout_support_app);
        Window window = dlApp.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        idIVOut = dlApp.findViewById(R.id.idIVOut);
        idIVOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlApp.dismiss();
            }
        });

        dlApp.show();
    }

    private void openDialogSPAudio() {
        dlAudio = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dlAudio.setContentView(R.layout.layout_support_audio);
        Window window = dlAudio.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        idIVOut = dlAudio.findViewById(R.id.idIVOut);
        idIVOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlAudio.dismiss();
            }
        });

        dlAudio.show();
    }
}