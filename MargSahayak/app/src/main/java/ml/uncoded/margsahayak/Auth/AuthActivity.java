package ml.uncoded.margsahayak.Auth;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

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

//        startActi
// vity(new Intent(AuthActivity.this, Individual_Detail_Activity.class));

        SharedPrefrenceUser mSharedUser = SharedPrefrenceUser.getInstance(this);
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

        }
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
