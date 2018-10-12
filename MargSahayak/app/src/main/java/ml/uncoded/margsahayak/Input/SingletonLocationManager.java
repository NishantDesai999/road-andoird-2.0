package ml.uncoded.margsahayak.Input;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

public class SingletonLocationManager {
    
    static final String TAG = "MyLocationMadness";
    private static LocationManager single_instance = null;

    public static LocationManager getInstance(Application a,String msg)
    {

        Log.d(TAG, "getInstance: called from " + msg );
        if (single_instance == null)
            single_instance =
                    ((LocationManager) a.getSystemService(Context.LOCATION_SERVICE));

        return single_instance;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d(TAG, "finalize: called");
    }


}
