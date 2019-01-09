package ml.uncoded.margsahayak.Auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.regex.Pattern;

import ml.uncoded.margsahayak.MainActivity;
import ml.uncoded.margsahayak.MainApplication;
import ml.uncoded.margsahayak.models.BisagResponse;
import ml.uncoded.margsahayak.models.OfflineComplainModel;
import ml.uncoded.margsahayak.models.StaticMethods;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.AuthResponse;
import ml.uncoded.margsahayak.Network.mCallBack;


public class RegisterFragment extends Fragment {

    EditText mFName, mLName, mEMail;
    Button btnOtpVerify;
    ProgressBar mProgresBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register2, container, false);
        mEMail = (EditText) v.findViewById(R.id.register_email);
        mFName = (EditText) v.findViewById(R.id.register_firstname);
        mLName = (EditText) v.findViewById(R.id.register_lastname);
        btnOtpVerify = (Button) v.findViewById(R.id.btn_input_upload);
        initListners();
        mProgresBar = v.findViewById(R.id.register_progressbar);
        btnOtpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAndUploadData();
            }
        });

        return v;
    }

    void verifyAndUploadData() {
        String mEmailText = mEMail.getText().toString();
        if(mEmailText.trim().equals("") || mEmailText==null){
            mEmailText="no_email_entered@gmail.com";
        }


        final String mFNameText = mFName.getText().toString();
        //final String mLNameText = mLName.getText().toString();

        //Verify email and data

        if (mFNameText.isEmpty() || (mFNameText.trim().length() == 0)) {
            mFName.setError(getString(R.string.error_required_field));
            mFName.requestFocus();
            return;

        } else if (mEmailText.isEmpty() || (mEmailText.trim().length() == 0)) {
            mEMail.setError(getString(R.string.error_required_field));
            mEMail.requestFocus();
            return;

        }
// else if (mLNameText.isEmpty() || (mLNameText.trim().length() == 0)) {
//            mLName.setError(getString(R.string.error_required_field));
//            mLName.requestFocus();
//            // Toast.makeText(getActivity(), "Please Fill All Fields ", Toast.LENGTH_SHORT).show();
//            return;
//        }
        else if (!(Pattern.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", mEmailText))) {
            mEMail.setError(getString(R.string.error_email_invalid));
            mEMail.requestFocus();
        }
//        else if (!(Pattern.matches("[a-zA-Z]+", mLNameText))) {
//            mLName.setError(getString(R.string.error_name_invalid));
//            mLName.requestFocus();
//        }
        else if (!(Pattern.matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", mFNameText))) {
            mFName.setError(getString(R.string.error_name_invalid));
            mFName.requestFocus();
        } else {
            if (StaticMethods.checkInternetConnectivity(getActivity())) {


                // mLName.setFocusable(false);
                mFName.setFocusable(false);
                mEMail.setFocusable(false);
                mFName.setEnabled(false);
                // mLName.setEnabled(false);
                mEMail.setEnabled(false);
                btnOtpVerify.setEnabled(false);
                btnOtpVerify.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SCREEN);
                StaticMethods.hideSoftKeyboard(getActivity());
                mProgresBar.setVisibility(View.VISIBLE);


                ((MainApplication) getActivity().getApplicationContext()).mApi
                        .register(SharedPrefrenceUser.getInstance(getActivity()).getToken(), mFNameText, mEmailText)
                        .enqueue(new mCallBack<AuthResponse>(getActivity(), mProgresBar) {
                            @Override
                            public void onSuccessfullResponse(View progressBar, AuthResponse mResponse, Context c) {
                                SharedPrefrenceUser.setStateRegisterOrOTPToHome(mFNameText, mResponse.data);
                                progressBar.setVisibility(View.INVISIBLE);
                                c.startActivity(new Intent(c, MainActivity.class));
                            }

                            @Override
                            public void onFailureResponse(View progressBar, Context c) {

                                ((AuthActivity) c).findViewById(R.id.register_email).setEnabled(true);
                                ((AuthActivity) c).findViewById(R.id.register_firstname).setEnabled(true);
                                // ((AuthActivity) c).findViewById(R.id.register_lastname).setEnabled(true);
                                ((AuthActivity) c).findViewById(R.id.btn_input_upload).setEnabled(true);
                                ((AuthActivity) c).findViewById(R.id.btn_input_upload).setBackground(c.getResources().getDrawable(R.drawable.btn_style));
                                StaticMethods.showSoftKeyboard((Activity) c, ((AuthActivity) c).findViewById(R.id.register_firstname));


                            }

                        });

            }


        }
    }


    private void initListners() {
        mEMail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    verifyAndUploadData();
                    return true;
                }
                return false;
            }
        });
    }


}


