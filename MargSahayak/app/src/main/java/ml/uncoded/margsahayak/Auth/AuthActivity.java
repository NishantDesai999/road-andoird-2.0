package ml.uncoded.margsahayak.Auth;

import android.app.Notification;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Locale;

import ml.uncoded.margsahayak.FilterActivity;
import ml.uncoded.margsahayak.Individual_Detail_Activity;
import ml.uncoded.margsahayak.Input.InputActivity2;
import ml.uncoded.margsahayak.MainActivity;
import ml.uncoded.margsahayak.R;

public class AuthActivity extends AppCompatActivity {

    FrameLayout mFrameLayout;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //startActivity(new Intent(AuthActivity.this, FilterActivity.class));

        SharedPrefrenceUser mSharedUser = SharedPrefrenceUser.getInstance(this);

        //setting apps language
        setAppLanguage(SharedPrefrenceUser.getLanguage());


        if(mSharedUser.getState().equals(Constant.home_state)){
            Intent i = new Intent(AuthActivity.this,MainActivity.class);
            startActivity(i);
        }

        setContentView(R.layout.activity_auth);
        mFrameLayout = (FrameLayout) findViewById(R.id.auth_framelayout);


        FragmentManager fm = getSupportFragmentManager();

        switch (mSharedUser.getState()) {
            case Constant.login_state:
                fm.beginTransaction()
                        .replace(R.id.auth_framelayout, new LoginFragment())
                        .commit();
                break;
            case Constant.otp_state:
                if (fm.findFragmentById(R.id.login_fragment) != null)
                    fm.beginTransaction().remove(fm.findFragmentById(R.id.login_fragment));

                fm.beginTransaction()
                        .replace(R.id.auth_framelayout, new OtpScreenFragment())
                        .commit();
                break;
            case Constant.register_state:
                fm.beginTransaction()
                        .replace(R.id.auth_framelayout, new RegisterFragment()).commit();
                break;
            default:
                finish();

        }
    }

    public void setAppLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }


    @Override
    public void onBackPressed() {

        Toast  toast = Toast.makeText(this, "Press back again to close this app",Toast.LENGTH_SHORT);
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
