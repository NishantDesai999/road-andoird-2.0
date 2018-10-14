package ml.uncoded.margsahayak;

import android.content.Context;
import android.content.Intent;
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

import ml.uncoded.margsahayak.models.ComplainModel;


public class FilterDataAdapter extends RecyclerView.Adapter<FilterDataAdapter.SingleItemRowHolder> {

    private ArrayList<ComplainModel> itemsList;
    private Context mContext;

    public FilterDataAdapter(Context context, ArrayList<ComplainModel> itemsList) {
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
       // holder.area.setText(singleItem.getTaluka().toUpperCase());
        holder.status.setText(singleItem.getComplaintStatus().toUpperCase());
//        holder.grievance.setText(singleItem.getGrivType().toUpperCase());
        holder.date.setText(singleItem.getTime().toUpperCase().substring(0,10) );
//        if(singleItem.getEstimatedTime().toUpperCase().equals("NONE") || singleItem.getComplaintStatus().toUpperCase().equals("PENDING"))
//        {
//            holder.completionDate.setVisibility(View.GONE);
//            holder.completionDate1.setVisibility(View.GONE);
//            Toast.makeText(mContext, ""+singleItem.getEstimatedTime().toUpperCase(), Toast.LENGTH_SHORT).show();
//        }
//        else
//            holder.completionDate1.setText(singleItem.getEstimatedTime().toUpperCase());
//
//        Log.v("debug",itemsList.toString());

        Glide.with(mContext)
                .load(singleItem.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_close_black_24dp).into(holder.itemImage);

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
        protected TextView completionDate,completionDate1;


        protected Button delete;
        protected Button share;

        protected CardView c;



        public SingleItemRowHolder(View view) {
            super(view);

            this.c = (CardView) view.findViewById(R.id.card);
            this.status = (TextView) view.findViewById(R.id.status);
            this.date = (TextView) view.findViewById(R.id.date);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.grievance = (TextView) view.findViewById(R.id.textView9);
            this.completionDate = (TextView) view.findViewById(R.id.textView10);
            this.completionDate1 = (TextView) view.findViewById(R.id.timeOfCompletion1);



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