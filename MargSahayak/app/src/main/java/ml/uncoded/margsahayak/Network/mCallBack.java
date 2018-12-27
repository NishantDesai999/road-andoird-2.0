package ml.uncoded.margsahayak.Network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import ml.uncoded.margsahayak.models.BisagResponse;
import ml.uncoded.margsahayak.models.Dialogs;
import ml.uncoded.margsahayak.models.OfflineComplainModel;
import retrofit2.Call;
import retrofit2.Response;

public abstract class mCallBack<T> implements interClassBack<T> {


    private Context c;
    private View progressBar;
    private OfflineComplainModel mInputData;
    private BisagResponse mBisagResponse;

    protected mCallBack(Context c, View p){
        this.c = c;
        this.progressBar = p;
    }

    protected mCallBack(Context c, View p, OfflineComplainModel mInputData, BisagResponse mBisagResponse){
        this.c = c;
        this.progressBar = p;
        this.mInputData = mInputData;
        this.mBisagResponse = mBisagResponse;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        try {
            if (response.isSuccessful()) {
                T mTH = response.body();

                onSuccessfullResponse(progressBar, mTH,c);


            } else {
                Dialogs dialogs;
                progressBar.setVisibility(View.INVISIBLE);
                dialogs = new Dialogs(response.code() + " " + response.message()+" Body : "+response.body());
                dialogs.show(((Activity) c).getFragmentManager(), "ErrMSG");
            }

        }catch (IllegalStateException e){
            Log.e("mCallBack"," Illegal Exception On response");
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        try {
            onFailureResponse(progressBar,c);
            t.printStackTrace();
            progressBar.setVisibility(View.INVISIBLE);
            String strErrMsg;
            Dialogs dialogs;
            if (t.getCause() != null && t.getCause().toString().contains("java.net.ConnectException")) {
                strErrMsg = "FAILED TO CONNECT TO SERVER\nNETWORK IS UNREACHABLE";
                dialogs = new Dialogs(strErrMsg);
            } else if (t.getCause() == null) {
                strErrMsg = "CONNECTION TIMED Out";
                dialogs = new Dialogs(strErrMsg);
            } else {
                strErrMsg = "Network Connection Seems Slow\nPlease Try After Some Time";
                dialogs = new Dialogs(strErrMsg);
            }
            dialogs.show(((Activity) c).getFragmentManager(), "ErrMSG");
        }catch (IllegalStateException e){
            Log.e("mCallBack"," Illegal Exception On failure");
            e.printStackTrace();
        }
    }
}
