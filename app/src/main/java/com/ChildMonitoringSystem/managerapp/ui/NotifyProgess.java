package com.ChildMonitoringSystem.managerapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentUserManual;

public class NotifyProgess extends Fragment {

    public void OpenDialogNotify(int center, Dialog dialog ) {

        dialog.setContentView(R.layout.layout_download_tool);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = center;
        window.setAttributes(windowLayoutParams);
        dialog.setCancelable(false);
        Button idBTNotifyOK = dialog.findViewById(R.id.idBTNotifyOK);
        Button idBTNotifyNo = dialog.findViewById(R.id.idBTNotifyNo);
        idBTNotifyOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //openFragmentUserManual();
                FragmentUserManual fragmentUserManual = new FragmentUserManual();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentUserManual);
                fragmentTransaction.commit();
            }
        });
        idBTNotifyNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
//    private void openFragmentUserManual() {
//        FragmentUserManual fragmentUserManual = new FragmentUserManual();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentUserManual);
//        fragmentTransaction.commit();
//    }
}
