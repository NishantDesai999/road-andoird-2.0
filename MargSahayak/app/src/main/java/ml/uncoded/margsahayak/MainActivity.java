package ml.uncoded.margsahayak;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

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

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
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
                                //if(notificationList.size()>0) {
                                    for (NotificationComplaintModel notificationComplaintModel : notificationList) {
                                        Log.d(TAG, "execute: " + notificationComplaintModel.getId());
                                        complainModel = realm.where(ComplainModel.class).equalTo("id", notificationComplaintModel.getId()).findFirst();
                                        if (notificationComplaintModel.getComplaintStatus() != null)
                                            complainModel.setComplaintStatus(notificationComplaintModel.getComplaintStatus());
                                        if (notificationComplaintModel.getComments() != null)
                                            complainModel.setComment(notificationComplaintModel.getComments());
                                        if (notificationComplaintModel.getEstimatedDate() != null)
                                            complainModel.setEstimatedTime(notificationComplaintModel.getEstimatedDate());
                                        if (notificationComplaintModel.getOfficerEmail() != null)
                                            complainModel.setOfficerEmail(notificationComplaintModel.getOfficerEmail());
                                        if (notificationComplaintModel.getOfficerId() != null)
                                            complainModel.setOfficerId(notificationComplaintModel.getOfficerId());
                                        if (notificationComplaintModel.getOfficerName() != null)
                                            complainModel.setOfficerName(notificationComplaintModel.getOfficerName());
                                        notificationComplaintModel.setImgUrl(complainModel.getUrl());
                                        Log.d("test", "execute: " + notificationComplaintModel.getEstimatedDate());
                                        realm.copyToRealm(notificationComplaintModel);
                                        realm.insertOrUpdate(complainModel);
                                    }
                                }

                            //}
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

                        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                                .addApi(LocationServices.API)
                                .build();
                        mGoogleApiClient.connect();

                          if ((!l.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                            Log.v(TAG,"build called...");
//                            buildAlertMessageNoGps();
                            showSettingDialog();
                        }else {
                            l.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
                            startActivity(new Intent(MainActivity.this, InputActivity2.class));
                        }

                }

            }
        });

        mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotificationCount.setVisibility(View.GONE);
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

    protected void buildAlertMessageNoGps() {
        Log.v("tesing", "building alert dialog");
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),17);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
//                        Intent i = new Intent(getActivity(),navigationMain.class);
//                        startActivity(i);
                        Toast.makeText(MainActivity.this,
                                "User cancelled Location capture", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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


    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        //startLocationUpdates();
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        break;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);
    }

    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            showSettingDialog();
        }
    };

    /* Broadcast receiver to check status of GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");
                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    // showSettingDialog();
                    Log.e("About GPS", "GPS is Disabled in your device");
                }

            }
        }
    };

    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //If permission granted show location dialog if APIClient is not null
                    if (mGoogleApiClient == null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                                .addApi(LocationServices.API)
                                .build();
                        mGoogleApiClient.connect();
                        showSettingDialog();
                    } else
                        showSettingDialog();


                } else {
                    Toast.makeText(MainActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
