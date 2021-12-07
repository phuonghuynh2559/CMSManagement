package com.ChildMonitoringSystem.managerapp.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ChildMonitoringSystem.managerapp.MainActivity;
import com.ChildMonitoringSystem.managerapp.R;
import com.ChildMonitoringSystem.managerapp.constan.Constan;
import com.ChildMonitoringSystem.managerapp.models.MyLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class FragmentMap extends Fragment {
    private GoogleMap mMap;
    private DatabaseReference reference;
    private LocationManager manager;
    private Marker myMarke;
    private ImageButton btnBack;
    private MainActivity mMainActivity;
    private String mySeriPhone = "";
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
        btnBack =mView.findViewById(R.id.btnBack);
        mMainActivity = (MainActivity)getActivity();
        mMainActivity.getToolbar().setTitle("Xem vị trí điện thoại");
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFrameLayout();
        goToFragmentMenu();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    private void loadFrameLayout(){
        FragmentInfomationPhone infomationPhone = new FragmentInfomationPhone();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayoutMap,infomationPhone);
        fragmentTransaction.commit();
    }
    private BroadcastReceiver seriPhone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constan.Action.equals(intent.getAction())){
                mySeriPhone = intent.getStringExtra("seriPhone");
                readLocation(mySeriPhone);
            }
        }
    };
    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constan.Action);
        requireActivity().registerReceiver(seriPhone,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(seriPhone);
    }
    private void readLocation(String mySeriPhone) {
        reference = FirebaseDatabase.getInstance().getReference().child("/"+mySeriPhone);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    try {
                        MyLocation location = snapshot.getValue(MyLocation.class);
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
                            mMap.addMarker(new MarkerOptions().position(myMarke.getPosition()).title(nameLocation));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarke.getPosition(), 18),5000,null);

                        }
                    }catch (Exception e){
                        Toast.makeText(getContext(),"Điện thoại này không có đồng bộ vị trí.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Điện thoại này không có đồng bộ vị trí.",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void goToFragmentMenu() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMenu fragmentMenu = new FragmentMenu();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentMenu);
                fragmentTransaction.commit();
            }
        });
    }
}