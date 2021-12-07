package com.ChildMonitoringSystem.managerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.models.Inbox;

import java.util.List;

public class ContentMessAdapter extends RecyclerView.Adapter<ContentMessAdapter.ContentMessViewHoder> {

    private final int MSG_TYPE_RIGHT = 1;
    private final int MSG_TYPE_LEFT = 0;
    private final String Send_To = "Tin nhắn đến";
    private final String Send = "Tin nhắn gửi đi";
    private List<Inbox> mList;
    private Context context;

    public ContentMessAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<Inbox> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContentMessViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox_right,parent, false);
            return new ContentMessViewHoder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox_left,parent, false);
            return new ContentMessViewHoder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContentMessViewHoder holder, int position) {
        Inbox inbox = mList.get(position);
        if (inbox == null)
        {
            return;
        }
        holder.idTVDateInbox.setText(inbox.getDATE_MESSAGE());
        holder.idTVContentInbox.setText(inbox.getCONTENT());

    }

    @Override
    public int getItemCount() {
        if (mList != null){
            return mList.size();
        }
        return 0;
    }

    public class ContentMessViewHoder extends RecyclerView.ViewHolder{
        private TextView idTVDateInbox,idTVContentInbox;
        public ContentMessViewHoder(@NonNull View itemView) {
            super(itemView);
            idTVDateInbox = itemView.findViewById(R.id.idTVDateInbox);
            idTVContentInbox = itemView.findViewById(R.id.idTVContentInbox);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getTYPE().equals(Send_To))
        {
            return MSG_TYPE_LEFT;
        }else{
            return MSG_TYPE_RIGHT;
        }
    }
}
