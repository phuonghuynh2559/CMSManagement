package com.ChildMonitoringSystem.managerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.models.Video;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickVideoListener;
import com.bumptech.glide.Glide;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHoder> {
    private Context context;
    private List<Video> mList;
    private IClickVideoListener iClickVideoListener;
    public VideoAdapter(Context context){ this.context =context;}
    public void setData(List<Video> mList, IClickVideoListener iClickVideoListener){
        this.mList = mList;
        this.iClickVideoListener = iClickVideoListener;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public VideoViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent, false);
        return new VideoViewHoder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull VideoViewHoder holder, int position) {
        Video video = mList.get(position);
        if (video == null)return;
        holder.idTVDateVideo.setText(video.getDATE_MEDIA());
        String url = "http://117.2.159.103:8080/Files/" + video.getSERI_PHONE()+"/Video/"+video.getMEDIA_NAME();
        holder.idTVDuration.setText(video.getDURATION());
        holder.idTVNameVideo.setText(video.getMEDIA_NAME());
        holder.idTVSize.setText(video.getSIZE());
        Glide.with(holder.imgVideo.getContext()).load(url).into(holder.imgVideo);
        holder.llVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickVideoListener.onClickItemVideo(video);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (mList  !=null ){
            return mList.size();
        }
        return 0;
    }

    public class VideoViewHoder extends RecyclerView.ViewHolder {
        private ImageView imgVideo;
        private LinearLayout llVideo;
        private TextView idTVDateVideo,idTVDuration,idTVNameVideo,idTVSize;
        public VideoViewHoder(@NonNull View itemView) {
            super(itemView);
            imgVideo = itemView.findViewById(R.id.imgVideo);
            llVideo = itemView.findViewById(R.id.llVideo);
            idTVDateVideo = itemView.findViewById(R.id.idTVDateVideo);
            idTVDuration = itemView.findViewById(R.id.idTVDuration);
            idTVNameVideo = itemView.findViewById(R.id.idTVNameVideo);
            idTVSize = itemView.findViewById(R.id.idTVSize);
        }
    }
}
