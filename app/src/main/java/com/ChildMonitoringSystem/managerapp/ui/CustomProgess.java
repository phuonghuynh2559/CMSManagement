package com.ChildMonitoringSystem.managerapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ChildMonitoringSystem.managerapp.R;


public class CustomProgess extends AppCompatActivity {
    private long Backpresstime;
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

    @Override
    public void onBackPressed() {
        if(Backpresstime+2000>System.currentTimeMillis())
        {
            super.onBackPressed();
        }
        else
            Toast.makeText(this, "Chạm lại để thoát ứng dụng", Toast.LENGTH_SHORT).show();
        Backpresstime = System.currentTimeMillis();
    }
}