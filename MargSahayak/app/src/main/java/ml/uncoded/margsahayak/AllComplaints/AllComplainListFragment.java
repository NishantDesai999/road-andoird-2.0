package ml.uncoded.margsahayak.AllComplaints;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import ml.uncoded.margsahayak.FilterActivity;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.ComplainModel;

public class AllComplainListFragment extends Fragment {

    private RealmChangeListener mRealmChangeListner;
    private RecyclerView mRecycleView;
    private final String TAG = "AllFragment";
    private Realm r;
    private linearListDataAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_complain_list, container, false);
        mRecycleView = (RecyclerView) v.findViewById(R.id.fragment_all_complain_recyclerview);
        Realm.init(getContext());
        r= Realm.getDefaultInstance();
        List<ComplainModel> complainList = r.where(ComplainModel.class).findAll();
        if(complainList.size() > 0){
            v.findViewById(R.id.complaints_tetview).setVisibility(View.VISIBLE);
            v.findViewById(R.id.btn_filter).setVisibility(View.VISIBLE);
        }else{
            v.findViewById(R.id.btn_filter).setVisibility(View.GONE);
            v.findViewById(R.id.complaints_tetview).setVisibility(View.GONE);
        }
        v.findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),FilterActivity.class);
                startActivity(i);
            }
        });


        adapter = new linearListDataAdapter(getActivity(), new ArrayList<ComplainModel>(complainList));
        mRecycleView.setLayoutManager(new CustomLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecycleView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(mRecycleView,false);
        //mRecycleView.setNestedScrollingEnabled(false);
        mRealmChangeListner = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                List<ComplainModel> complainList = r.where(ComplainModel.class).findAll();
                if(complainList.size() > 0){
                    getActivity().findViewById(R.id.complaints_tetview).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.btn_filter).setVisibility(View.VISIBLE);
                }else{
                    getActivity().findViewById(R.id.complaints_tetview).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.btn_filter).setVisibility(View.GONE);
                }
                adapter = new linearListDataAdapter(getActivity(), new ArrayList<ComplainModel>(complainList));
                mRecycleView.setAdapter(adapter);
                //Toast.makeText(getActivity(),"Realm refreseh ",Toast.LENGTH_LONG).show();
            }
        };

        r.addChangeListener(mRealmChangeListner);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class CustomLinearLayoutManager extends LinearLayoutManager{

        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context,orientation,reverseLayout);
        }
        @Override
        public boolean canScrollVertically() {

            return true;
        }

    }
}
