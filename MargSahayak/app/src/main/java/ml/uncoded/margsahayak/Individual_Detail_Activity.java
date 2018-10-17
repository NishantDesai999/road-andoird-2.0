package ml.uncoded.margsahayak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmList;
import ml.uncoded.margsahayak.models.ComplainModel;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

public class Individual_Detail_Activity extends AppCompatActivity {

    TextView mOfficerName, mEstimatedDate, mComment, mDescription, mTalukaName, mDistrictName, mStateName;
    TextView mRoadType, mGriType, mComplainId, mStatus, mSubmittedDate, mRoadName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual__detail_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });
        init();

    }

    void init() {
        Intent intent = getIntent();
        mGriType = (TextView) findViewById(R.id.griTypeScroll);
        mComplainId = (TextView) findViewById(R.id.complain_id);
        mStatus = (TextView) findViewById(R.id.statusScroll);
        mSubmittedDate = (TextView) findViewById(R.id.submittedDate);
        mRoadName = (TextView) findViewById(R.id.roadName);
        mOfficerName = (TextView) findViewById(R.id.officerName);
        mEstimatedDate = (TextView) findViewById(R.id.estimatedDate);
        mComment = (TextView) findViewById(R.id.comment);
        mDescription = (TextView) findViewById(R.id.descriptions);

        Realm r=Realm.getDefaultInstance();
        ComplainModel complainModel=r.where(ComplainModel.class).equalTo("id",intent.getStringExtra("MComplainIdkey")).findFirst();
        mGriType.setText(complainModel.getGrivType());
        mComplainId.setText(complainModel.getId());
        mStatus.setText(complainModel.getComplaintStatus());
        if(complainModel.getComplaintStatus().toUpperCase().equals("APPROVED"))
        {
           mStatus.setTextColor(BLUE);
        }
        if(complainModel.getComplaintStatus().toUpperCase().equals("REJECTED")){
            mStatus.setTextColor(RED);
        }
        if(complainModel.getComplaintStatus().toUpperCase().equals("IN PROGRESS")){
            mStatus.setTextColor(YELLOW);
        }
        if(complainModel.getComplaintStatus().toUpperCase().equals("COMPLETED")){
            mStatus.setTextColor(GREEN);
        }
        mOfficerName.setText(complainModel.getOfficerName());
        mSubmittedDate.setText(complainModel.getTime());
        mRoadName.setText(complainModel.getRoadName());
        mEstimatedDate.setText(complainModel.getEstimatedTime());
        StringBuilder mCommentsString = new StringBuilder();
        RealmList<String> commentData = complainModel.getComment();
        for(int i=0; i < commentData.size(); i++){
            mCommentsString.append(commentData.get(i));
        }
        mComment.setText(mCommentsString);
        mDescription.setText(complainModel.getDescription());
        Glide.with(Individual_Detail_Activity.this).load(complainModel.getUrl()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                ((android.support.design.widget.CollapsingToolbarLayout)findViewById(R.id.toolbar_layout)).setBackground(resource);

            }
        });
        //Toast.makeText(this, "GrievanceType"+intent.getStringExtra("MGrivTypekey"), Toast.LENGTH_SHORT).show();

    }



}
