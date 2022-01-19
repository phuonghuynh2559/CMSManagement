package com.ChildMonitoringSystem.managerapp.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.ChildMonitoringSystem.managerapp.ui.CustomProgess;
import com.ChildMonitoringSystem.managerapp.ui.NotifyProgess;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMap extends NotifyProgess {
    private GoogleMap mMap;
    private DatabaseReference reference;
    private LocationManager manager;
    private Marker myMarke;
    private ImageView btnBack;
    private MainActivity mMainActivity;
    private RadioButton radioButton_online, radioButton_offline, radioButton_InDay, radioButton_FromDay;
    private LinearLayout idLLOptionOffline, idLLFiter, idLL1;
    private String seriPhone;
    private Button idBTNDeleteLocation, idBTNFilter, idBTSetFilter;
    private RadioGroup radioGroup_Location, radioGroup_Filter;
    private EditText idEDInDay, idEDFromDay;
    private TextView idTVUndercore, dateApi1, dateApi2, idTVDateLocation;

    private RecyclerView rcv_InfoPhone;
    private InfomationPhoneAdapter infomationPhoneAdapter;
    private MyShareReference myShareReference;
    private String phoneNumber;
    private Dialog dialog, dialogDownloadTool;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            mMap = googleMap;
            LatLng sydney = new LatLng(10.1020835, 106.671841);
            mMap.clear();
            myMarke = mMap.addMarker(new MarkerOptions().position(sydney).title("Vị trí mặc định"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18), 5000, null);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        dialogDownloadTool = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View mView = inflater.inflate(R.layout.fragment_map, container, false);

        myShareReference = new MyShareReference(getContext());

        phoneNumber = myShareReference.getValueString("phoneNumber");

        mMainActivity = (MainActivity) getActivity();
        mMainActivity.getToolbar().setTitle("Xem vị trí điện thoại");

        btnBack = mView.findViewById(R.id.btnBack);
        rcv_InfoPhone = mView.findViewById(R.id.rcv_InfoPhone);
        radioButton_online = mView.findViewById(R.id.radioButton_online);
        radioButton_offline = mView.findViewById(R.id.radioButton_offline);

        idLLOptionOffline = mView.findViewById(R.id.idLLOptionOffline);
        idLL1 = mView.findViewById(R.id.idLL1);

        idBTNDeleteLocation = mView.findViewById(R.id.idBTNDeleteLocation);
        idBTNFilter = mView.findViewById(R.id.idBTNFilter);

        radioGroup_Location = mView.findViewById(R.id.radioGroup_Location);
        radioGroup_Filter = mView.findViewById(R.id.radioGroup_Filter);

        radioButton_InDay = mView.findViewById(R.id.radioButton_InDay);
        radioButton_FromDay = mView.findViewById(R.id.radioButton_FromDay);

        idLLFiter = mView.findViewById(R.id.idLLFiter);

        idEDInDay = mView.findViewById(R.id.idEDInDay);
        idEDFromDay = mView.findViewById(R.id.idEDFromDay);

        idBTSetFilter = mView.findViewById(R.id.idBTSetFilter);

        idTVUndercore = mView.findViewById(R.id.idTVUndercore);

        dateApi1 = mView.findViewById(R.id.dateApi1);
        dateApi2 = mView.findViewById(R.id.dateApi2);

        idTVDateLocation = mView.findViewById(R.id.idTVDateLocation);

        idEDInDay.setInputType(InputType.TYPE_NULL);
        idEDFromDay.setInputType(InputType.TYPE_NULL);
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
        idLL1.setVisibility(View.GONE);
        idLLFiter.setVisibility(View.GONE);
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
                        OpenDialogNotify(Gravity.CENTER,dialogDownloadTool);
                    } else {
                        infomationPhoneAdapter.setData(mList, new IClickInfomationPhone() {
                            @Override
                            public void onClickGoToMenu(InfomationPhone phone) {
                                seriPhone = phone.getSERI_PHONE();
                                idLL1.setVisibility(View.VISIBLE);
                                radioGroup_Location.setVisibility(View.VISIBLE);
                                radioButton_online.setChecked(false);
                                radioButton_offline.setChecked(false);
                                idLLOptionOffline.setVisibility(View.INVISIBLE);
                                radioGroup_Filter.setVisibility(View.INVISIBLE);
                                idLLFiter.setVisibility(View.GONE);
                                evenClickListener(seriPhone);
                            }

                        });
                        rcv_InfoPhone.setAdapter(infomationPhoneAdapter);
                    }
                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<InfomationPhone>> call, Throwable t) {

            }
        });
    }

    private void evenClickListener(String seriphone) {
        radioButton_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomProgess.OpenDialog(Gravity.CENTER, dialog);
                idLLOptionOffline.setVisibility(View.INVISIBLE);
                radioGroup_Filter.setVisibility(View.INVISIBLE);
                idLLFiter.setVisibility(View.GONE);
                readLocationRealTime(seriphone);
                idTVDateLocation.setVisibility(View.INVISIBLE);
            }
        });
        radioButton_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomProgess.OpenDialog(Gravity.CENTER, dialog);
                idLLOptionOffline.setVisibility(View.VISIBLE);
                readLocationOffLine(seriphone);
                idTVDateLocation.setVisibility(View.INVISIBLE);
                idBTNDeleteLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        idTVDateLocation.setVisibility(View.INVISIBLE);
                        radioGroup_Filter.setVisibility(View.INVISIBLE);
                        idLLFiter.setVisibility(View.GONE);

                        APIClient.getUserService().deleteLocation(seriphone).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                mMap.clear();
                                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                });
                idBTNFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        idTVDateLocation.setVisibility(View.INVISIBLE);
                        radioGroup_Filter.setVisibility(View.VISIBLE);
                        radioButton_InDay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                idLLFiter.setVisibility(View.VISIBLE);
                                idEDInDay.setVisibility(View.VISIBLE);
                                idTVUndercore.setVisibility(View.INVISIBLE);
                                idEDFromDay.setVisibility(View.INVISIBLE);

                                idEDInDay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Calendar calendar = Calendar.getInstance();
                                        DatePickerDialog dpd = new DatePickerDialog(dialog.getContext(),
                                                new DatePickerDialog.OnDateSetListener() {
                                                    @Override
                                                    public void onDateSet(DatePicker view, int year,
                                                                          int monthOfYear, int dayOfMonth) {
                                                        idEDInDay.setText(dayOfMonth + "-"
                                                                + (monthOfYear + 1) + "-" + year);
                                                        dateApi1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                                        Log.d("TAG", "onClick: " + dateApi1.getText().toString());
                                                    }
                                                },
                                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                                        dpd.show();
                                    }
                                });
                                idBTSetFilter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CustomProgess.OpenDialog(Gravity.CENTER, dialog);
                                        String textEDT = idEDInDay.getText().toString().trim();
                                        String textTVdateAPI1 = dateApi1.getText().toString().trim();
                                        if (textEDT.isEmpty()){
                                            CustomProgess.CancleDialog(dialog);
                                            idEDInDay.setError("Bạn chưa điền đủ thông tin!");
                                        }
                                        CallAPI(seriphone,textTVdateAPI1 );
                                    }
                                });
                            }

                        });
                        radioButton_FromDay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                idTVDateLocation.setVisibility(View.INVISIBLE);
                                idLLFiter.setVisibility(View.VISIBLE);
                                idEDInDay.setVisibility(View.VISIBLE);
                                idTVUndercore.setVisibility(View.VISIBLE);
                                idEDFromDay.setVisibility(View.VISIBLE);
                                idEDFromDay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Calendar calendar = Calendar.getInstance();
                                        DatePickerDialog dpd = new DatePickerDialog(dialog.getContext(),
                                                new DatePickerDialog.OnDateSetListener() {
                                                    @Override
                                                    public void onDateSet(DatePicker view, int year,
                                                                          int monthOfYear, int dayOfMonth) {
                                                        idEDFromDay.setText(dayOfMonth + "-"
                                                                + (monthOfYear + 1) + "-" + year);
                                                        dateApi2.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                                        Log.d("TAG", "onClick: " + dateApi2.getText().toString());
                                                    }
                                                },
                                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                                        dpd.show();
                                    }
                                });
                                idBTSetFilter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CustomProgess.OpenDialog(Gravity.CENTER, dialog);
                                        String textEDT1 = idEDInDay.getText().toString().trim();
                                        String textEDT2 = idEDFromDay.getText().toString().trim();
                                        String textTVdateAPI1 = dateApi1.getText().toString().trim();
                                        String textTVdateAPI2 = dateApi2.getText().toString().trim();
                                        if (textEDT1.isEmpty()){
                                            CustomProgess.CancleDialog(dialog);
                                            idEDInDay.setError("Bạn chưa điền đủ thông tin!");
                                        }
                                        if (textEDT2.isEmpty()){
                                            CustomProgess.CancleDialog(dialog);
                                            idEDFromDay.setError("Bạn chưa điền đủ thông tin!");
                                        }
                                        CallAPI2(seriphone, textTVdateAPI1, textTVdateAPI2);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void readLocationRealTime(String seriPhone) {
        reference = FirebaseDatabase.getInstance().getReference().child("/" + seriPhone);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        LocationRealTime location = snapshot.getValue(LocationRealTime.class);
                        if (location != null) {
                            CustomProgess.CancleDialog(dialog);
                            String nameLocation = "";
                            myMarke.setPosition(new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongtitude())));
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongtitude()), 10);
                                if (addresses.size() > 0) {
                                    for (Address adr : addresses) {
                                        if (adr.getAddressLine(0) != null && adr.getAddressLine(0).length() > 0) {
                                            nameLocation = adr.getAddressLine(0);
                                            break;
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //xóa vị trí củ
                            mMap.clear();
                            CustomProgess.CancleDialog(dialog);
                            mMap.addMarker(new MarkerOptions().position(myMarke.getPosition())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(myMarke.getPosition(), 17), 5000, null);
                            String finalNameLocation = nameLocation;
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(@NonNull Marker marker) {
                                    idTVDateLocation.setVisibility(View.VISIBLE);
                                    String dateLoction = "Ngày: "+location.getDateLog();
                                    String nameLocation = "Địa chỉ: "+ finalNameLocation;
                                    idTVDateLocation.setText(dateLoction+"\n"+nameLocation);
                                    return false;
                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.d("TAG", "onDataChange: " + e);
                    }
                } else {
                    idLLOptionOffline.setVisibility(View.INVISIBLE);
                    CustomProgess.CancleDialog(dialog);
                    Toast.makeText(getContext(), "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: " + error);
                Toast.makeText(getContext(), "Điện thoại này không có đồng bộ vị trí.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readLocationOffLine(String seriPhone) {
        mMap.clear();
        APIClient.getUserService().getLocation(seriPhone).enqueue(new Callback<List<LocationOffline>>() {
            @Override
            public void onResponse(Call<List<LocationOffline>> call, Response<List<LocationOffline>> response) {
                if (response.isSuccessful()) {
                    List<LocationOffline> mList = response.body();
                    getApi(mList);
                }
            }

            @Override
            public void onFailure(Call<List<LocationOffline>> call, Throwable t) {

            }
        });
    }

    private void CallAPI(String seri, String date) {
        mMap.clear();
        APIClient.getUserService().getLocationOneDay(seri, date).enqueue(new Callback<List<LocationOffline>>() {
            @Override
            public void onResponse(Call<List<LocationOffline>> call, Response<List<LocationOffline>> response) {
                if (response.isSuccessful()) {
                    List<LocationOffline> mList = response.body();
                    if (mList.size()==0){
                        CustomProgess.CancleDialog(dialog);
                        Toast.makeText(getContext(),"Không có dữ liệu!",Toast.LENGTH_SHORT).show();
                    }else {
                        getApi(mList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LocationOffline>> call, Throwable t) {

            }
        });
    }

    private void CallAPI2(String seri, String fromDay, String toDay) {
        mMap.clear();
        APIClient.getUserService().getLocationFilter(seri, fromDay, toDay).enqueue(new Callback<List<LocationOffline>>() {
            @Override
            public void onResponse(Call<List<LocationOffline>> call, Response<List<LocationOffline>> response) {
                if (response.isSuccessful()) {
                    List<LocationOffline> mList = response.body();
                    getApi(mList);
                }
            }

            @Override
            public void onFailure(Call<List<LocationOffline>> call, Throwable t) {

            }
        });
    }

    private void getApi(List<LocationOffline> mList) {
        if (mList.size() == 0) {
            Toast.makeText(getContext(), "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
            CustomProgess.CancleDialog(dialog);
        } else {
            int i;
            for (i = 0 ; i < mList.size(); i++) {
                String latitude = mList.get(i).getLATITUDE();
                String longtitude = mList.get(i).getLONGTITUDE();
                myMarke.setPosition(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude)));
                if (i==0){
                    mMap.addMarker(new MarkerOptions().position(myMarke.getPosition())
                            .title(mList.get(i).getDATE_LOG())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//                    mMap.addMarker(new MarkerOptions().position(myMarke.getPosition()).title(mList.get(0).getDATE_LOG()).icon(BitmapFromVector(getContext(),R.drawable.ic_circle_start)));
                }else if(i==mList.size() - 1)
                {
                    mMap.addMarker(new MarkerOptions().position(myMarke.getPosition())
                            .title(mList.get(i).getDATE_LOG())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
                mMap.addMarker(new MarkerOptions().position(myMarke.getPosition()).title(mList.get(i).getDATE_LOG()).icon(BitmapFromVector(getContext(),R.drawable.ic_circle)));
                //mMap.addMarker(new MarkerOptions().position(myMarke.getPosition()).title(mList.get(i).getDATE_LOG()));
            }
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    idTVDateLocation.setVisibility(View.VISIBLE);
                    String address = getAddress(String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude));
                    idTVDateLocation.setText("Địa chỉ: "+address);
                    return false;
                }
            });
            CustomProgess.CancleDialog(dialog);
            mMap.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(myMarke.getPosition(), 17), 5000, null);

        }
    }
    private String getAddress(String latitude, String longtitude){
        String nameLocation = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longtitude), 1);
            if (addresses!=null){
                Address retrurnAddress = addresses.get(0);
                StringBuilder builder = new StringBuilder("");
                for (int i = 0; i <= retrurnAddress.getMaxAddressLineIndex(); i++){
                    builder.append(retrurnAddress.getAddressLine(i)).append("\n");
                }
                nameLocation = builder.toString();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return nameLocation;
    }
//    private void showAddress(String latitude, String longtitude){
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(@NonNull Marker marker) {
//                idTVDateLocation.setVisibility(View.VISIBLE);
////                        String dateLoction = "Ngày: "+mList.get().getDATE_LOG();
////                        String nameLocation = "Địa chỉ: "+finalNameLocation;
////                        idTVDateLocation.setText(dateLoction+"\n"+nameLocation);
//                String nameLocation = "";
//                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//                List<Address> addresses;
//                try {
//                    addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longtitude), 1);
//                    if (addresses.size() > 0) {
//                        for (int i = 0 ; i<addresses.get(0).getMaxAddressLineIndex();i++){
//                            nameLocation += addresses.get(0).getAddressLine(i);
//                        }
//                        for (Address adr : addresses) {
//                            if (adr.getAddressLine(0) != null && adr.getAddressLine(0).length() > 0) {
//                                nameLocation = adr.getAddressLine(0);
//                                break;
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Log.d("Địa chỉ vị trí", "onMarkerClick: "+ latitude +" "+ longtitude+" "+ nameLocation);
//                return false;
//            }
//        });
//    }
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private void goToFragmentMenu() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}