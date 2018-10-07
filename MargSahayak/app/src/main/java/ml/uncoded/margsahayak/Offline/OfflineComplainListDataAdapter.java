package ml.uncoded.margsahayak.Offline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;

import io.realm.Realm;
import ml.uncoded.margsahayak.MainApplication;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.OfflineComplainModel;


public class OfflineComplainListDataAdapter extends RecyclerView.Adapter<OfflineComplainListDataAdapter.SingleItemRowHolder> {

    private ArrayList<OfflineComplainModel> itemsList;
    private Context mContext;

    public OfflineComplainListDataAdapter(Context context, ArrayList<OfflineComplainModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offline_complain_individual_card, viewGroup,false);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {


        OfflineComplainModel singleItem = itemsList.get(i);
        holder.grievance.setText(singleItem.getGrievanceName().toUpperCase());
        Log.v("debug",itemsList.toString());

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeFile(singleItem.getImgurl(),options);
        holder.itemImage.setImageBitmap(bitmap);
        holder.itemImage.setColorFilter(Color.argb(100, 0, 0, 0));
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {


        protected ImageView itemImage;
        protected TextView grievance;
        protected Button btnCancel,btnRetryUpload;
        protected CardView c;
        public SingleItemRowHolder(final View view) {
            super(view);

            this.c = (CardView) view.findViewById(R.id.offline_card);
            this.itemImage = (ImageView) view.findViewById(R.id.img_grievance);
            this.grievance = (TextView) view.findViewById(R.id.tv_grievance_name);
            this.btnCancel=view.findViewById(R.id.btn_cancel);
            this.btnRetryUpload=view.findViewById(R.id.btn_retry_upload);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),v.getId()+"",Toast.LENGTH_LONG);

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OfflineComplainModel singleItem = itemsList.get(getAdapterPosition());
                    Realm r=Realm.getDefaultInstance();
                    r.beginTransaction();
                    OfflineComplainModel offlineComplainModel=r.where(OfflineComplainModel.class).equalTo("id",singleItem.getId()).findFirst();
                    offlineComplainModel.deleteFromRealm();
                    r.commitTransaction();
                }
            });

            btnRetryUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainApplication m=(MainApplication) mContext.getApplicationContext() ;


                }
            });



        }

    }
}