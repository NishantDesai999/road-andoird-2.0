package ml.uncoded.margsahayak.Auth;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ml.uncoded.margsahayak.MainActivity;
import ml.uncoded.margsahayak.MainApplication;
import ml.uncoded.margsahayak.models.BisagResponse;
import ml.uncoded.margsahayak.models.OfflineComplainModel;
import ml.uncoded.margsahayak.models.StaticMethods;
import ml.uncoded.margsahayak.Network.mCallBack;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.AuthResponse;


public class OtpScreenFragment extends Fragment {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public View mPhoneEdit;
    public EditText mOtpEdt;
    private Button mVerifyBtn;
    private String mOtpNumber;
    private ProgressBar mProgresBar;
    private Button mResendOtp;
    private TextView mTimerText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_otp_screen, container, false);
        mOtpEdt = (EditText) v.findViewById(R.id.otp_input);
        mVerifyBtn = (Button) v.findViewById(R.id.btn_input_upload);
        mProgresBar = (ProgressBar) v.findViewById(R.id.otp_progressbar);
        mResendOtp = v.findViewById(R.id.resend_otp);
        mTimerText = v.findViewById(R.id.timer_text);
        mPhoneEdit = v.findViewById(R.id.mobileNoEdit);
        ((TextView)mPhoneEdit.findViewById(R.id.mobileEditText)).setText(SharedPrefrenceUser.getInstance(getActivity()).getPhone());
        initListners();
        mResendOtp.setEnabled(false);
        mResendOtp.setTextColor(Color.GRAY);

        if (checkAndRequestPermissions()) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("otp"));
        }

        resendCountDownCounter mCount = new resendCountDownCounter(30000,1000,mTimerText,mResendOtp,getActivity());
        mCount.start();
        return v;

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                String message = intent.getStringExtra("message");
                if(message.toLowerCase().contains("marg sahayak")) {
                    String[] temp = message.split(" ");
                    message = temp[temp.length-1];
                    mOtpNumber = message;
                    mOtpEdt.setText(message);
                    checkOtp();
                }
            }
        }
    };


    private void initListners() {
        mOtpEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    checkOtp();
                    return true;
                }
                return false;
            }
        });

        mPhoneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefrenceUser.setStateLogin();
                FragmentTransaction mFragmentTransition = getActivity().getSupportFragmentManager().beginTransaction();
                mFragmentTransition.replace(R.id.auth_framelayout, new LoginFragment());
                mFragmentTransition.addToBackStack(null);
                mFragmentTransition.commit();
            }
        });
        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                checkOtp();
            }
        });
        mResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticMethods.checkInternetConnectivity(getActivity())) {

                    mOtpEdt.setEnabled(false);
                    mVerifyBtn.setEnabled(false);
                    mResendOtp.setEnabled(false);
                    mResendOtp.setTextColor(Color.GRAY);
                    mVerifyBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SCREEN);
                    StaticMethods.hideSoftKeyboard(getActivity());

                    mProgresBar.setVisibility(View.VISIBLE);
                    ((MainApplication) getActivity().getApplicationContext()).mApi
                            .login(SharedPrefrenceUser.getInstance(getActivity()).getPhone())
                            .enqueue(new mCallBack<AuthResponse>(getActivity(), mProgresBar) {
                                @Override
                                public void onSuccessfullResponse(View progressBar, AuthResponse mResponse, Context c) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    ((AuthActivity)c).findViewById(R.id.otp_input).setEnabled(true);
                                    ((AuthActivity)c).findViewById(R.id.resend_otp).setEnabled(false);
                                    ((AuthActivity)c).findViewById(R.id.btn_input_upload).setEnabled(true);
                                    ((AuthActivity)c).findViewById(R.id.btn_input_upload).setBackground(c.getResources().getDrawable(R.drawable.btn_style));
                                    StaticMethods.showSoftKeyboard((Activity) c,((AuthActivity)c).findViewById(R.id.otp_input));

                                    resendCountDownCounter mCount = new resendCountDownCounter(12000,1000,mTimerText,mResendOtp,getActivity());
                                    mCount.start();

                                }

                                @Override
                                public void onFailureResponse(View progressBar, Context c) {
                                    ((AuthActivity)c).findViewById(R.id.otp_input).setEnabled(true);
                                    ((AuthActivity)c).findViewById(R.id.resend_otp).setEnabled(true);
                                    ((AuthActivity)c).findViewById(R.id.btn_input_upload).setEnabled(true);
                                    ((AuthActivity)c).findViewById(R.id.btn_input_upload).setBackground(c.getResources().getDrawable(R.drawable.btn_style));
                                    StaticMethods.showSoftKeyboard((Activity) c,((AuthActivity)c).findViewById(R.id.otp_input));
                                }

                            });

                }


            }
        });

    }


    private void checkOtp() {

        mOtpNumber = mOtpEdt.getText().toString();
        mOtpEdt.setError(null);

        boolean cancel = false;

        if (TextUtils.isEmpty(mOtpNumber)) {
            mOtpEdt.setError(getString(R.string.error_required_field));
            cancel = true;
        } else if (!(Pattern.matches("[0-9]+", mOtpNumber) && (mOtpNumber.length() == 4))) {
            mOtpEdt.setError(getString(R.string.error_OTP_size_invalid));
            cancel = true;
        }

        if (cancel) {
            mOtpEdt.requestFocus();
        } else {

            if (StaticMethods.checkInternetConnectivity(getActivity())) {

                mOtpEdt.setEnabled(false);
                mVerifyBtn.setEnabled(false);
                mVerifyBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SCREEN);
                StaticMethods.hideSoftKeyboard(getActivity());
                mProgresBar.setVisibility(View.VISIBLE);
                ((MainApplication) getActivity().getApplicationContext()).mApi
                        .otp(mOtpNumber, SharedPrefrenceUser.getInstance(getActivity()).getPhone())
                        .enqueue(new mCallBack<AuthResponse>(getActivity(), mProgresBar) {
                            @Override
                            public void onSuccessfullResponse(View progressBar, AuthResponse mResponse, Context c) {
                                if (Boolean.parseBoolean(mResponse.success)) {

                                    Log.d("responseTrue", "onSuccessfullResponse: "+mResponse.status);
                                    if(mResponse.status.toLowerCase().contains("true")) {
                                        SharedPrefrenceUser.setStateOtpToRegister(mResponse.data);

                                        progressBar.setVisibility(View.INVISIBLE);
                                        FragmentTransaction mFragmentTransition = getActivity().getSupportFragmentManager().beginTransaction();
                                        mFragmentTransition.replace(R.id.auth_framelayout, new RegisterFragment());
                                        mFragmentTransition.addToBackStack(null);
                                        mFragmentTransition.commit();
                                    }else{
                                        SharedPrefrenceUser.setStateRegisterOrOTPToHome(mResponse.status,mResponse.data);
                                        c.startActivity(new Intent(c,MainActivity.class));
                                    }
                                }else{
                                    EditText e = (EditText)((AuthActivity)c).findViewById(R.id.otp_input);
                                    e.setText("");
                                    e.setError(mResponse.data);
                                    e.requestFocus();
                                    ((AuthActivity)c).findViewById(R.id.otp_input).setEnabled(true);
                                    ((AuthActivity)c).findViewById(R.id.btn_input_upload).setEnabled(true);
                                    ((AuthActivity)c).findViewById(R.id.btn_input_upload).setBackground(c.getResources().getDrawable(R.drawable.btn_style));
                                    StaticMethods.showSoftKeyboard((Activity) c,((AuthActivity)c).findViewById(R.id.otp_input));
                                    progressBar.setVisibility(View.INVISIBLE);

                                }

                            }

                            @Override
                            public void onFailureResponse(View progressBar, Context c) {

                                ((AuthActivity)c).findViewById(R.id.otp_input).setEnabled(true);
                                ((AuthActivity)c).findViewById(R.id.btn_input_upload).setEnabled(true);
                                ((AuthActivity)c).findViewById(R.id.btn_input_upload).setBackground(c.getResources().getDrawable(R.drawable.btn_style));
                                StaticMethods.showSoftKeyboard((Activity) c,((AuthActivity)c).findViewById(R.id.otp_input));
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        });

            }
        }

    }


    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void onResume() {

//        if(!getActivity().isDestroyed()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                IntentFilter i = new IntentFilter();
//                i.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//                i.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
//                getActivity().registerReceiver(receiver, i);
//            }


            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }
}
