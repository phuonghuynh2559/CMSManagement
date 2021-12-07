package com.ChildMonitoringSystem.managerapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ChildMonitoringSystem.managerapp.R;


public class CustomProgess extends AppCompatActivity {

    public static void OpenDialog(int center,Dialog dialog) {

        dialog.setContentView(R.layout.custom_procesbar);
        Window window = dialog.getWindow();
        if(window==null)
        {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = center;
        window.setAttributes(windowLayoutParams);
        dialog.setCancelable(false);

        dialog.show();
    }

    public static void CancleDialog(Dialog dialog)
    {
        dialog.dismiss();
    }
}