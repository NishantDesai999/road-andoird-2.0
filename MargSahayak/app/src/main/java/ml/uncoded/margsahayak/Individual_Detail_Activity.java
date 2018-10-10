package ml.uncoded.margsahayak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.concurrent.ExecutionException;

import ml.uncoded.margsahayak.models.ComplainModel;

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
        Intent i = getIntent();
        mGriType = (TextView) findViewById(R.id.griTypeScroll);
        mComplainId = (TextView) findViewById(R.id.complain_id);
        mStatus = (TextView) findViewById(R.id.statusScroll);
        mSubmittedDate = (TextView) findViewById(R.id.submittedDate);
        mRoadName = (TextView) findViewById(R.id.roadName);
        mOfficerName = (TextView) findViewById(R.id.officerName);
        mEstimatedDate = (TextView) findViewById(R.id.estimatedDate);
        mComment = (TextView) findViewById(R.id.comment);
        mDescription = (TextView) findViewById(R.id.descriptions);

        Toast.makeText(this, "GrievanceType"+i.getStringExtra("MGrivTypekey"), Toast.LENGTH_SHORT).show();
        mGriType.setText(i.getStringExtra("MGrivTypekey"));
        mComplainId.setText(i.getStringExtra("MComplainIdkey"));
        mStatus.setText(i.getStringExtra("MComplaintStatuskey"));
        mOfficerName.setText(i.getStringExtra("MOfficerNamekey"));
        mSubmittedDate.setText(i.getStringExtra("MSubmittedDatekey"));
        mRoadName.setText(i.getStringExtra("MRoadNamekey"));
        mOfficerName.setText(i.getStringExtra("MOfficerNamekey"));
        mEstimatedDate.setText(i.getStringExtra("MEstimatedTimekey"));
        mComment.setText(i.getStringExtra("MCommentkey"));
        mDescription.setText(i.getStringExtra("MDescriptionkey"));




//        try {
//            Bitmap mImgBitmap=Glide.with(Individual_Detail_Activity.this)
//                    .load(i.getStringExtra("MURLkey")).asBitmap().into(400,400).get();
//            BitmapDrawable background=new BitmapDrawable(mImgBitmap);
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Glide.with(Individual_Detail_Activity.this).load(i.getStringExtra("MURLkey")).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                ((android.support.design.widget.CollapsingToolbarLayout)findViewById(R.id.toolbar_layout)).setBackground(resource);
            }
        });

    }



}
