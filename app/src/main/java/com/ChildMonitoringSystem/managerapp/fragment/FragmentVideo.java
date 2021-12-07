package com.ChildMonitoringSystem.managerapp.fragment;

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
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.VideoAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.Video;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickVideoListener;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentVideo extends Fragment {
    private RecyclerView rcvVideo;
    private View mView;
    private VideoAdapter videoAdapter;
    private VideoView videoView;
    private ImageButton btnBack;
    private MainActivity mMainActivity;
    private TextView idTVOpenFolder;
    private Dialog dialogProcesbar;
    private ImageView idIVNoData;

    private String namePhone = "";
    private TextView player_duration;
    private ImageButton idIBBack, idIBPlay, idIBPause, idIForward;
    private ImageView idIBOutDL;
    private SeekBar seeBar;
    private RelativeLayout rlControlsVideo, idRLVideo;
    private LinearLayout idLLDownloadDL;
    boolean isOpen = true;
    private Handler handler;
    private Runnable runnable;
    private Dialog dialog, dialogNotify;
    private View vDialogNotify;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogProcesbar = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_video, container, false);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Xem video điện thoại");

        rcvVideo = mView.findViewById(R.id.rcv_video_main);

        btnBack = mView.findViewById(R.id.btnBack);

        idIVNoData = mView.findViewById(R.id.idIVNoData);
        //idTVOpenFolder =mView.findViewById(R.id.idTVOpenFolder);

        videoAdapter = new VideoAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        rcvVideo.setLayoutManager(gridLayoutManager);

        loadFrameLayout();
        goToFragmentMenu();
        return mView;
    }

    private void openFolderDownload() {
        idTVOpenFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Environment.getExternalStorageDirectory() + "/Download/";
                Uri uri = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(uri, "*/*");
                startActivity(intent);
            }
        });
    }

    private void loadFrameLayout() {
        FragmentInfomationPhone infomationPhone = new FragmentInfomationPhone();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayoutImage, infomationPhone);
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
                namePhone = intent.getStringExtra("namePhone");
                CustomProgess.OpenDialog(Gravity.CENTER, dialogProcesbar);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getListVideo(seri);
                    }
                }, 1000);

            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constan.Action);
        requireActivity().registerReceiver(seriPhone, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(seriPhone);
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
                                if (mMainActivity.checkInternet() == 1) {
                                    openVideo(video);
                                }
                                else if (mMainActivity.checkInternet() == 2) {
                                    showNotify(Gravity.CENTER,video);
                                }else {
                                    Toast.makeText(getContext(),"Bạn không có kết nối mạng!",Toast.LENGTH_SHORT).show();
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

    private void showNotify(int center,Video video) {
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
                openVideo(video);
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

    private void openVideo(Video video) {
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
        idLLDownloadDL = dialog.findViewById(R.id.idLLDownloadDL);

        String path = "http://117.2.159.103:8080/Files/" + video.getSERI_PHONE() + "/Video/" + video.getMEDIA_NAME();
        idIBOutDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        idLLDownloadDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Video đang được tải xuống vui lòng chờ.", Toast.LENGTH_SHORT).show();
                starDowndloadFile(path);
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

    private void starDowndloadFile(String path) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Tải về.");
        request.setDescription("Đang tải về ...");
        Toast.makeText(getContext(), "Đang tải về!", Toast.LENGTH_SHORT).show();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, namePhone + "_VideoDownload_" + String.valueOf(System.currentTimeMillis()));
        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }
}
