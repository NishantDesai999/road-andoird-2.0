package ml.uncoded.margsahayak.Auth;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ml.uncoded.margsahayak.R;

public class resendCountDownCounter extends CountDownTimer {

        TextView mTimerText;
        Button mResendOtp;
        Context c;

        public resendCountDownCounter(long millisInFuture, long countDownInterval, TextView mTimerText, Button mResendOtp,Context c) {
            super(millisInFuture, countDownInterval);
            this.mResendOtp = mResendOtp;
            this.mTimerText = mTimerText;
            this.c = c;
        }
        @Override
        public void onTick(long millisUntilFinished) {
            int s = (int) (millisUntilFinished/1000);
            int seconds = s%60;
            StringBuilder secondsText = new StringBuilder();
            if(seconds/10 < 1){
                secondsText.append("0").append(seconds);
            }else{
                secondsText.append(seconds);
            }
            mTimerText.setText("0"+s/60+" : " +secondsText.toString());
        }

        @Override
        public void onFinish() {
            mTimerText.setText("00:00");
            mResendOtp.setEnabled(true);
            mResendOtp.setTextColor(c.getResources().getColor(R.color.colorBtn));

        }


}
