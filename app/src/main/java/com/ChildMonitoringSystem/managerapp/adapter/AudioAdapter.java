package com.ChildMonitoringSystem.managerapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.models.Audio;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickAudioListener;

import java.util.List;

public class AudioAdapter extends  RecyclerView.Adapter<AudioAdapter.AudioViewHoder> {
    private List<Audio> mList;
    private Context mContext;
    private IClickAudioListener iClickAudioListener;
    public AudioAdapter(Context mContext){this.mContext = mContext;}

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Audio> mList, IClickAudioListener iClickAudioListener) {
        this.mList = mList;
        this.iClickAudioListener = iClickAudioListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AudioViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio,parent, false);
        return new AudioViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHoder holder, int position) {
        final Audio audio = mList.get(position);
        if (audio==null)return ;
        holder.tvAudioDayIT.setText(audio.getDATE_IMAGE());
        holder.cvAudioIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickAudioListener.onClickAudio(audio);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }
        return 0;
    }

    public class AudioViewHoder extends RecyclerView.ViewHolder{
        private CardView cvAudioIT;
        private TextView tvAudioDayIT;
        public AudioViewHoder(@NonNull View itemView) {
            super(itemView);
            cvAudioIT = itemView.findViewById(R.id.cvAudioIT);
            tvAudioDayIT = itemView.findViewById(R.id.tvAudioDayIT);
        }
    }
}
