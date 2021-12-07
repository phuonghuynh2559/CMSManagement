package com.ChildMonitoringSystem.managerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ChildMonitoringSystem.managerapp.api.APIClient;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentApp;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentAudio;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentCallLog;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentChangePassword;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentContact;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentImages;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentInbox;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentMap;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentMenu;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentUserManual;
import com.ChildMonitoringSystem.managerapp.fragment.FragmentVideo;
import com.ChildMonitoringSystem.managerapp.models.User;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    public Toolbar getToolbar() {
        return toolbar;
    }

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String phoneNumber="";
    public String getPhoneNumber() {
        return phoneNumber;
    }

    private TextView tvNameUser, tvEmailUser, tvTotalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawerlayout);
        navigationView=findViewById(R.id.navigation);
        initUI();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            User user = (User) bundle.get("data");
            if (user != null){
                phoneNumber = user.getPHONE_NUMBERS();
            }
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        loadMenuMain();
        navigationView.setNavigationItemSelectedListener(this);
        showUserInfomation(phoneNumber);
    }
    public void loadMenuMain(){
        toolbar.setTitle("Danh sách chức năng");
        FragmentMenu fragmentMenu = new FragmentMenu();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentMenu);
        fragmentTransaction.commit();
    }
    public void initUI(){
        tvNameUser = navigationView.getHeaderView(0).findViewById(R.id.tvNameUser);
        tvEmailUser = navigationView.getHeaderView(0).findViewById(R.id.tvEmailUser);
        tvTotalUser = navigationView.getHeaderView(0).findViewById(R.id.tvTotalUser);
    }
    public void showUserInfomation(String phoneNumber){
        Call<User> getUser = APIClient.getUserService().getUser(phoneNumber);
        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User mUser = response.body();
                    if (mUser == null){
                        tvNameUser.setVisibility(View.GONE);
                        tvEmailUser.setVisibility(View.GONE);
                        tvTotalUser.setVisibility(View.GONE);
                    }else{
                        tvNameUser.setText(mUser.getNAME());
                        tvEmailUser.setText(mUser.getEMAIL());
                        tvTotalUser.setText(String.valueOf(mUser.getTOTAL_PHONE_MONITOR()));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Không có kết nối với sever.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.nav_contact){
            FragmentContact fragmentContact = new FragmentContact();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentContact);
            fragmentTransaction.commit();
        }
        if (id==R.id.nav_history){
            FragmentCallLog fragmentCallLog = new FragmentCallLog();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentCallLog);
            fragmentTransaction.commit();
        }
        if (id==R.id.nav_inbox){
            FragmentInbox fragmentInbox = new FragmentInbox();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentInbox);
            fragmentTransaction.commit();
        }
        if (id==R.id.nav_image){
            FragmentImages fragmentImages = new FragmentImages();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentImages);
            fragmentTransaction.commit();
        }
        if (id==R.id.nav_video){
            FragmentVideo fragmentVideo = new FragmentVideo();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentVideo);
            fragmentTransaction.commit();
        }
        if (id==R.id.nav_location){
            FragmentMap fragmentMap = new FragmentMap();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentMap);
            fragmentTransaction.commit();
        }
        if (id==R.id.nav_app){
            FragmentApp fragmentApp = new FragmentApp();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentApp);
            fragmentTransaction.commit();
        }
        if (id==R.id.nav_audio){
            FragmentAudio fragmentAudio = new FragmentAudio();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentAudio);
            fragmentTransaction.commit();
        }
        if (id==R.id.nav_User_Manual){
            FragmentUserManual fragmentUserManual = new FragmentUserManual();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentUserManual);
            fragmentTransaction.commit();
        }
        if (id==R.id.nav_account){
            FragmentChangePassword fragmentChangePassword = new FragmentChangePassword();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentChangePassword);
            fragmentTransaction.commit();
        }
        if(id==R.id.nav_monitor)
        {
            Toast.makeText(MainActivity.this, "Chức Năng Đang Được Phát Triển !", Toast.LENGTH_SHORT).show();
//            FragmentMonitorManage fragmentMonitorManage = new FragmentMonitorManage();
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.frameLayoutMenu,fragmentMonitorManage);
//            fragmentTransaction.commit();
        }
        if (id==R.id.nav_log_out){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
    //Sự kiện out khi drawer layout đg mở
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    public int checkInternet(){
        boolean wifiConected;
        boolean mobileConnected;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnected()){
            wifiConected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConected){
                return 1;
            }else if(mobileConnected){
                return 2;
            }
        }
        return 0;
    }
}