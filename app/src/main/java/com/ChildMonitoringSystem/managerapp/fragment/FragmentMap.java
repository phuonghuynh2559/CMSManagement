package com.ChildMonitoringSystem.managerapp.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.adapter.InfomationPhoneAdapter;
import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.models.LocationRealTime;
import com.ChildMonitoringSystem.managerapp.models.LocationOffline;
import com.ChildMonitoringSystem.managerapp.my_interface.IClickInfomationPhone;
import com.ChildMonitoringSystem.managerapp.sharereferen.MyShareReference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMap extends Fragment {
    private GoogleMap mMap;
    private DatabaseReference reference;
    private LocationManager manager;
    private Marker myMarke;
    private ImageView btnBack;
    private MainActivity mMainActivity;

    private RecyclerView rcv_InfoPhone;
    private InfomationPhoneAdapter infomationPhoneAdapter;
    private MyShareReference myShareReference;
    private String phoneNumber;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            LatLng sydney = new LatLng(10.1020835,106.671841);
            mMap.clear();
            myMarke = mMap.addMarker(new MarkerOptions().position(sydney).title(""));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,18),5000,null);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_map, container, false);
        myShareReference = new MyShareReference(getContext());
        phoneNumber = myShareReference.getValueString("phoneNumber");

        mMainActivity = (MainActivity)getActivity();
        mMainActivity.getToolbar().setTitle("Xem vị trí điện thoại");

        btnBack =mView.findViewById(R.id.btnBack);
        rcv_InfoPhone =mView.findViewById(R.id.rcv_InfoPhone);

        infomationPhoneAdapter = new InfomationPhoneAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_InfoPhone.setLayoutManager(gridLayoutManager);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        goToFragmentMenu();
        loadListPhoneMonitor(phoneNumber);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void loadListPhoneMonitor(String phoneNumber) {
        APIClient.getUserService().getListInfoPhone(phoneNumber).enqueue(new Callback<List<InfomationPhone>>() {
            @Override
            public void onResponse(Call<List<InfomationPhone>> call, Response<List<InfomationPhone>> response) {
                if (response.isSuccessful()) {
                    List<InfomationPhone> mList = response.body();
                    if (mList.size() == 0) {
                        Toast.makeText(getContext(), "Không có máy giám sát nào!", Toast.LENGTH_SHORT).show();
                    } else {
                        infomationPhoneAdapter.setData(mList, new IClickInfomationPhone() {
                            @Override
                            public void onClickGoToMenu(InfomationPhone phone) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        drawLocation(phone.getSERI_PHONE());
                                    }
                                }, 1000);
                            }

                        });
                        rcv_InfoPhone.setAdapter(infomationPhoneAdapter);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<InfomationPhone>> call, Throwable t) {

            }
        });
    }
    private void drawLocation(String seriPhone){
        readLocationRealTime(seriPhone);
        readLocationOffLine(seriPhone);
    }

    private void readLocationRealTime(String seriPhone) {
        reference = FirebaseDatabase.getInstance().getReference().child("/"+seriPhone);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    try {
                        LocationRealTime location = snapshot.getValue(LocationRealTime.class);
                        if (location != null){
                            String nameLocation="";
                            myMarke.setPosition(new LatLng(Double.parseDouble(location.getLatitude()),Double.parseDouble(location.getLongtitude())));
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(Double.parseDouble(location.getLatitude()),Double.parseDouble(location.getLongtitude()),10);
                                if (addresses.size() >0){
                                    for (Address adr: addresses){
                                        if (adr.getAddressLine(0) !=null && adr.getAddressLine(0).length() >0){
                                            nameLocation = adr.getAddressLine(0);
                                            break;
                                        }
                                    }
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            //xóa vị trí củ
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(myMarke.getPosition())
                                    .title(nameLocation)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarke.getPosition(), 18),5000,null);

                        }
                    }catch (Exception e){
                        Log.d("TAG", "onDataChange: "+e);
                        Toast.makeText(getContext(),"Điện thoại này không có đồng bộ vị trí.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: "+error);
                Toast.makeText(getContext(),"Điện thoại này không có đồng bộ vị trí.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readLocationOffLine(String seriPhone) {
        APIClient.getUserService().getLocation(seriPhone).enqueue(new Callback<List<LocationOffline>>() {
            @Override
            public void onResponse(Call<List<LocationOffline>> call, Response<List<LocationOffline>> response) {
                if (response.isSuccessful()){
                    List<LocationOffline>mList = response.body();
                    if (mList.size()==0){
                    }else{
                        for (int i = 0 ; i< mList.size();i++){
                            String nameLocation="";
                            myMarke.setPosition(new LatLng(Double.parseDouble(mList.get(i).getLATITUDE()),Double.parseDouble(mList.get(i).getLONGTITUDE())));
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(Double.parseDouble(mList.get(i).getLATITUDE()),Double.parseDouble(mList.get(i).getLONGTITUDE()),10);
                                if (addresses.size() >0){
                                    for (Address adr: addresses){
                                        if (adr.getAddressLine(0) !=null && adr.getAddressLine(0).length() >0){
                                            String type = " Vị trí đã từng đi qua: ";
                                            nameLocation = adr.getAddressLine(0)+type + mList.get(i).getDATE_LOG();
                                            break;
                                        }
                                    }
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            mMap.addMarker(new MarkerOptions().position(myMarke.getPosition()).title(nameLocation));
                        }
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarke.getPosition(), 18),5000,null);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LocationOffline>> call, Throwable t) {

            }
        });
    }

    private void goToFragmentMenu() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}