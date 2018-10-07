package ml.uncoded.margsahayak.Auth;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Pattern;

import ml.uncoded.margsahayak.MainApplication;
import ml.uncoded.margsahayak.models.BisagResponse;
import ml.uncoded.margsahayak.models.OfflineComplainModel;
import ml.uncoded.margsahayak.models.StaticMethods;
import ml.uncoded.margsahayak.Network.mCallBack;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.AuthResponse;

public class LoginFragment extends Fragment {

    private EditText mPhoneView;
    private ProgressBar mProgresBar;
    private Button mNextBtn;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mPhoneView = (EditText) v.findViewById(R.id.login_input);
        mNextBtn = (Button) v.findViewById(R.id.btn_login);
        mProgresBar = (ProgressBar) v.findViewById(R.id.login_progressbar);
        initListners();
        mPhoneView.requestFocus();
        return v;

    }

    private void initListners() {
        mPhoneView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        mNextBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        mPhoneView.setError(null);
        final String phoneNumber = mPhoneView.getText().toString();

        boolean cancel = false;

        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneView.setError(getString(R.string.error_required_field));
            cancel = true;
        } else if (!(Pattern.matches("[0-9]+", phoneNumber) && (phoneNumber.length() == 10))) {
            mPhoneView.setError(getString(R.string.error_number_invalid));
            cancel = true;
        }

        if (cancel) {
            //Phone Number is not OK show it in focus..
            mPhoneView.requestFocus();
        } else {



            if (StaticMethods.checkInternetConnectivity(getActivity())) {

                mPhoneView.setEnabled(false);

                mNextBtn.setEnabled(false);
                mNextBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SCREEN);
                StaticMethods.hideSoftKeyboard(getActivity());

                mProgresBar.setVisibility(View.VISIBLE);
                ((MainApplication) getActivity().getApplicationContext()).mApi
                        .login(phoneNumber)
                        .enqueue(new mCallBack<AuthResponse>(getActivity(), mProgresBar) {
                            @Override
                            public void onSuccessfullResponse(View progressBar, AuthResponse mResponse, Context c) {

                                    SharedPrefrenceUser.setStateLoginToOtp(phoneNumber);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    FragmentTransaction mFragmentTransition = getActivity().getSupportFragmentManager().beginTransaction();
                                    mFragmentTransition.replace(R.id.auth_framelayout, new OtpScreenFragment());
                                    mFragmentTransition.addToBackStack(null);
                                    mFragmentTransition.commit();

                            }

                            @Override
                            public void onFailureResponse(View progressBar, Context c) {

                                ((AuthActivity)c).findViewById(R.id.login_input).setEnabled(true);
                                ((AuthActivity)c).findViewById(R.id.btn_login).setEnabled(true);
                                ((AuthActivity)c).findViewById(R.id.btn_login).setBackground(c.getResources().getDrawable(R.drawable.btn_style));
                                StaticMethods.showSoftKeyboard((Activity) c,((AuthActivity)c).findViewById(R.id.login_input));



                            }

                        });

            }


        }

    }
}
