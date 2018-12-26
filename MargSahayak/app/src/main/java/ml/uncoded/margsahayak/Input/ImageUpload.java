package ml.uncoded.margsahayak.Input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import ml.uncoded.margsahayak.Auth.SharedPrefrenceUser;
import ml.uncoded.margsahayak.MainApplication;
import ml.uncoded.margsahayak.Network.mCallBack;
import ml.uncoded.margsahayak.models.AuthResponse;
import ml.uncoded.margsahayak.models.BisagResponse;
import ml.uncoded.margsahayak.models.OfflineComplainModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageUpload {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static String filePath;
    private static int uploadingTries = 0;
    @SuppressLint("StaticFieldLeak")
    private static View mImageUploadProgressBar;

    ImageUpload() {
    }

    public ImageUpload(Context context, String filePath, View mImageUploadProgressBar) {
        ImageUpload.filePath = filePath;
        ImageUpload.context = context;
        ImageUpload.mImageUploadProgressBar = mImageUploadProgressBar;
    }

    public static void uploadImage() {

        if (ImageUpload.uploadingTries > 5) return;
        File mFile = null;
        mFile = new File(ImageUpload.filePath);
        //Toast.makeText(context, "File : " + mFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), mFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", mFile.getName(), reqFile);

        ((MainApplication) ImageUpload.context.getApplicationContext()).mApi
                .uploadImage(SharedPrefrenceUser.getInstance(ImageUpload.context).getToken(), body)
                .enqueue(
                new mCallBack<AuthResponse>(ImageUpload.context, ImageUpload.mImageUploadProgressBar) {
                    @Override
                    public void onSuccessfullResponse(View progressBar, AuthResponse response, Context c) {
                        if (!Boolean.parseBoolean(response.success)) {
                            ImageUpload.uploadingTries++;
                            //Toast.makeText(c, "response not succesful# " + ImageUpload.uploadingTries, Toast.LENGTH_SHORT).show();
                            ImageUpload.uploadImage();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            //Toast.makeText(ImageUpload.context, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailureResponse(View progressBar, Context c) {
                    }
                });
    }
}
