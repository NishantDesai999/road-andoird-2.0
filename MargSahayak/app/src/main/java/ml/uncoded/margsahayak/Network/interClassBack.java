package ml.uncoded.margsahayak.Network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import ml.uncoded.margsahayak.models.BisagResponse;
import ml.uncoded.margsahayak.models.OfflineComplainModel;
import retrofit2.Callback;

public interface interClassBack<T> extends Callback<T> {
    void onSuccessfullResponse(View progressBar, T response, Context c);
    void onFailureResponse(View progressBar, Context c);

}
