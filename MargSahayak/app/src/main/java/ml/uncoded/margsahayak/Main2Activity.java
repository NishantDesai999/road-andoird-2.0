package ml.uncoded.margsahayak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import ml.uncoded.margsahayak.Input.InputActivity;
import ml.uncoded.margsahayak.models.BisagResponse;
import ml.uncoded.margsahayak.models.ComplainModel;
import ml.uncoded.margsahayak.models.Dialogs;
import ml.uncoded.margsahayak.models.OfflineComplainModel;
import ml.uncoded.margsahayak.models.StaticMethods;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "main2Activty";
    Dialogs dialogs;
    String strErrMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        ((Button) findViewById(R.id.bisagTest)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainApplication)getApplicationContext()).mApi
                        .callBisagApi("23.456574","72.234324")
                        .enqueue(new Callback<List<BisagResponse>>() {
                            @Override
                            public void onResponse(Call<List<BisagResponse>> call, Response<List<BisagResponse>> response) {

                                if(response.isSuccessful()){
                                    List<BisagResponse> mBisagResponse = response.body();
                                    Toast.makeText(Main2Activity.this, ""+mBisagResponse.get(0).distance+ " "+mBisagResponse.get(0).roadName + " " + mBisagResponse.get(0).raodType, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Main2Activity.this, "Bisag Response not succesfuls", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<BisagResponse>> call, Throwable t) {
                                Toast.makeText(Main2Activity.this, "Bisag Response failure", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });


        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = StaticMethods.checkInternetConnectivity(Main2Activity.this);
                if (!flag) {
                } else {
                    final View p = findViewById(R.id.mainActivity2ProgressBar);
                    p.setVisibility(View.VISIBLE);
                    MainApplication m = (MainApplication) getApplicationContext();
                    m.mApi.getComplain().enqueue(new Callback<ComplainModel>() {
                        private final String TAG = "complain";

                        @Override
                        public void onResponse(Call<ComplainModel> call, final Response<ComplainModel> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "response : " + response.headers());
                                Realm r = Realm.getDefaultInstance();
                                r.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {

                                        ComplainModel complainModel = response.body();
                                        realm.copyToRealm(complainModel);
                                        Log.d(TAG, "data : " + complainModel.getId());
//                                    p.setVisibility(View.INVISIBLE);
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        Intent i = new Intent(Main2Activity.this, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);

                                    }
                                });

                                Log.d(TAG, "Db : " + r.where(ComplainModel.class).findAll().size());

                            }
                            //Checking server Internal Errors
                            else {
                                Log.d(TAG, "Code: " + response.code() + " Message: " + response.message());
                                p.setVisibility(View.INVISIBLE);
                                //Toast.makeText(Main2Activity.this, "" + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                                strErrMsg = response.code() + " " + response.message();
                                dialogs = new Dialogs(strErrMsg);
                                dialogs.show(getFragmentManager(), "ErrMSG");
                            }
                        }

                        @Override
                        public void onFailure(Call<ComplainModel> call, Throwable t) {


                            t.printStackTrace();
                            Log.d(TAG, "OnFailure");
                            p.setVisibility(View.INVISIBLE);
                            Toast.makeText(Main2Activity.this, "" + t.getCause() + " :: " + " ---> " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            if (t.getCause() != null && t.getCause().toString().contains("java.net.ConnectException")) {
                                strErrMsg = "FAILED TO CONNECT TO SERVER\nNETWORK IS UNREACHABLE";
                                dialogs = new Dialogs(strErrMsg);
                            } else if (t.getCause() == null) {

                                strErrMsg = "CONNECTION TIMED OUT";
                                dialogs = new Dialogs(strErrMsg);

                            } else {

                                strErrMsg = "JSON PARSING ERROR";
                                dialogs = new Dialogs(strErrMsg);

                            }
                            dialogs.show(getFragmentManager(), "ErrMSG");

                        }
                    });
                }
            }
        });


        ((Button) findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = StaticMethods.checkInternetConnectivity(Main2Activity.this);
                if (!flag) {
                } else {
                    final View p = findViewById(R.id.mainActivity2ProgressBar);
                    p.setVisibility(View.VISIBLE);
                    MainApplication m = (MainApplication) getApplicationContext();
                    m.mApi.getComplain().enqueue(new Callback<ComplainModel>() {
                        private final String TAG = "complain";

                        @Override
                        public void onResponse(Call<ComplainModel> call, final Response<ComplainModel> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "response : " + response.headers());
                                Realm r = Realm.getDefaultInstance();
                                r.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {

                                        Realm r = Realm.getDefaultInstance();
                                        ComplainModel complainModel = response.body();
                                        Number maxId = r.where(OfflineComplainModel.class).max("id");
                                        int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                                        OfflineComplainModel offlineComplainModel = r.createObject(OfflineComplainModel.class, nextId);
                                        offlineComplainModel.setTime(complainModel.getTime());
                                        offlineComplainModel.setGrievanceDescription(complainModel.getDescription());
                                        offlineComplainModel.setLocation(complainModel.getLocation());
                                        offlineComplainModel.setGrievanceName(complainModel.getGrivType());
                                        offlineComplainModel.setImgurl(complainModel.getUrl());
                                        realm.copyToRealm(complainModel);
                                        realm.copyToRealm(offlineComplainModel);
                                        Log.d(TAG, "data : " + complainModel.getId());
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        Intent i = new Intent(Main2Activity.this, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);

                                    }
                                });

                                Log.d(TAG, "Db : " + r.where(ComplainModel.class).findAll().size());

                            }
                            //Checking server Internal Errors
                            else {
                                Log.d(TAG, "Code: " + response.code() + " Message: " + response.message());
                                p.setVisibility(View.INVISIBLE);
                                //Toast.makeText(Main2Activity.this, "" + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                                strErrMsg = response.code() + " " + response.message();
                                dialogs = new Dialogs(strErrMsg);
                                dialogs.show(getFragmentManager(), "ErrMSG");
                            }
                        }

                        @Override
                        public void onFailure(Call<ComplainModel> call, Throwable t) {


                            t.printStackTrace();
                            Log.d(TAG, "OnFailure");
                            p.setVisibility(View.INVISIBLE);
                            Toast.makeText(Main2Activity.this, "" + t.getCause() + " :: " + " ---> " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            if (t.getCause() != null && t.getCause().toString().contains("java.net.ConnectException")) {
                                strErrMsg = "FAILED TO CONNECT TO SERVER\nNETWORK IS UNREACHABLE";
                                dialogs = new Dialogs(strErrMsg);
                            } else if (t.getCause() == null) {

                                strErrMsg = "CONNECTION TIMED OUT";
                                dialogs = new Dialogs(strErrMsg);

                            } else {

                                strErrMsg = "JSON PARSING ERROR";
                                dialogs = new Dialogs(strErrMsg);

                            }
                            dialogs.show(getFragmentManager(), "ErrMSG");

                        }
                    });
                }
            }
        });

        ((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, InputActivity.class);
                startActivity(i);

            }
        });


    }
}

