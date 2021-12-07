package com.ChildMonitoringSystem.managerapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickInfomationPhone;

import java.util.List;

public class InfomationPhoneAdapter extends RecyclerView.Adapter<InfomationPhoneAdapter.ViewHoder> {
    private Context context;
    private List<InfomationPhone> mList;
    private IClickInfomationPhone iClickInfomationPhone;
    private int selectedItem = -1;
    public InfomationPhoneAdapter(Context mconContext) {
        this.context = mconContext;
    }
    public void setData(List<InfomationPhone> list,IClickInfomationPhone iClickInfomationPhone)
    {
        this.mList=list;
        this.iClickInfomationPhone = iClickInfomationPhone;
        //notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_infomation_phone,parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        InfomationPhone phone = mList.get(position);
        if (phone==null)return;
        holder.tv_namePhone.setText(phone.getNAME_SPY());
        holder.tv_Model.setText(phone.getMODEL());
        holder.cv_InfomationPhone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                selectedItem = position;
                notifyDataSetChanged();
                iClickInfomationPhone.onClickGoToMenu(phone);
            }
        });
        if (selectedItem == position){
            holder.relativelayoutIP.setBackgroundColor(Color.parseColor("#00ffff"));
        }else{
            holder.relativelayoutIP.setBackgroundColor(Color.parseColor("#f0f8ff"));
        }
    }
    @Override
    public int getItemCount() {
        if (mList  !=null ){
            return mList.size();
        }
        return 0;
    }

    public class ViewHoder extends RecyclerView.ViewHolder{
        private TextView tv_namePhone, tv_Model;
        private CardView cv_InfomationPhone;
        private RelativeLayout relativelayoutIP;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_namePhone = itemView.findViewById(R.id.tv_namePhone);
            tv_Model = itemView.findViewById(R.id.tv_Model);
            cv_InfomationPhone = itemView.findViewById(R.id.cv_InfomationPhone);
            relativelayoutIP = itemView.findViewById(R.id.relativelayoutIP);
        }
    }
}
