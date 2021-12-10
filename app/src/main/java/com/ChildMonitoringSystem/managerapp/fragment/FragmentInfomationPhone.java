package com.ChildMonitoringSystem.managerapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.InfomationPhoneAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickInfomationPhone;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentInfomationPhone extends Fragment {
    private View view;
    private RecyclerView rcvInfoPhone;
    private TextView textView;
    private InfomationPhoneAdapter infomationPhoneAdapter;
    private MyShareReference myShareReference;
    private String phoneNumber;
    private MainActivity mMainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_infomation_phone, container,false);
        myShareReference = new MyShareReference(getContext());
        phoneNumber = myShareReference.getValueString("phoneNumber");
        rcvInfoPhone=view.findViewById(R.id.rcv_InfoPhone);
        textView = view.findViewById(R.id.textviewIP);
        infomationPhoneAdapter = new InfomationPhoneAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvInfoPhone.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL);
        rcvInfoPhone.addItemDecoration(itemDecoration);
        mMainActivity = (MainActivity)getActivity();
        assert mMainActivity != null;
        getInfomationPhone(phoneNumber);
        return view;
    }
    private void getInfomationPhone(String phoneUser){
        Call<List<InfomationPhone>>getListInfoPhone = APIClient.getUserService().getListInfoPhone(phoneUser);
        getListInfoPhone.enqueue(new Callback<List<InfomationPhone>>() {
            @Override
            public void onResponse(@NonNull Call<List<InfomationPhone>> call, @NonNull Response<List<InfomationPhone>> response) {
                if (response.isSuccessful()){
                    List<InfomationPhone>list = response.body();
                    assert list != null;
                    if (list.size() == 0){
                        Toast.makeText(getContext(),"Chưa có máy giám sát.",Toast.LENGTH_SHORT).show();
                    }else {
                        textView.setVisibility(View.VISIBLE);
                        infomationPhoneAdapter.setData(list, new IClickInfomationPhone() {
                            @Override
                            public void onClickGoToMenu(InfomationPhone phone) {
                                goToFragmentMenu(phone.getSERI_PHONE(), phone.getNAME_SPY());
                            }
                        });
                        rcvInfoPhone.setAdapter(infomationPhoneAdapter);
                    }
                }
                else{
                    Toast.makeText(getContext(),"Không có kết nối với máy chủ.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<InfomationPhone>> call, Throwable t) {

            }
        });
    }

    private void goToFragmentMenu(String seri_phone,String name_Spy) {
        Intent intent = new Intent(Constan.Action);
        intent.putExtra("seriPhone",seri_phone);
        intent.putExtra("namePhone",name_Spy);
        getContext().sendBroadcast(intent);
    }
}
