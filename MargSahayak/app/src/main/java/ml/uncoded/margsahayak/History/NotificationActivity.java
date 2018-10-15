package ml.uncoded.margsahayak.History;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import ml.uncoded.margsahayak.AllComplaints.linearListDataAdapter;
import ml.uncoded.margsahayak.Auth.SharedPrefrenceUser;
import ml.uncoded.margsahayak.FilterActivity;
import ml.uncoded.margsahayak.FilterDataAdapter;
import ml.uncoded.margsahayak.Main2Activity;
import ml.uncoded.margsahayak.MainActivity;
import ml.uncoded.margsahayak.MainApplication;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.ComplainModel;
import ml.uncoded.margsahayak.models.Dialogs;
import ml.uncoded.margsahayak.models.NotificationComplaintModel;
import ml.uncoded.margsahayak.models.StaticMethods;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private String strErrMsg;
    private Dialogs dialogs;
    private RecyclerView mRecycleView;
    private NotificationListDataAdapter adapter;
    private ProgressBar p;
    Realm r1;
    private RealmChangeListener mRealmChangeListner;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setToolBar();
        p = findViewById(R.id.notification_progressBar);
        layoutManager = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        r1 = Realm.getDefaultInstance();
        mRecycleView = findViewById(R.id.notification_recycleview);
        List<NotificationComplaintModel> complainList = r1.where(NotificationComplaintModel.class).findAll();
        Log.d("test", "onCreate: " + complainList.size());
        adapter = new NotificationListDataAdapter(NotificationActivity.this, new ArrayList<NotificationComplaintModel>(complainList));
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(adapter);
        mRealmChangeListner = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
//                List<NotificationComplaintModel> complainList = r1.where(NotificationComplaintModel.class).findAll();
//                adapter = new NotificationListDataAdapter(NotificationActivity.this, new ArrayList<NotificationComplaintModel>(complainList));
//                mRecycleView.setAdapter(adapter);
                Toast.makeText(NotificationActivity.this, "Realm refreseh ", Toast.LENGTH_LONG).show();
            }
        };
        r1.addChangeListener(mRealmChangeListner);


    }

    private void setToolBar() {
        Toolbar mNotificationToolbar = (Toolbar) findViewById(R.id.toolbar_notification_activity);
        setSupportActionBar(mNotificationToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mNotificationToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow_white));
        mNotificationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_btn:
                onRefreshNotification();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void onRefreshNotification(){
            boolean flag = StaticMethods.checkInternetConnectivity(NotificationActivity.this);
            if (!flag) {
            } else {
                p.setVisibility(View.VISIBLE);
                MainApplication m = (MainApplication) getApplicationContext();
                m.mApi.getComplaintNotification(SharedPrefrenceUser.getInstance(NotificationActivity.this).getToken()).enqueue(new Callback<List<NotificationComplaintModel>>() {
                    private final String TAG = "complain";

                    @Override
                    public void onResponse(Call<List<NotificationComplaintModel>> call, final Response<List<NotificationComplaintModel>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "response : " + response.headers());
                            final Realm r = Realm.getDefaultInstance();
                            r.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    List<NotificationComplaintModel> notificationList = response.body();
                                    ComplainModel complainModel;
                                    for (NotificationComplaintModel notificationComplaintModel : notificationList) {
                                        Log.d(TAG, "execute: "+notificationComplaintModel.getId());
                                            complainModel = realm.where(ComplainModel.class).equalTo("id", notificationComplaintModel.getId()).findFirst();
                                        if(notificationComplaintModel.getComplaintStatus()!=null)
                                            complainModel.setComplaintStatus(notificationComplaintModel.getComplaintStatus());
                                        if(notificationComplaintModel.getComments()!=null)
                                            complainModel.setComment(notificationComplaintModel.getComments());
                                        if(notificationComplaintModel.getEstimatedDate()!=null)
                                            complainModel.setEstimatedTime(notificationComplaintModel.getEstimatedDate());
                                        if(notificationComplaintModel.getOfficerEmail()!=null)
                                            complainModel.setOfficerEmail(notificationComplaintModel.getOfficerEmail());
                                        if(notificationComplaintModel.getOfficerId()!=null)
                                            complainModel.setOfficerId(notificationComplaintModel.getOfficerId());
                                        if(notificationComplaintModel.getOfficerName()!=null)
                                            complainModel.setOfficerName(notificationComplaintModel.getOfficerName());
                                            notificationComplaintModel.setImgUrl(complainModel.getUrl());
                                        Log.d("test", "execute: " + notificationComplaintModel.getEstimatedDate());
                                        realm.copyToRealm(notificationComplaintModel);
                                      realm.insertOrUpdate(complainModel);
                                    }


                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {

                                    List<NotificationComplaintModel> complainList = r1.where(NotificationComplaintModel.class).findAll();
                                    adapter = new NotificationListDataAdapter(NotificationActivity.this, new ArrayList<NotificationComplaintModel>(complainList));
                                    mRecycleView.setLayoutManager(layoutManager);
                                    mRecycleView.setAdapter(adapter);
                                    //Log.d(TAG, "data : " + complainModel.getId());
                                    p.setVisibility(View.INVISIBLE);

                                }
                            });

//                                Log.d(TAG, "Db : " + r.where(ComplainModel.class).findAll().size());

                        }
                        //Checking server Internal Errors
                        else {
                            Log.d(TAG, "Code: " + response.code() + " Message: " + response.message());
                            p.setVisibility(View.INVISIBLE);
                            Toast.makeText(NotificationActivity.this, "" + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                            strErrMsg = response.code() + " " + response.message();
                            dialogs = new Dialogs(strErrMsg);
                            dialogs.show(getFragmentManager(), "ErrMSG");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NotificationComplaintModel>> call, Throwable t) {


                        t.printStackTrace();
                        Log.d(TAG, "OnFailure");
                        p.setVisibility(View.INVISIBLE);
                        Toast.makeText(NotificationActivity.this, "" + t.getCause() + " :: " + " ---> " + t.getMessage(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}

