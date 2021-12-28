package com.ChildMonitoringSystem.managerapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;


public class FragmentMenu extends Fragment {
private View view;
private CardView cv_Contact,cv_HistoryCall,cv_MessInbox,cv_Image,cv_Video,cv_Location, cv_IntallAppMN, cv_AudioMN;
private MainActivity mMainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_menu, container,false);
        mMainActivity = (MainActivity)getActivity();
        mMainActivity.getToolbar().setTitle("Danh sách chức năng");
        cv_Contact = view.findViewById(R.id.cv_ContactMN);
        cv_HistoryCall = view.findViewById(R.id.cv_History_CallMN);
        cv_MessInbox = view.findViewById(R.id.cv_mess_inboxMN);
        cv_Image = view.findViewById(R.id.cv_ImageMN);
        cv_Video = view.findViewById(R.id.cv_VideoMN);
        cv_Location = view.findViewById(R.id.cv_LocationMN);
        cv_IntallAppMN = view.findViewById(R.id.cv_IntallAppMN);
        cv_AudioMN = view.findViewById(R.id.cv_AudioMN);
        cv_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGotoFragmentContat();
            }
        });
        cv_HistoryCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGotoFragmentHistoryCall();
            }
        });
        cv_MessInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGotoFragmentInbox();
            }
        });
        cv_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGotoFragmentImages();
            }
        });
        cv_Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGotoFragmentVideo();
            }
        });
        cv_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGoToFragmentLocation();
            }
        });
        cv_IntallAppMN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGoToFragmentAppInstall();
            }
        });
        cv_AudioMN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGoToFragmentAudio();
            }
        });
        return view;
    }
    private void clickGotoFragmentContat() {
        FragmentContact fragmentContact = new FragmentContact();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentContact).addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void clickGotoFragmentHistoryCall(){
        FragmentCallLog fragmentCallLog = new FragmentCallLog();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentCallLog).addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void clickGotoFragmentInbox() {
        FragmentInbox fragmentInbox = new FragmentInbox();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentInbox);
        fragmentTransaction.commit();
    }
    private void clickGotoFragmentImages() {
        FragmentImages fragmentImages = new FragmentImages();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentImages);
        fragmentTransaction.commit();
    }
    private void clickGotoFragmentVideo() {
        FragmentVideo fragmentVideo = new FragmentVideo();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentVideo);
        fragmentTransaction.commit();
    }
    private void clickGoToFragmentLocation(){
        FragmentMap fragmentMap = new FragmentMap();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentMap);
        fragmentTransaction.commit();
    }
    private void clickGoToFragmentAppInstall(){
        FragmentApp fragmentApp = new FragmentApp();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentApp);
        fragmentTransaction.commit();
    }
    private void clickGoToFragmentAudio() {
        FragmentAudio fragmentAudio = new FragmentAudio();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentAudio);
        fragmentTransaction.commit();
    }
}
