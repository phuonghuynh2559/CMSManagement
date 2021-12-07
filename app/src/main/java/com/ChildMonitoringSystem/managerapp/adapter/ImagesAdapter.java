package com.ChildMonitoringSystem.managerapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.Images;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickImageListener;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHoder> {
    private Context context;
    private List<Images> mlist;
    private IClickImageListener iClickImageListener;

    public ImagesAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Images> list,IClickImageListener iClickImageListener){
        this.mlist = list;
        this.iClickImageListener = iClickImageListener;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ImageViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent, false);
        return new ImageViewHoder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ImageViewHoder holder, int position) {
        Images image = mlist.get(position);
        if (image == null)return;
        Glide.with(holder.img.getContext())
                .load("http://117.2.159.103:8080/Files/"+image.getSERI_PHONE()+"/Images/"+image.getIMAGES_NAME()).into(holder.img);
        holder.cvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickImageListener.onClickItemImage(image);
            }
        });
        //holder.tv_dateImage.setText(image.getDATE_IMAGE());
    }

    @Override
    public int getItemCount() {
        if (mlist  !=null ){
            return mlist.size();
        }
        return 0;
    }

    public class ImageViewHoder extends RecyclerView.ViewHolder {
        private ImageView img;
        private CardView cvImage;
        //private TextView tv_dateImage;
        public ImageViewHoder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_image);
            cvImage = itemView.findViewById(R.id.cv_image);
            //tv_dateImage = itemView.findViewById(R.id.tv_dateImgae);
        }
    }
}
