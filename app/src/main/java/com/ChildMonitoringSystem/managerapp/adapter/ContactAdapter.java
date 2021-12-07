package com.ChildMonitoringSystem.managerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.models.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHoder>  {
    private Context mContext;
    private List<Contact> mList;
    public ContactAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setData(List<Contact> list){
        this.mList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ContactViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,parent, false);
        return new ContactViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHoder holder, int position) {
        Contact contact = mList.get(position);
        if (contact==null)
            return;
        holder.tv_name.setText(contact.getNAME());
        holder.tv_phone.setText(contact.getPHONE_NUMBERS());
    }

    @Override
    public int getItemCount() {
        if ( mList!=null){
            return mList.size();
        }
        return 0;
    }

    public class ContactViewHoder extends RecyclerView.ViewHolder{
        private TextView tv_name, tv_phone;
        public ContactViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone_nb);
        }
    }
}
