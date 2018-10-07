package ml.uncoded.margsahayak;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ml.uncoded.margsahayak.models.ComplainModel;

public class Individual_Detail_Activity extends AppCompatActivity {

    ComplainModel singleItem;
    TextView mOfficerName, mEstimatedDate, mComment, mDescription, mTalukaName, mDistrictName, mStateName;
    TextView mRoadType, mGriType, mComplainId, mStatus, mSubmittedDate, mRoadName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual__detail_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init();

    }

    void init() {
        singleItem = (ComplainModel) getIntent().getSerializableExtra("MDATA");
        mOfficerName = (TextView) findViewById(R.id.officerName);
        mEstimatedDate = (TextView) findViewById(R.id.officerName);
        mComment = (TextView) findViewById(R.id.officerName);
        mOfficerName = (TextView) findViewById(R.id.officerName);
        mOfficerName = (TextView) findViewById(R.id.officerName);
        mOfficerName = (TextView) findViewById(R.id.officerName);
        mOfficerName = (TextView) findViewById(R.id.officerName);
        mOfficerName = (TextView) findViewById(R.id.officerName);

    }
}
