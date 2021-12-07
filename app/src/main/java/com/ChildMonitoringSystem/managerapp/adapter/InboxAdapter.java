package com.ChildMonitoringSystem.managerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.models.PhoneNameInbox;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickItemMessage;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHoder>{
    private Context mconContext;
    private List<PhoneNameInbox> mList;
    private IClickItemMessage iClickItemMessage;
    public InboxAdapter(Context mconContext) {
        this.mconContext = mconContext;
    }
    public void setData(List<PhoneNameInbox> list,IClickItemMessage iClickItemMessage)
    {
        this.mList=list;
        this.iClickItemMessage = iClickItemMessage;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public InboxViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_inbox,parent, false);
        return new InboxViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHoder holder, int position) {
        PhoneNameInbox phoneNameInbox = mList.get(position);
        if (phoneNameInbox ==null)
            return;
        holder.idTVName.setText(phoneNameInbox.getNAME());
        holder.idTVPhoneNumber.setText(phoneNameInbox.getPHONE_NUMBERS());
        holder.idCVInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemMessage.onClickItemMessage(phoneNameInbox);
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

    public class InboxViewHoder extends RecyclerView.ViewHolder{
        private CardView idCVInbox;
        private TextView idTVName, idTVPhoneNumber;
        public InboxViewHoder(@NonNull View itemView) {
            super(itemView);
            idCVInbox = itemView.findViewById(R.id.idCVInbox);
            idTVName = itemView.findViewById(R.id.idTVName);
            idTVPhoneNumber = itemView.findViewById(R.id.idTVPhoneNumber);
        }
    }
}
