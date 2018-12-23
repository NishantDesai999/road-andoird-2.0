package ml.uncoded.margsahayak;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.Group;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import ml.uncoded.margsahayak.models.ComplainModel;


public class FilterDataAdapter extends RecyclerView.Adapter<FilterDataAdapter.SingleItemRowHolder> {

    private ArrayList<ComplainModel> itemsList;
    private Context mContext;

    public FilterDataAdapter(Context context, ArrayList<ComplainModel> itemsList) {
        Collections.reverse(itemsList);
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_complain_individual_card2, viewGroup,false);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {


        ComplainModel singleItem = itemsList.get(i);
        holder.status.setText(singleItem.getComplaintStatus().toUpperCase());
        holder.grievance.setText(singleItem.getGrivType().toUpperCase());
        if (singleItem.getComplaintStatus().toUpperCase().equals("PENDING")) {
          holder.gpEstimatedTime.setVisibility(View.GONE);
          holder.reportedDate.setText(singleItem.getTime().toUpperCase().substring(0,10) );
        }else {
            holder.gpReportedOn.setVisibility(View.GONE);
            holder.estimatedTime.setText(singleItem.getEstimatedTime().toUpperCase());
        }
        Glide.with(mContext)
                .load(singleItem.getUrl())
                .into(holder.itemImage);
        holder.itemImage.setColorFilter(Color.argb(150, 0, 0, 0));
        holder.itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected ImageView itemImage;
        protected TextView status;
        protected TextView reportedDate;
        protected TextView grievance;
        protected TextView estimatedTime;
        protected Group gpEstimatedTime,gpReportedOn,gpGrievanceType;
        protected Button share;
        protected CardView c;



        public SingleItemRowHolder(View view) {
            super(view);

            this.c = (CardView) view.findViewById(R.id.card);
            this.status = (TextView) view.findViewById(R.id.tv_status_data);
            this.reportedDate = (TextView) view.findViewById(R.id.tv_reported_on_data);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.grievance = (TextView) view.findViewById(R.id.tv_grievance_type_data);
            this.estimatedTime = (TextView) view.findViewById(R.id.tv_estimated_time_data);
            this.gpEstimatedTime=view.findViewById(R.id.gp_estimated_time);
            this.gpReportedOn=view.findViewById(R.id.gp_reported_on);



            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int index = getAdapterPosition();
                    Intent mIntentToIndividualCard =new Intent(mContext,Individual_Detail_Activity.class);
                    mIntentToIndividualCard.putExtra("MComplainIdkey",itemsList.get(index).getId());
                    mContext.startActivity(mIntentToIndividualCard);

                }
            });


        }

    }
}