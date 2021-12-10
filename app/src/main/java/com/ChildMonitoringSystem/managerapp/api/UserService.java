package com.ChildMonitoringSystem.managerapp.api;

import com.ChildMonitoringSystem.managerapp.models.App;
import com.ChildMonitoringSystem.managerapp.models.Audio;
import com.ChildMonitoringSystem.managerapp.models.Contact;
import com.ChildMonitoringSystem.managerapp.models.CreateAccount;
import com.ChildMonitoringSystem.managerapp.models.HistoryCall;
import com.ChildMonitoringSystem.managerapp.models.HistorySignin;
import com.ChildMonitoringSystem.managerapp.models.Images;
import com.ChildMonitoringSystem.managerapp.models.Inbox;
import com.ChildMonitoringSystem.managerapp.models.InfomationPhone;
import com.ChildMonitoringSystem.managerapp.models.LocationOffline;
import com.ChildMonitoringSystem.managerapp.models.PhoneNameInbox;
import com.ChildMonitoringSystem.managerapp.models.User;
import com.ChildMonitoringSystem.managerapp.models.UserRequest;
import com.ChildMonitoringSystem.managerapp.models.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService
{
    @GET("Home/api/User")
    Call<User>getUser(@Query("phone") String phoneUser);
     //Login
     @POST("api/Login")
     Call<User> userLogin(@Body UserRequest userRequest);

     //Create account
     @POST("api/Regis")
     Call<CreateAccount> createUser(@Body CreateAccount createAccount);
     //Loggin history
     @POST("api/postHL")
     Call<HistorySignin>HistorySignIN(@Body HistorySignin historySignin
     );
     //change password
     @POST("api/Forgot")
     Call<UserRequest>changePass(@Body UserRequest userRequest);
     //get list monitor phone
    @GET("Home/api/getinfophonebyPhoneUser")
    Call<List<InfomationPhone>> getListInfoPhone(@Query("phone_user") String key);
     //get list contact from monitor phone
    @GET("Home/api/getphonebySeri")
    Call<List<Contact>> getListContact(@Query("seri") String seriPhone);
    //get list history call from monitor phone
     @GET("Home/api/getcallbySeri")
     Call<List<HistoryCall>> getCallLog(@Query("seri") String key);
     //get list message inbox from monitor phone
     @GET("Home/api/getmess2")
    Call<List<PhoneNameInbox>> getInboxCall(@Query("seri") String key);
     @GET("Home/api/getmess3")
     Call<List<Inbox>>getContentInbox(@Query("num") String phoneNumber,
                                      @Query("seri")String seri);
    //get list images from monitor phone
    @GET("Home/api/GetImage")
    Call<List<Images>> getListImages(@Query("seri")String seriPhone,
    @Query("num") int num);
    @GET("Home/api/getinfoapp")
    Call<List<App>>getListApp(@Query("seri_phone") String seriPhone);
    //get list audio
    @GET("Home/api/GetAudios")
    Call<List<Audio>> getAudio(@Query("seri") String seriPhone);
    //get list video
    @GET("Home/api/GetVideos")
    Call<List<Video>>getListVideo(@Query("seri") String seriPhone);
    @GET("Home/api/getinformation")
    Call<InfomationPhone>getInfo(@Query("seri") String seriPhone);
    // History login
    @GET("Home/api/getHL")
    Call<List<HistorySignin>>getLoginHistory(@Query("num") String phoneNumber);
    //Get location
    @GET("Home/api/GetLocation")
    Call<List<LocationOffline>>getLocation(@Query("seri")String seriPhone);
}
