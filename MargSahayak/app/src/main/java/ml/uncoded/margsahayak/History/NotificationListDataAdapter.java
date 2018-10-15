package ml.uncoded.margsahayak.History;

import android.content.Context;
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

import io.realm.RealmList;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.NotificationComplaintModel;


public class NotificationListDataAdapter extends RecyclerView.Adapter<NotificationListDataAdapter.SingleItemRowHolder> {

    private ArrayList<NotificationComplaintModel> itemsList;
    private Context mContext;

    public NotificationListDataAdapter(Context context, ArrayList<NotificationComplaintModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;

        for(NotificationComplaintModel x: itemsList){
            Log.d("test", "NotificationListDataAdapter: " + x.getId());
        }
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_status_change__card, viewGroup,false);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {


        NotificationComplaintModel singleItem = itemsList.get(i);
       // holder.area.setText(singleItem.getTaluka().toUpperCase());
        holder.status.setText(singleItem.getComplaintStatus().toUpperCase());
//        holder.grievance.setText(singleItem.getGrivType().toUpperCase());
//        holder.date.setText(singleItem.getTime().toUpperCase().substring(0,10) );
            holder.completionDate1.setText(singleItem.getEstimatedDate());
        StringBuilder mCommentsString = new StringBuilder();
        RealmList<String> commentData = singleItem.getComments();
        for(int j=0;j < commentData.size(); j++){
            mCommentsString.append(commentData.get(j));
        }
            holder.comments.setText(mCommentsString);

        Log.d("test",itemsList.toString());
        Glide.with(mContext)
                .load(singleItem.getImgUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_image_black_24dp).into(holder.itemImage);

        holder.itemImage.setColorFilter(Color.argb(150, 0, 0, 0));
        holder.itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView area;

        protected ImageView itemImage;

        protected TextView status;
        protected TextView date;
        protected TextView grievance;
        protected TextView completionDate,completionDate1,comments;


        protected Button delete;
        protected Button share;

        protected CardView c;



        public SingleItemRowHolder(View view) {
            super(view);

            this.c = (CardView) view.findViewById(R.id.card);
            this.status = (TextView) view.findViewById(R.id.tv_status_data);
            this.date = (TextView) view.findViewById(R.id.date);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.grievance = (TextView) view.findViewById(R.id.textView9);
            this.completionDate = (TextView) view.findViewById(R.id.tv_estimated_time);
            this.completionDate1 = (TextView) view.findViewById(R.id.tv_estimated_time_data);
            this.comments=view.findViewById(R.id.tv_comment_data);


            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Toast.makeText(v.getContext(),v.getId()+"",Toast.LENGTH_LONG);

                }
            });


        }

    }
}