package ml.uncoded.margsahayak.History;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.Group;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
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
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import io.realm.RealmList;
import ml.uncoded.margsahayak.Individual_Detail_Activity;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.NotificationComplaintModel;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;


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
        StringBuilder mCommentsString = new StringBuilder();
        int commentListsize=singleItem.getComments().size();
        if(singleItem.getComplaintStatus().toUpperCase().equals("APPROVED"))
        {

            holder.itemImage.setImageResource(R.drawable.ic_noti_approved);
            holder.gpEstimatedDate.setVisibility(View.VISIBLE);
            holder.status.setText(singleItem.getComplaintStatus().toUpperCase());
            holder.status.setTextColor(0xFF0277BD);
            if(holder.getAdapterPosition()==0){holder.dividerLineU.setVisibility(View.GONE);}
            if(holder.getAdapterPosition()==(itemsList.size()-1)){holder.dividerLineL.setVisibility(View.GONE);}
            holder.EstimatedDate.setText(singleItem.getEstimatedDate());
            if(commentListsize!=0 && singleItem.getComments()!=null){
                RealmList<String> commentData = singleItem.getComments();
                for(int j=0;j < commentData.size(); j++){
                    mCommentsString.append(commentData.get(j));
                }
                holder.gpComments.setVisibility(View.VISIBLE);
                if(mCommentsString.length()>0&&mCommentsString.toString()!=null)
                {
                    holder.comments.setText(mCommentsString);
                }
                else
                {
                    holder.comments.setText("No Comments");
                }
            }

        }
         if(singleItem.getComplaintStatus().toUpperCase().equals("REJECTED"))
         {

             holder.itemImage.setImageResource(R.drawable.ic_noti_rejected);
               holder.status.setText(singleItem.getComplaintStatus().toUpperCase());
                holder.status.setTextColor(0xFFC62828);
             if(holder.getAdapterPosition()==0){holder.dividerLineU.setVisibility(View.GONE);}
             if(holder.getAdapterPosition()==(itemsList.size()-1)){holder.dividerLineL.setVisibility(View.GONE);}
             if(commentListsize!=0 && singleItem.getComments()!=null){
                   RealmList<String> commentData = singleItem.getComments();
                   for(int j=0;j < commentData.size(); j++){
                       mCommentsString.append(commentData.get(j));
                   }
                   holder.gpComments.setVisibility(View.VISIBLE);
                 //  holder.comments.setText(mCommentsString);
                 if(mCommentsString.length()>0&&mCommentsString.toString()!=null)
                 {
                     holder.comments.setText(mCommentsString);
                 }
                 else
                 {
                     holder.comments.setText("No Comments");
                 }


             }

        }
        if(singleItem.getComplaintStatus().toUpperCase().equals("IN PROGRESS")){
            holder.itemImage.setImageResource(R.drawable.ic_noti_workinprogress);
            holder.status.setText(singleItem.getComplaintStatus().toUpperCase());
            holder.status.setTextColor(0xFFFBC02D);
            if(holder.getAdapterPosition()==0){holder.dividerLineU.setVisibility(View.GONE);}
            if(holder.getAdapterPosition()==(itemsList.size()-1)){holder.dividerLineL.setVisibility(View.GONE);}



            if(commentListsize!=0 && singleItem.getComments()!=null){
                RealmList<String> commentData = singleItem.getComments();
                for(int j=0;j < commentData.size(); j++){
                    mCommentsString.append(commentData.get(j));
                }

                    holder.gpComments.setVisibility(View.VISIBLE);
                if(mCommentsString.length()>0&&!(mCommentsString.toString().equals("null")))
                {
                    holder.comments.setText(mCommentsString);
                }
                else
                {
                    holder.comments.setText("No Comments");
                }

                holder.gpEstimatedDate.setVisibility(View.VISIBLE);
                holder.EstimatedDate.setText(singleItem.getEstimatedDate());


            }

        }
        if(singleItem.getComplaintStatus().toUpperCase().equals("COMPLETED")){
            holder.itemImage.setImageResource(R.drawable.ic_noti_completed);
            holder.status.setText(singleItem.getComplaintStatus().toUpperCase());
            holder.status.setTextColor(0xFF2E7D32);
            if(holder.getAdapterPosition()==0){holder.dividerLineU.setVisibility(View.GONE);}
            if(holder.getAdapterPosition()==(itemsList.size()-1)){holder.dividerLineL.setVisibility(View.GONE);}
            if(commentListsize!=0 && singleItem.getComments()!=null && !(singleItem.getComments().isEmpty())){
                RealmList<String> commentData = singleItem.getComments();
                for(int j=0;j < commentData.size(); j++){
                    mCommentsString.append(commentData.get(j));
                }
                Log.e("Comments from noti",mCommentsString.toString());
                holder.gpComments.setVisibility(View.VISIBLE);
                if(mCommentsString.length()>0 && !(mCommentsString.toString().equals("null")) )
                {
                    holder.comments.setText(mCommentsString);
                }
                else
                {
                    holder.comments.setText("No Comments");
                }

            }
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected ImageView itemImage;
        protected TextView status;
        protected TextView EstimatedDate,comments;
        protected Group gpComments,gpEstimatedDate;
        protected Button share;
        protected View dividerLineU,dividerLineL;
        protected CardView c;



        public SingleItemRowHolder(View view) {
            super(view);

            this.c = (CardView) view.findViewById(R.id.card);
            this.status = (TextView) view.findViewById(R.id.tv_status_data);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.EstimatedDate = (TextView) view.findViewById(R.id.tv_estimated_time_data);
            this.comments=view.findViewById(R.id.tv_comment_data);
            this.gpComments=view.findViewById(R.id.gp_comments);
            this.gpEstimatedDate=view.findViewById(R.id.gp_estimated_time);
            this.dividerLineU=view.findViewById(R.id.divider_line_upper);
            this.dividerLineL=view.findViewById(R.id.divider_line_lower);

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