package com.ChildMonitoringSystem.managerapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.models.HistorySignin;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HitoryViewHoder> {
    private Context context;
    private List<HistorySignin> mList;
    public HistoryAdapter(Context mContext) {
        this.context = mContext;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<HistorySignin> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public HitoryViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_login_history,parent, false);
        return new HitoryViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HitoryViewHoder holder, int position) {
        HistorySignin historySignin = mList.get(position);
        if (historySignin == null){
            return;
        }
        holder.idTVDateHistory.setText(historySignin.getDATE_LOGIN());
        holder.idTVModelPhone.setText(historySignin.getINFO());
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public class HitoryViewHoder extends RecyclerView.ViewHolder{
        private TextView idTVDateHistory,idTVModelPhone;
        public HitoryViewHoder(@NonNull View itemView) {
            super(itemView);
            idTVDateHistory = itemView.findViewById(R.id.idTVDateHistory);
            idTVModelPhone = itemView.findViewById(R.id.idTVModelPhone);
        }
    }
}
