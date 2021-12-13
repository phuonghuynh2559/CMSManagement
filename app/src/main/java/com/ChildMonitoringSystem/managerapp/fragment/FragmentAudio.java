package com.ChildMonitoringSystem.managerapp.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.AudioAdapter;
import com.ChildMonitoringSystem.managerapp.adapter.InfomationPhoneAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.Audio;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickAudioListener;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickInfomationPhone;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentAudio extends Fragment {
    private View mView, viewDialog;
    private MainActivity mMainActivity;
    private ImageView idIBBackMenu;
    private RecyclerView rcvAudio, rcv_InfoPhone;
    private AudioAdapter audioAdapter;
    private Dialog dialog;
    private ImageView idIVNoData;
    private Dialog dialogProcessbar;
    private InfomationPhoneAdapter infomationPhoneAdapter;
    private MyShareReference myShareReference;
    private String phoneNumber;

    private TextView tvPlayerPosition,tvPlayerDuration, idTVNameAudio;
    private ImageView btRew,btPlay,btPause,btFf;
    private ImageView idIBOutDL;
    private LinearLayout idLLDownloadDL;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable runnable;
    private String namePhone;
    public FragmentAudio() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogProcessbar = new Dialog(getContext());
        mView = inflater.inflate(R.layout.fragment_audio, container, false);
        myShareReference = new MyShareReference(getContext());
        phoneNumber = myShareReference.getValueString("phoneNumber");

        mMainActivity= (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Ghi Âm");

        idIBBackMenu = mView.findViewById(R.id.idIBBackMenu);
        rcvAudio = mView.findViewById(R.id.rcv_Audio);
        rcv_InfoPhone = mView.findViewById(R.id.rcv_InfoPhone);
        idIVNoData = mView.findViewById(R.id.idIVNoData);

        infomationPhoneAdapter = new InfomationPhoneAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_InfoPhone.setLayoutManager(gridLayoutManager);
        loadListPhoneMonitor(phoneNumber);

        audioAdapter = new AudioAdapter(getContext());
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rcvAudio.setLayoutManager(gridLayoutManager);

        backMenu();

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
                                CustomProgess.OpenDialog(Gravity.CENTER, dialogProcessbar);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getListAudio(phone.getSERI_PHONE());
                                    }
                                }, 1000);
                                namePhone = phone.getNAME_SPY();
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

    private void backMenu(){
        idIBBackMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getListAudio(String seriPhone){
        Call<List<Audio>> getAudio = APIClient.getUserService().getAudio(seriPhone);
        getAudio.enqueue(new Callback<List<Audio>>() {
            @Override
            public void onResponse(Call<List<Audio>> call, Response<List<Audio>> response) {
                if (response.isSuccessful()){
                    List<Audio>audioList = response.body();
                    if (audioList.size() == 0){
                        CustomProgess.CancleDialog(dialogProcessbar);
                        idIVNoData.setVisibility(View.VISIBLE);
                        rcvAudio.setVisibility(View.INVISIBLE);
                    }else{
                        CustomProgess.CancleDialog(dialog);
                        audioAdapter.setData(audioList, new IClickAudioListener() {
                            @Override
                            public void onClickAudio(Audio audio) {
                                openDetailAudio(audio);
                            }
                        });
                        rcvAudio.setAdapter(audioAdapter);
                        idIVNoData.setVisibility(View.INVISIBLE);
                        rcvAudio.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Audio>> call, Throwable t) {

            }
        });
    }

    private void openDetailAudio(Audio audio) {
        viewDialog = getLayoutInflater().inflate(R.layout.layout_dialog_audio_call, null);
        dialog = new Dialog(getContext(),android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(viewDialog);
        Window window = dialog.getWindow();
        if (window == null)return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        tvPlayerPosition=dialog.findViewById(R.id.player_posision);
        tvPlayerDuration=dialog.findViewById(R.id.player_duration);
        idTVNameAudio=dialog.findViewById(R.id.idTVNameAudio);
        seekBar = dialog.findViewById(R.id.seeBar);
        btRew = dialog.findViewById(R.id.bt_rew);
        btPlay = dialog.findViewById(R.id.bt_play);
        btPause = dialog.findViewById(R.id.bt_pause);
        btFf = dialog.findViewById(R.id.bt_ff);
        idIBOutDL = dialog.findViewById(R.id.idIBOutDL);
        idLLDownloadDL = dialog.findViewById(R.id.idLLDownloadDL);

        idIBOutDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                dialog.dismiss();
            }
        });
        idTVNameAudio.setText(audio.getIMAGES_NAME());
        String audioURL = "http://117.2.159.103:8080/Files/"
                +audio.getSERI_PHONE()+"/Audio/"+audio.getIMAGES_NAME();
        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(audioURL));
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };
        int duration = mediaPlayer.getDuration();
        String sDuration = convertFormat(duration);
        tvPlayerDuration.setText(sDuration);
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay.setVisibility(v.GONE);
                btPause.setVisibility(v.VISIBLE);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                handler.postDelayed(runnable, 0);

            }
        });
        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPause.setVisibility(v.GONE);
                btPlay.setVisibility(v.VISIBLE);
                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
            }
        });
        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                if (mediaPlayer.isPlaying() && duration != currentPosition) {
                    currentPosition = currentPosition + 5000;
                    tvPlayerPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);

                }
            }
        });
        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                    currentPosition = currentPosition - 5000;
                    tvPlayerPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });
        eventSeeBar();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btPause.setVisibility(View.GONE);
                btPlay.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(0);
            }
        });
        idLLDownloadDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starDowndloadFile(audioURL);
            }
        });
        dialog.show();
    }
    private void eventSeeBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
                tvPlayerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        String time;
        int x, seconds, minutes, hours;
        x = duration/1000;
        seconds = x%60;
        x /= 60;
        minutes = x % 60;
        x /= 60;
        hours = x%24;
        if (hours != 0){
            time = String.format("%02d",hours)+":"+String.format("%02d",minutes)+":"+String.format("%02d",seconds);
        }else {
            time = String.format("%02d",minutes)+":"+String.format("%02d",seconds);
        }
        return time;
    }
    private void starDowndloadFile(String path) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Tải về.");
        request.setDescription("Đang tải về ...");
        Toast.makeText(getContext(), "Đang tải về!", Toast.LENGTH_SHORT).show();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,namePhone+"_AudioDownload_" + String.valueOf(System.currentTimeMillis()));
        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null){
            downloadManager.enqueue(request);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (dialogProcessbar != null) {
            CustomProgess.CancleDialog(dialogProcessbar);
        }
    }
}