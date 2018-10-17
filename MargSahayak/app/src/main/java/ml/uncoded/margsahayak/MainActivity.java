package ml.uncoded.margsahayak;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import ml.uncoded.margsahayak.Auth.SharedPrefrenceUser;
import ml.uncoded.margsahayak.History.NotificationActivity;
import ml.uncoded.margsahayak.History.NotificationListDataAdapter;
import ml.uncoded.margsahayak.Input.InputActivity2;
import ml.uncoded.margsahayak.Input.MyLocationListener;
import ml.uncoded.margsahayak.Input.SingletonLocationManager;
import ml.uncoded.margsahayak.Network.Api;
import ml.uncoded.margsahayak.Network.mCallBack;
import ml.uncoded.margsahayak.models.ComplainModel;
import ml.uncoded.margsahayak.models.Dialogs;
import ml.uncoded.margsahayak.models.NotificationComplaintModel;
import ml.uncoded.margsahayak.models.StaticMethods;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivty";
    public Api mApi;
    ImageView mNotification, mOptions;
    Button mAddComplainButton;
    View mBottomLayout;
    TextView mNotificationCount;
    private long lastBackPressTime = 0;
    private String strErrMsg;
    private Dialogs dialogs;
    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initListners();
        onRefreshNotification();
        StaticMethods.permissionmethod(MainActivity.this);

    }

    public void onRefreshNotification(){

        boolean flag = checkInternetConnectivity(MainActivity.this);
        if (!flag) {
        } else {
            MainApplication m = (MainApplication) getApplicationContext();
            m.mApi.getComplaintNotification(SharedPrefrenceUser.getInstance(MainActivity.this).getToken()).enqueue(new Callback<List<NotificationComplaintModel>>() {
                private final String TAG = "complain";
                @Override
                public void onResponse(Call<List<NotificationComplaintModel>> call, final Response<List<NotificationComplaintModel>> response) {
                    if (response.isSuccessful()&& response.body().size()!=0) {
                        mNotificationCount.setVisibility(View.VISIBLE);
                        mNotificationCount.setText(String.valueOf(response.body().size()));
                        Log.d(TAG, "response : " + response.headers());
                        final Realm r = Realm.getDefaultInstance();
                        r.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                List<NotificationComplaintModel> notificationList = response.body();
                                ComplainModel complainModel;
                                for (NotificationComplaintModel notificationComplaintModel : notificationList) {
                                    Log.d(TAG, "execute: "+notificationComplaintModel.getId());
                                    complainModel = realm.where(ComplainModel.class).equalTo("id", notificationComplaintModel.getId()).findFirst();
                                    if(notificationComplaintModel.getComplaintStatus()!=null)
                                        complainModel.setComplaintStatus(notificationComplaintModel.getComplaintStatus());
                                    if(notificationComplaintModel.getComments()!=null)
                                        complainModel.setComment(notificationComplaintModel.getComments());
                                    if(notificationComplaintModel.getEstimatedDate()!=null)
                                        complainModel.setEstimatedTime(notificationComplaintModel.getEstimatedDate());
                                    if(notificationComplaintModel.getOfficerEmail()!=null)
                                        complainModel.setOfficerEmail(notificationComplaintModel.getOfficerEmail());
                                    if(notificationComplaintModel.getOfficerId()!=null)
                                        complainModel.setOfficerId(notificationComplaintModel.getOfficerId());
                                    if(notificationComplaintModel.getOfficerName()!=null)
                                        complainModel.setOfficerName(notificationComplaintModel.getOfficerName());
                                    notificationComplaintModel.setImgUrl(complainModel.getUrl());
                                    Log.d("test", "execute: " + notificationComplaintModel.getEstimatedDate());
                                    realm.copyToRealm(notificationComplaintModel);
                                    realm.insertOrUpdate(complainModel);
                                }


                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {



                            }
                        });

//                                Log.d(TAG, "Db : " + r.where(ComplainModel.class).findAll().size());

                    }
                    //Checking server Internal Errors
                    else {
//                        Log.d(TAG, "Code: " + response.code() + " Message: " + response.message());
//                        Toast.makeText(MainActivity.this, "" + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
//                        strErrMsg = response.code() + " " + response.message();
//                        dialogs = new Dialogs(strErrMsg);
//                        dialogs.show(getFragmentManager(), "ErrMSG");
                    }
                }

                @Override
                public void onFailure(Call<List<NotificationComplaintModel>> call, Throwable t) {

//
//                    t.printStackTrace();
//                    Log.d(TAG, "OnFailure");
//                    Toast.makeText(MainActivity.this, "" + t.getCause() + " :: " + " ---> " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    if (t.getCause() != null && t.getCause().toString().contains("java.net.ConnectException")) {
//                        strErrMsg = "FAILED TO CONNECT TO SERVER\nNETWORK IS UNREACHABLE";
//                        dialogs = new Dialogs(strErrMsg);
//                    } else if (t.getCause() == null) {
//
//                        strErrMsg = "CONNECTION TIMED OUT";
//                        dialogs = new Dialogs(strErrMsg);
//                    } else {
//                        strErrMsg = "JSON PARSING ERROR";
//                        dialogs = new Dialogs(strErrMsg);
//                    }
//                    dialogs.show(getFragmentManager(), "ErrMSG");
                }
            });

        }

    }
    public static boolean checkInternetConnectivity(Context c){

        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WifiManager wifi = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
        if(!(wifi.isWifiEnabled() || mobileDataEnabled)) {
            return false;
        }
        return true; //Internet is On
    }
    public void init(){
        mNotification=(ImageView)findViewById(R.id.notificationIcon);
        mNotificationCount=findViewById(R.id.badge_notification_1);
        mOptions=(ImageView)findViewById(R.id.optionsIcon);
        mAddComplainButton=(Button)findViewById(R.id.add_new_complain_btn);
        mBottomLayout = findViewById(R.id.bottomLayout);
}

    public void initListners(){


        mAddComplainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   startActivity(new Intent(MainActivity.this,Main2Activity.class));

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {

                          MyLocationListener locationListener = MyLocationListener.getInstance("mAIN Activity");
                          LocationManager l = SingletonLocationManager.getInstance(getApplication(),"From Main Activity");
                          l.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
                }

                startActivity(new Intent(MainActivity.this,InputActivity2.class));

            }
        });

        mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(i);

            }
        });
        mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionsBottomSheet optionsBottomSheet=OptionsBottomSheet.getInstance();
                optionsBottomSheet.show(getSupportFragmentManager(),"First Bottom sheet");

            }
        });
    }
    @Override
    public void onBackPressed() {

        Toast toast = Toast.makeText(this, "Press back again to close this app",Toast.LENGTH_SHORT);
        if (this.lastBackPressTime < System.currentTimeMillis() - 2000) {
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            this.lastBackPressTime = 0;
            finish();
        }
    }

}
