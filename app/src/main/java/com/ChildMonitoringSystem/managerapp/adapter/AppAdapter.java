package com.ChildMonitoringSystem.managerapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.models.App;
import com.bumptech.glide.Glide;

import java.util.List;

public class AppAdapter extends  RecyclerView.Adapter<AppAdapter.AppViewHoder> {
    private List<App> mList;
    private Context mContext;
    public AppAdapter(Context mContext) {
        this.mContext = mContext;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<App> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AppViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app,parent, false);
        return new AppViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHoder holder, int position) {
        App app = mList.get(position);
        if (app==null)
            return;
        Glide.with(holder.imageView.getContext())
                .load("http://117.2.159.103:8080/Files/"+mList.get(position).getSERI_PHONE()+
                        "/App/"+mList.get(position)
                        .getICON()).into(holder.imageView);
        holder.tv_AppName.setText(app.getAPPNAME());
        holder.tv_PName.setText(app.getpNAME());
    }

    @Override
    public int getItemCount() {
        if ( mList!=null){
            return mList.size();
        }
        return 0;
    }

    public class AppViewHoder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private CardView cardView;
        private TextView tv_AppName, tv_PName;
        public AppViewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_App);
            tv_AppName = itemView.findViewById(R.id.tv_AppName);
            tv_PName = itemView.findViewById(R.id.tv_PName);
        }
    }
}
