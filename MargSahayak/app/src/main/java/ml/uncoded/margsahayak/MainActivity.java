package ml.uncoded.margsahayak;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import io.realm.Realm;
import ml.uncoded.margsahayak.History.NotificationActivity;
import ml.uncoded.margsahayak.Input.InputActivity2;
import ml.uncoded.margsahayak.Input.MyLocationListener;
import ml.uncoded.margsahayak.Network.Api;
import ml.uncoded.margsahayak.Network.mCallBack;
import ml.uncoded.margsahayak.models.StaticMethods;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivty";
    public Api mApi;
    ImageView mNotification, mOptions;
    Button mAddComplainButton;
    View mBottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initListners();
        StaticMethods.permissionmethod(MainActivity.this);
    }

    public void init(){
        mNotification=(ImageView)findViewById(R.id.notificationIcon);
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

                          MyLocationListener locationListener = MyLocationListener.getInstance();

                          ((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                          .requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
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

}
