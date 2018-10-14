package ml.uncoded.margsahayak.Offline;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import ml.uncoded.margsahayak.Auth.SharedPrefrenceUser;
import ml.uncoded.margsahayak.Input.ImageUpload;
import ml.uncoded.margsahayak.Input.InputActivity2;
import ml.uncoded.margsahayak.MainActivity;
import ml.uncoded.margsahayak.MainApplication;
import ml.uncoded.margsahayak.Network.mCallBack;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.BisagResponse;
import ml.uncoded.margsahayak.models.ComplainModel;
import ml.uncoded.margsahayak.models.Dialogs;
import ml.uncoded.margsahayak.models.OfflineComplainModel;
import ml.uncoded.margsahayak.models.StaticMethods;


public class OfflineComplainListDataAdapter extends RecyclerView.Adapter<OfflineComplainListDataAdapter.SingleItemRowHolder> {

    private ArrayList<OfflineComplainModel> itemsList;
    private Context mContext;

    public OfflineComplainListDataAdapter(Context context, ArrayList<OfflineComplainModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offline_complain_individual_card, viewGroup, false);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {


        OfflineComplainModel singleItem = itemsList.get(i);
        holder.grievance.setText(singleItem.getGrievanceName().toUpperCase());
        Log.v("debug", itemsList.toString());
        Glide.with(mContext)
                .load(singleItem.getImgurl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_close_black_24dp).into(holder.itemImage);
        holder.itemImage.setColorFilter(Color.argb(100, 0, 0, 0));
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    void deleteComplaint(int index) {
        Log.d("adapterPos",String.valueOf(index));

        if(index < 0) return;
        OfflineComplainModel singleItem = itemsList.get(index);
        Realm r = Realm.getDefaultInstance();
        r.beginTransaction();
        OfflineComplainModel offlineComplainModel = r.where(OfflineComplainModel.class).equalTo("id", singleItem.getId()).findFirst();
        offlineComplainModel.deleteFromRealm();
        r.commitTransaction();
    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {


        protected ImageView itemImage;
        protected TextView grievance;
        protected Button btnCancel, btnRetryUpload;
        protected CardView c;
        protected ProgressBar mProgressBar;

        public SingleItemRowHolder(final View view) {
            super(view);

            this.c = (CardView) view.findViewById(R.id.offline_card);
            this.itemImage = (ImageView) view.findViewById(R.id.img_grievance);
            this.grievance = (TextView) view.findViewById(R.id.tv_grievance_name);
            this.btnCancel = view.findViewById(R.id.btn_cancel);
            this.btnRetryUpload = view.findViewById(R.id.btn_retry_upload);
            this.mProgressBar = view.findViewById(R.id.offlineProgressBar);
            mProgressBar.setVisibility(View.INVISIBLE);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteComplaint(getAdapterPosition());
                }
            });

            btnRetryUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainApplication m = (MainApplication) mContext.getApplicationContext();
                    final int complainIndex = getAdapterPosition();
                    OfflineComplainModel singleItem = itemsList.get(complainIndex);
                    Realm r = Realm.getDefaultInstance();
                    final OfflineComplainModel mInputData = r.where(OfflineComplainModel.class).equalTo("id", singleItem.getId()).findFirst();
                    //Give save offline or cancel dialog with pls enable internet to ulasd internet
                    if (!StaticMethods.checkInternetConnectivity(mContext)){mProgressBar.setVisibility(View.INVISIBLE);}
                    else{
                        mProgressBar.setVisibility(View.VISIBLE);
                         m.mApi.callBisagApi(mInputData.getLocation().get(0), mInputData.getLocation().get(1))
                                .enqueue(new mCallBack<List<BisagResponse>>(mContext, mProgressBar) {

                                    @Override
                                    public void onSuccessfullResponse(View progressBar, List<BisagResponse> response, Context c) {

                                        float minD = Float.parseFloat(response.get(0).distance), tempD;
                                        int index = 0;
                                        for (int i = 0; i < response.size(); i++) {
                                            index = i;
                                            tempD = Float.parseFloat(response.get(0).distance);
                                            if (minD > tempD) {
                                                minD= tempD;
                                            }
                                        }

                                        //Show you are too far away from road
                                        //Canot upload complain
                                        // ok

                                        if (minD > 1.5) {
                                            final Dialog mDistanceDialog = new Dialog(mContext);
                                            mDistanceDialog.setContentView(R.layout.custom_dialog_layout);
                                            ((TextView) mDistanceDialog.findViewById(R.id.customDialog_tv_message)).setText("You are too far away from road");
                                            mDistanceDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                                            mDistanceDialog.show();

                                            ((Button) mDistanceDialog.findViewById(R.id.customDialog_btn_Ok)).setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    mDistanceDialog.dismiss();
                                                    deleteComplaint(complainIndex);

                                                }
                                            });
                                            return;
                                        }

                                        BisagResponse mBisagresponse = response.get(index);

                                        Toast.makeText(mContext, "" +
                                                mBisagresponse.roadCode + " " + mBisagresponse.roadName + " " +
                                                mBisagresponse.raodType, Toast.LENGTH_SHORT).show();

                                        //Upload image ---
                                        //String filePath = "/storage/emulated/0/Pictures/Marg_Sahayak/IMG_20180928_155751.jpg";
                                        String filePath = mInputData.getImgurl();
                                      //  Toast.makeText(c, "Filepath : " + filePath, Toast.LENGTH_SHORT).show();
                                        Log.d("debug", filePath);
                                        ImageUpload mImageUpload = new ImageUpload(mContext, filePath, mProgressBar);
                                        ImageUpload.uploadImage();
                                        ComplainModel mComplainModel = new ComplainModel();
                                        mComplainModel.setDescription(mInputData.getGrievanceDescription());
                                        Log.d("debug", "onSuccessfullResponse: " + mComplainModel.getComplaintStatus());
                                        mComplainModel.setGrivType(mInputData.getGrievanceName());
                                        mComplainModel.setTime(mInputData.getTime());
                                        //Log.d("ImageUrl from offAdap",mInputData.getImgurl());
                                       // Toast.makeText(c, "ImgaeURl"+mInputData.getImgurl(), Toast.LENGTH_SHORT).show();

                                        String[] tempS = mInputData.getImgurl().split("/");
                                        mComplainModel.setUrl(tempS[tempS.length - 1]);

                                        Log.d("offlineUrl", "onSuccessfullResponse: " + mInputData.getImgurl() + "  " + tempS[tempS.length -1]);

                                        mComplainModel.setLocation(mInputData.getLocation());
                                        mComplainModel.setComplaintStatus("Pending.");
                                        mComplainModel.setRoadCode(mBisagresponse.roadCode);
                                        mComplainModel.setRoadName(mBisagresponse.roadName);
                                        mComplainModel.setRoadType(mBisagresponse.raodType);
                                        // Upload Complain ---
                                        ((MainApplication) mContext.getApplicationContext()).mApi
                                                .postComplaint(SharedPrefrenceUser.getInstance(mContext).getToken(), mComplainModel)
                                                .enqueue(new mCallBack<ComplainModel>(mContext, mProgressBar) {

                                                    @Override
                                                    public void onSuccessfullResponse(final View progressBar, final ComplainModel response, Context c) {


                                                        if (Boolean.parseBoolean(response.getSuccess())) {

                                                            Realm r = Realm.getDefaultInstance();
                                                            r.executeTransactionAsync(new Realm.Transaction() {
                                                                @Override
                                                                public void execute(Realm realm) {
                                                                    realm.copyToRealm(response);

                                                                }
                                                            }, new Realm.Transaction.OnSuccess() {
                                                                @Override
                                                                public void onSuccess() {
                                                                    deleteComplaint(complainIndex);
                                                                    final Dialog mSuccessDialog = new Dialog(mContext);
                                                                    mSuccessDialog.setContentView(R.layout.custom_dialog_layout);
                                                                    ((TextView) mSuccessDialog.findViewById(R.id.customDialog_tv_message)).setText("Complaint Posted Successfully");
                                                                    mSuccessDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                                                                    mSuccessDialog.show();
                                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                                    ((Button) mSuccessDialog.findViewById(R.id.customDialog_btn_Ok)).setOnClickListener(new View.OnClickListener() {

                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            mSuccessDialog.dismiss();
                                                                        }
                                                                    });

                                                                }
                                                            });
                                                        } else {
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            Dialogs dialogs;
                                                            String strErrMsg="There seems some problem with connectivity.\nPlease try again after sometime";
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            dialogs = new Dialogs(strErrMsg);
                                                            dialogs.show(((Activity) c).getFragmentManager(), "ErrMSG");
                                                            Toast.makeText(c, "" + response.getMsg(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailureResponse(View progressBar, Context c) {

                                                    }
                                                });

                                    }

                                    @Override
                                    public void onFailureResponse(View progressBar, Context c) {
                                        Toast.makeText(mContext, "Bisag Response failure", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }


                }
            });


        }


    }
}