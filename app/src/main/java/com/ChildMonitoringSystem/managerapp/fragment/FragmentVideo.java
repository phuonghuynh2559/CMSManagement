package com.ChildMonitoringSystem.managerapp.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
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
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.InfomationPhoneAdapter;
import com.ChildMonitoringSystem.managerapp.adapter.VideoAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.internet_checking.InternetChecking;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.models.Video;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickInfomationPhone;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickVideoListener;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;
import com.ChildMonitoringSystem.managerapp.ui.NotifyProgess;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentVideo extends NotifyProgess {
    private RecyclerView rcvVideo, rcv_InfoPhone;
    private View mView;
    private VideoAdapter videoAdapter;
    private VideoView videoView;
    private ImageView btnBack;
    private MainActivity mMainActivity;
    private Dialog dialogProcesbar, dialogDownloadTool;
    private ImageView idIVNoData;
    private InfomationPhoneAdapter infomationPhoneAdapter;
    private MyShareReference myShareReference;
    private String phoneNumber;

    private TextView player_duration;
    private ImageButton idIBBack, idIBPlay, idIBPause, idIForward;
    private ImageView idIBOutDL;
    private SeekBar seeBar;
    private RelativeLayout rlControlsVideo, idRLVideo;
    private LinearLayout idLLDeleteVideo;
    boolean isOpen = true;
    private Handler handler;
    private Runnable runnable;
    private Dialog dialog, dialogNotify, dialogDelteVideoDownl;
    private String pathVideo;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                CustomProgess.CancleDialog(dialogProcesbar);
                openVideo(pathVideo);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogProcesbar = new Dialog(getContext());
        dialogDownloadTool = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_video, container, false);
        myShareReference = new MyShareReference(getContext());
        phoneNumber = myShareReference.getValueString("phoneNumber");
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Xem video điện thoại");

        rcvVideo = mView.findViewById(R.id.rcv_video_main);
        rcv_InfoPhone = mView.findViewById(R.id.rcv_InfoPhone);

        btnBack = mView.findViewById(R.id.btnBack);

        idIVNoData = mView.findViewById(R.id.idIVNoData);

        infomationPhoneAdapter = new InfomationPhoneAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_InfoPhone.setLayoutManager(gridLayoutManager);
        loadListPhoneMonitor(phoneNumber);

        videoAdapter = new VideoAdapter(getContext());
        GridLayoutManager gridLayoutManagerVideo = new GridLayoutManager(getContext(), 1);
        rcvVideo.setLayoutManager(gridLayoutManagerVideo);

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
                        OpenDialogNotify(Gravity.CENTER,dialogDownloadTool);
                    } else {
                        infomationPhoneAdapter.setData(mList, new IClickInfomationPhone() {
                            @Override
                            public void onClickGoToMenu(InfomationPhone phone) {
                                CustomProgess.OpenDialog(Gravity.CENTER, dialogProcesbar);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getListVideo(phone.getSERI_PHONE());
                                    }
                                }, 1000);
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
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getListVideo(String seriPhone) {
        Call<List<Video>> getVieo = APIClient.getUserService().getListVideo(seriPhone);
        getVieo.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    List<Video> vLsit = response.body();
                    if (vLsit.size() == 0) {
                        CustomProgess.CancleDialog(dialogProcesbar);
                        idIVNoData.setVisibility(View.VISIBLE);
                        rcvVideo.setVisibility(View.INVISIBLE);
                    } else {
                        CustomProgess.CancleDialog(dialogProcesbar);
                        videoAdapter.setData(vLsit, new IClickVideoListener() {
                            @Override
                            public void onClickItemVideo(Video video) {
                                String url = "http://117.2.159.103:8080/Files/" + video.getSERI_PHONE() + "/Video/" + video.getMEDIA_NAME();
                                if (InternetChecking.Checknet(getContext()) == 1) {
                                    CustomProgess.OpenDialog(Gravity.CENTER, dialogProcesbar);
                                    checkPermison(url);
                                } else if (InternetChecking.Checknet(getContext()) == 2) {
                                    showNotify(Gravity.CENTER, url);
                                } else {
                                    Toast.makeText(getContext(), "Bạn không có kết nối mạng!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        rcvVideo.setAdapter(videoAdapter);
                        idIVNoData.setVisibility(View.INVISIBLE);
                        rcvVideo.setVisibility(View.VISIBLE);
                    }
                } else {
                    CustomProgess.CancleDialog(dialogProcesbar);
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {

            }
        });
    }

    private void showNotify(int center, String url) {
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
                checkPermison(url);
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

    protected void checkPermison(String url) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                starDowndloadFile(url);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void starDowndloadFile(String url) {
        String fileName = URLUtil.guessFileName(url, null, null);
        String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Log.d("TAG", "onClick: path " + downloadPath);
        File file = new File(downloadPath, fileName);
        pathVideo = String.valueOf(file);
        Log.d("TAG-path", "starDowndloadFile: " + pathVideo);
        if (file.exists()) {
            openVideo(pathVideo);
            Toast.makeText(getContext(), "Video đã được tải về trước đó", Toast.LENGTH_SHORT).show();
            CustomProgess.CancleDialog(dialogProcesbar);
        } else {
            DownloadManager.Request request = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    request = new DownloadManager.Request(Uri.parse(url))
                            .setTitle(fileName)
                            .setDescription("Downloading...")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                            .setDestinationUri(Uri.fromFile(file))
                            .setRequiresCharging(false)
                            .setAllowedOverMetered(true)
                            .setAllowedOverRoaming(true);
                }
            } else {
                request = new DownloadManager.Request(Uri.parse(url))
                        .setTitle(fileName)
                        .setDescription("Downloading...")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setDestinationUri(Uri.fromFile(file))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true);
            }
            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(getContext().DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
        }
    }

    private void openVideo(String path) {
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_dialog_video, null);
        dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(viewDialog);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        videoView = dialog.findViewById(R.id.idVideoView);
        player_duration = dialog.findViewById(R.id.player_duration);
        idIBBack = dialog.findViewById(R.id.idIBBack);
        idIBPlay = dialog.findViewById(R.id.idIBPlay);
        idIBPause = dialog.findViewById(R.id.idIBPause);
        idIForward = dialog.findViewById(R.id.idIForward);
        idIBOutDL = dialog.findViewById(R.id.idIBOutDL);

        seeBar = dialog.findViewById(R.id.seeBar);
        rlControlsVideo = dialog.findViewById(R.id.idControl);
        idRLVideo = dialog.findViewById(R.id.idRLVideo);
        idLLDeleteVideo = dialog.findViewById(R.id.idLLDeleteVideo);

        idIBOutDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        idLLDeleteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNotifi(Gravity.CENTER,path);
            }
        });
        videoView.setVideoURI(Uri.parse(path));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seeBar.setMax(videoView.getDuration());
                videoView.start();
            }
        });
        idIBBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition() - 10000);
                videoView.start();
                seeBar.setMax(videoView.getDuration());
                handler.postDelayed(runnable, 0);
            }
        });
        idIForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition() + 10000);
                videoView.start();
                seeBar.setMax(videoView.getDuration());
                handler.postDelayed(runnable, 0);
            }
        });
        idIBPlay.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (!videoView.isPlaying()) {
                    videoView.start();
                    idIBPlay.setVisibility(View.GONE);
                    idIBPause.setVisibility(View.VISIBLE);
                }
            }
        });
        idIBPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idIBPlay.setVisibility(View.VISIBLE);
                idIBPause.setVisibility(View.GONE);
                videoView.pause();
            }
        });
        idRLVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    hidenControl();
                    isOpen = false;
                } else {
                    showControl();
                    isOpen = true;
                }
            }
        });
        setHandler();
        initializeSeeBar();
        dialog.show();
    }

    private void openDialogNotifi(int center,String path) {
        dialogDelteVideoDownl = new Dialog(getContext());
        dialogDelteVideoDownl.setContentView(R.layout.layout_dialog_notification);
        Window window = dialogDelteVideoDownl.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = center;
        window.setAttributes(windowLayoutParams);
        dialogDelteVideoDownl.setCancelable(false);
        Button idBTNotifyOK = dialogDelteVideoDownl.findViewById(R.id.idBTNotifyOK);
        Button idBTNotifyNo = dialogDelteVideoDownl.findViewById(R.id.idBTNotifyNo);

        TextView idTVContent = dialogDelteVideoDownl.findViewById(R.id.idTVContent);
        TextView idTVQuestion = dialogDelteVideoDownl.findViewById(R.id.idTVQuestion);
        idTVContent.setText("Bạn muốn xóa video này ?");
        idTVQuestion.setVisibility(View.INVISIBLE);
        idBTNotifyOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(path);
                deleteDirectory(file);
                dialogDelteVideoDownl.dismiss();
                dialog.dismiss();
            }
        });
        idBTNotifyNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDelteVideoDownl.dismiss();
            }
        });
        dialogDelteVideoDownl.show();
    }

    public static void deleteDirectory(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
            file.delete();
        }
    }

    private void hidenControl() {
        rlControlsVideo.setVisibility(View.GONE);
        final Window window = this.dialog.getWindow();
        if (window == null) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = window.getDecorView();
        if (decorView != null) {
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= 14) {
                uiOption |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT >= 16) {
                uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    private void showControl() {
        rlControlsVideo.setVisibility(View.VISIBLE);
        final Window window = this.dialog.getWindow();
        if (window == null) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView = window.getDecorView();
        if (decorView != null) {
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= 14) {
                uiOption &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT >= 16) {
                uiOption &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOption &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    @SuppressLint("DefaultLocale")
    private String converTime(int ms) {
        String time;
        int x, seconds, minutes, hours;
        x = ms / 1000;
        seconds = x % 60;
        x /= 60;
        minutes = x % 60;
        x /= 60;
        hours = x % 24;
        if (hours != 0) {
            time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        } else {
            time = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        }
        return time;
    }

    private void setHandler() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.getDuration() > 0) {
                    int curPos = videoView.getCurrentPosition();
                    seeBar.setProgress(curPos);
                    player_duration.setText("" + converTime(videoView.getDuration() - curPos));
                }
                handler.postDelayed(this, 0);
            }
        };
        handler.postDelayed(runnable, 500);
    }

    private void initializeSeeBar() {
        seeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == R.id.seeBar) {
                    if (fromUser) {
                        videoView.seekTo(progress);
                        videoView.start();
                        idIBPlay.setVisibility(View.GONE);
                        idIBPause.setVisibility(View.VISIBLE);
                        int curPos = videoView.getCurrentPosition();
                        player_duration.setText("" + converTime(videoView.getDuration() - curPos));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity()
                .registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(broadcastReceiver);
    }
}
