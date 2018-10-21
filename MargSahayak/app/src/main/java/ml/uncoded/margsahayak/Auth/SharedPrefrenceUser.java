package ml.uncoded.margsahayak.Auth;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;

import io.realm.Realm;

public class SharedPrefrenceUser {

    private static final String USER_Login_SHARED_PREFRENCE = "user";

    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_TOKEN = "keytoken";
    private static final String KEY_STATE = "keystate";

    private static SharedPrefrenceUser sharedPrefrence_User_instance;
    private static Context context_to_use;

    private SharedPrefrenceUser(Context context) {
        context_to_use = context;
    }

    public static synchronized SharedPrefrenceUser getInstance(Context context) {
        if (sharedPrefrence_User_instance == null) {
            sharedPrefrence_User_instance = new SharedPrefrenceUser(context);
        }
        return sharedPrefrence_User_instance;
    }

    public static void setStateLoginToOtp(String phone_number) {
        SharedPreferences mSharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_PHONE, phone_number);
        editor.putString(KEY_STATE, Constant.otp_state);
        editor.apply();
    }
    public static void setStateLogin() {
        SharedPreferences mSharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_STATE, Constant.login_state);
        editor.apply();
    }


    public static void setStateOtpToRegister(String token) {
        SharedPreferences mSharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_STATE, Constant.register_state);
        editor.putString(KEY_TOKEN, token);
        editor.apply();

    }

    public static void setStateRegisterOrOTPToHome(String username, String token) {
        SharedPreferences mSharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_STATE, Constant.home_state);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public String getToken() {
        SharedPreferences mSharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        return "token " + mSharedPreferences.getString(KEY_TOKEN, "");
    }

    public String getState() {
        SharedPreferences mSharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_STATE, Constant.login_state);
    }


    public String getPhone() {
        SharedPreferences mSharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_PHONE, " ");
    }


    //    public boolean isLoggedIn() {
//        SharedPreferences sharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(KEY_USERNAME, null) != null;
//    }
//
//    public String getToken() {
//        SharedPreferences sharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(KEY_TOKEN, null);
//    }
//
//
//    public void setToken(String token) {
//        SharedPreferences sharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(KEY_TOKEN, token);
//        editor.apply();
//    }
//
    public void logout() {
        Realm r=Realm.getDefaultInstance();
        r.beginTransaction();
        r.deleteAll();
        r.commitTransaction();
        SharedPreferences sharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(context_to_use, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context_to_use.startActivity(intent);

    }
}