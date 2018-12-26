package ml.uncoded.margsahayak.Offline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.OfflineComplainModel;

public class OfflineComplainListFragment extends Fragment {

    private RealmChangeListener mRealmChangeListner;
    private RecyclerView mRecycleView;
    private final String TAG = "AllFragment";
    private Realm r;
    private OfflineComplainListDataAdapter adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View v = inflater.inflate(R.layout.fragment_offline_complain_list, container, false);
        mRecycleView = (RecyclerView) v.findViewById(R.id.fragment_offline_complain_recyclerview);
        Realm.init(getContext());
        r= Realm.getDefaultInstance();
        List<OfflineComplainModel> complainList = r.where(OfflineComplainModel.class).findAll();
        if(complainList.size() > 0){
            v.findViewById(R.id.offline_textview).setVisibility(View.VISIBLE);
        }else{
            v.findViewById(R.id.offline_textview).setVisibility(View.GONE);
        }
        adapter = new OfflineComplainListDataAdapter(getActivity(), new ArrayList<OfflineComplainModel>(complainList));
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecycleView.setAdapter(adapter);
//        mRecycleView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.findViewById(R.id.)
//            }
//        });
        mRealmChangeListner = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                List<OfflineComplainModel> complainList = r.where(OfflineComplainModel.class).findAll();
                if(complainList.size() > 0){
                    v.findViewById(R.id.offline_textview).setVisibility(View.VISIBLE);
                }else{
                    v.findViewById(R.id.offline_textview).setVisibility(View.GONE);
                }
                adapter = new OfflineComplainListDataAdapter(getActivity(), new ArrayList<OfflineComplainModel>(complainList));
                mRecycleView.setAdapter(adapter);
            }
        };

        r.addChangeListener(mRealmChangeListner);
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}
