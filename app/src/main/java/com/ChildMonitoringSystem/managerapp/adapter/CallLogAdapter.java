package com.ChildMonitoringSystem.managerapp.adapter;

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
import com.ChildMonitoringSystem.managerapp.models.HistoryCall;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickHistoryCall;

import java.util.List;

public class CallLogAdapter extends  RecyclerView.Adapter<CallLogAdapter.CallViewHoder> {
    private Context mContext;
    private List<HistoryCall> mList;
    private IClickHistoryCall iClickHistoryCall;
    public CallLogAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(List<HistoryCall> list,IClickHistoryCall iClickHistoryCall){
        this.mList = list;
        this.iClickHistoryCall = iClickHistoryCall;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CallViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_call,parent, false);
        return new CallViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHoder holder, int position) {
        HistoryCall historyCall = mList.get(position);
        if (historyCall==null)
            return;
        holder.tv_name.setText(historyCall.getCONTACT_NAME());
        holder.tv_phone.setText(historyCall.getPHONE_NUMBER());
        holder.tv_type.setText(historyCall.getCALL_TYPE());
        holder.tv_date.setText(historyCall.getCALL_DATE());
        holder.tv_time.setText(historyCall.getCALL_DURATION());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickHistoryCall.onClickHistoryCall(historyCall);
            }
        });
    }

    @Override
    public int getItemCount() {
        if ( mList!=null){
            return mList.size();
        }
        return 0;
    }

    public class CallViewHoder extends RecyclerView.ViewHolder{
        private ImageView img;
        private CardView cardView;
        private TextView tv_name, tv_phone, tv_type, tv_date, tv_time;
        public CallViewHoder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_view);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone_nb);
            tv_type = itemView.findViewById(R.id.tv_type_call);
            tv_date = itemView.findViewById(R.id.tv_date_call);
            tv_time = itemView.findViewById(R.id.tv_time_call);
            cardView = itemView.findViewById(R.id.cv_History_Call);
        }
    }
}
