package ml.uncoded.margsahayak;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import ml.uncoded.margsahayak.models.ComplainModel;

public class FilterActivity extends AppCompatActivity {

    int mDayFrom, mMonthFrom, mYearFrom, mDayTo, mMonthTo, mYearTo;
    Calendar mCalendar = Calendar.getInstance();
    Dialog mFilterDialog;
    RecyclerView mRecycleView;
    Realm r;
    private FilterDataAdapter adapter;
    Spinner mSpinnerStatus, mSpinnerGrievance;
    TextView mStartingDatePick, mEndingDatePick;

    String mStartDateString;
    String mEndDateString;
    int mSelectedGrievanceIndex;
    int mSelectedStatusIndex;
    Boolean isFirstTime = true;
    Boolean invalid_dates = true;
    ImageView mNoDataFoundImage;
    TextView errorInvalidDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filter);

        setToolBar();

        mNoDataFoundImage = (ImageView) findViewById(R.id.no_data_found_imageView);
        mRecycleView = (RecyclerView) findViewById(R.id.filter_recyclerview);

        //initialize both spinner to zero
        mSelectedGrievanceIndex = mSelectedStatusIndex = 0;

        //initialize both dates start date first day of year and end date to current date
        mDayTo = mCalendar.get(Calendar.DAY_OF_MONTH);
        mMonthTo = mCalendar.get(Calendar.MONTH) + 1;
        mYearTo = mYearFrom = mCalendar.get(Calendar.YEAR);
        mMonthFrom = mDayFrom = 1;
        mStartDateString = mDayFrom + "-" + mMonthFrom + "-" + mYearFrom;
        mEndDateString = mDayTo + "-" + mMonthTo + "-" + mYearTo;



        LinearLayoutManager layoutManager = new LinearLayoutManager(FilterActivity.this, LinearLayoutManager.VERTICAL, false);
        Realm.init(FilterActivity.this);
        r = Realm.getDefaultInstance();
        List<ComplainModel> complainList = r.where(ComplainModel.class).findAll();
        adapter = new FilterDataAdapter(FilterActivity.this, new ArrayList<ComplainModel>(complainList));
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(adapter);
        mRecycleView.setNestedScrollingEnabled(false);
        if (complainList.size() > 0) {
            mNoDataFoundImage.setVisibility(View.INVISIBLE);
        } else {
            mNoDataFoundImage.setVisibility(View.VISIBLE);
        }


//        mRealmChangeListner = new RealmChangeListener() {
//            @Override
//            public void onChange(Object o) {
//                List<ComplainModel> complainList = r.where(ComplainModel.class).findAll();
//                if(complainList.size() > 0){
//                    getActivity().findViewById(R.id.complaints_tetview).setVisibility(View.VISIBLE);
//                }else{
//                    getActivity().findViewById(R.id.complaints_tetview).setVisibility(View.GONE);
//                }
//                adapter = new linearListDataAdapter(getActivity(), new ArrayList<ComplainModel>(complainList));
//                mRecycleView.setAdapter(adapter);
//                Toast.makeText(getActivity(),"Realm refreseh ",Toast.LENGTH_LONG).show();
//            }
//        };

//        r.addChangeListener(mRealmChangeListner);

        showFilterDialog();

    }


    private void setToolBar() {
        Toolbar mFilterToolbar = (Toolbar) findViewById(R.id.toolbar_filter_activity);
        setSupportActionBar(mFilterToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mFilterToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow_white));
        mFilterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FilterActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_btn:
                showFilterDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private void showFilterDialog() {


        mFilterDialog = new Dialog(FilterActivity.this);
        mFilterDialog.setContentView(R.layout.filter_dialog_layout);
        mFilterDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        mFilterDialog.show();
        errorInvalidDate = (TextView) mFilterDialog.findViewById(R.id.error_invalid_dates);
        errorInvalidDate.setVisibility(View.GONE);

        mFilterDialog.findViewById(R.id.save_offline_complaint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
                if (!invalid_dates) {
                    mFilterDialog.dismiss();
                } else {
                    errorInvalidDate.setVisibility(View.VISIBLE);
                }
            }
        });

        mFilterDialog.findViewById(R.id.cancel_complaint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilterDialog.dismiss();
            }
        });
        allDatePicker();
        allSpinner();

    }

    public void allDatePicker() {


        mStartingDatePick = (TextView) mFilterDialog.findViewById(R.id.starting_date_pick);
        mEndingDatePick = (TextView) mFilterDialog.findViewById(R.id.ending_date_pick);

        if (isFirstTime) {
            isFirstTime = false;
        } else {
            mStartingDatePick.setText(mStartDateString);
            mEndingDatePick.setText(mEndDateString);
        }


        mStartingDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);
                        mDayFrom = dayOfMonth;
                        mMonthFrom = monthOfYear + 1;
                        mYearFrom = year;
                        mStartDateString = mDayFrom + "-" + mMonthFrom + "-" + mYearFrom;
                        mStartingDatePick.setText(mStartDateString);

                    }
                }, mYearFrom, mMonthFrom - 1, mDayFrom);
                datePickerDialog.show();
            }
        });
        mEndingDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);
                        mDayTo = dayOfMonth;
                        mMonthTo = monthOfYear + 1;
                        mYearTo = year;
                        mEndDateString = mDayTo + "-" + mMonthTo + "-" + mYearTo;
                        mEndingDatePick.setText(mEndDateString);

                    }
                }, mYearTo, mMonthTo - 1, mDayTo);
                datePickerDialog.show();
            }
        });


    }

    public void allSpinner() {

        mSpinnerGrievance = (Spinner) mFilterDialog.findViewById(R.id.spinner_grievance);
        ArrayAdapter<CharSequence> mAdapterGrievance = ArrayAdapter.createFromResource(this,
                R.array.grievance_array, android.R.layout.simple_spinner_item);
        mAdapterGrievance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerGrievance.setAdapter(mAdapterGrievance);
        mSpinnerGrievance.setSelection(mSelectedGrievanceIndex);


        mSpinnerGrievance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSpinnerGrievance.setSelection(i);
                mSelectedGrievanceIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mSpinnerGrievance.setSelection(mSelectedGrievanceIndex);
            }
        });

        mSpinnerStatus = (Spinner) mFilterDialog.findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> mAdapterStatus = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        mAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerStatus.setAdapter(mAdapterStatus);
        mSpinnerStatus.setSelection(mSelectedStatusIndex);

        mSpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSpinnerStatus.setSelection(i);
                mSelectedStatusIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mSpinnerStatus.setSelection(mSelectedStatusIndex);
            }
        });


    }

    public void applyFilter() {

        String mSelectedGrievance = mSpinnerGrievance.getSelectedItem().toString();
        String mSelectedStatus = mSpinnerStatus.getSelectedItem().toString();

        Realm.init(FilterActivity.this);
        r = Realm.getDefaultInstance();
        List<ComplainModel> complainList;
        invalid_dates = false;
        Date mStartDateQuery = mCalendar.getTime();
        Date mEndDateQuery = mCalendar.getTime();
        // Condition for date checking
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        SimpleDateFormat sdfForQuery = new SimpleDateFormat("dd:MM:yyyy");
        try {
            mStartDateQuery = sdf.parse(mStartDateString);
            mEndDateQuery = sdf.parse(mEndDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (mEndDateQuery.compareTo(mStartDateQuery) < 0) {
            invalid_dates = true;
        } else {
            invalid_dates = false;
        }


        if (!invalid_dates) {
            if (mSelectedGrievance.equals("ALL") || mSelectedStatus.equals("ALL")) {
                if (mSelectedGrievance.equals("ALL") && mSelectedStatus.equals("ALL")) {
                    // no selection of both field girevance and status
                    complainList = r.where(ComplainModel.class).findAll();
                } else if (mSelectedGrievance.equals("ALL")) {
                    // no selection of field grievance
                    complainList = r.where(ComplainModel.class).equalTo("complaintStatus", mSelectedStatus, Case.INSENSITIVE).findAll();
                } else {
                    // no selection of field status
                    complainList = r.where(ComplainModel.class).equalTo("grivType", mSelectedGrievance, Case.INSENSITIVE).findAll();
                }
            } else {
                // Query for all field
                complainList = r.where(ComplainModel.class).equalTo("complaintStatus", mSelectedStatus, Case.INSENSITIVE)
                        .equalTo("grivType", mSelectedGrievance, Case.INSENSITIVE)
                        .findAll();
            }

            //use java date formate RealmQuery.greaterThan(String fieldName, Date value)
            // data can be sorted


            ArrayList<ComplainModel> mQueiredList = new ArrayList<ComplainModel>();

            for (ComplainModel c : complainList) {
                try {
                    if ((sdfForQuery.parse(c.getTime())).after(mStartDateQuery) && (sdfForQuery.parse(c.getTime())).before(mEndDateQuery) || ((sdfForQuery.parse(c.getTime())).compareTo(mStartDateQuery) == 0) || ((sdfForQuery.parse(c.getTime())).compareTo(mEndDateQuery) == 0)) {
                        mQueiredList.add(c);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }


            adapter = new FilterDataAdapter(FilterActivity.this, mQueiredList);
            mRecycleView.setAdapter(adapter);

            if (mQueiredList.size() > 0) {
                mNoDataFoundImage.setVisibility(View.INVISIBLE);
            } else {
                mNoDataFoundImage.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
