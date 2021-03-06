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
import android.widget.ImageView;
import android.widget.QuickContactBadge;
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

    TextView mOfficerName, mEstimatedDate, mComment, mDescription,mComplaintUploadTime;
    TextView  mGriType, mComplainId, mStatus, mSubmittedDate, mRoadName;
    ImageView mMailToOfficer;

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
        mMailToOfficer=findViewById(R.id.iv_email_Officer);
        mComplaintUploadTime=findViewById(R.id.tv_complaint_upload_time);

        Realm r=Realm.getDefaultInstance();
        final ComplainModel complainModel=r.where(ComplainModel.class).equalTo("id",intent.getStringExtra("MComplainIdkey")).findFirst();
        mGriType.setText(complainModel.getGrivType());
        mComplainId.setText(complainModel.getId());
        mStatus.setText(complainModel.getComplaintStatus());
        if(complainModel.getComplaintStatus().toUpperCase().equals("APPROVED"))
        {
           mStatus.setTextColor(0xFF0277BD);
        }
        if(complainModel.getComplaintStatus().toUpperCase().equals("REJECTED")){
            mStatus.setTextColor(0xFFC62828);
        }
        if(complainModel.getComplaintStatus().toUpperCase().equals("IN PROGRESS")){
            mStatus.setTextColor(0xFFFBC02D);
        }
        if(complainModel.getComplaintStatus().toUpperCase().equals("COMPLETED")){
            mStatus.setTextColor(0xFF2E7D32);
        }
        mMailToOfficer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { complainModel.getOfficerEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
        mOfficerName.setText(complainModel.getOfficerName());
        mSubmittedDate.setText(complainModel.getTime());
        mRoadName.setText(complainModel.getRoadName());
        if((complainModel.getEstimatedTime()!= null) && (complainModel.getEstimatedTime().length()>0)) {
            mEstimatedDate.setText(complainModel.getEstimatedTime());
        }

        StringBuilder mCommentsString = new StringBuilder();
        RealmList<String> commentData = complainModel.getComment();
        for(int i=0; i < commentData.size(); i++){
            mCommentsString.append(commentData.get(i));
        }
        if(mCommentsString.length()>0 && mCommentsString!=null)
        mComment.setText(mCommentsString);
        else{
            mComment.setText("No Comments");
        }
        mDescription.setText(complainModel.getDescription());
        Glide.with(Individual_Detail_Activity.this).load(complainModel.getUrl()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                ((android.support.design.widget.CollapsingToolbarLayout)findViewById(R.id.toolbar_layout)).setBackground(resource);

            }
        });
        mComplaintUploadTime.setText(complainModel.getTime());
        //Toast.makeText(this, "GrievanceType"+intent.getStringExtra("MGrivTypekey"), Toast.LENGTH_SHORT).show();

    }



}
