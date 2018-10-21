package ml.uncoded.margsahayak.Input;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import ml.uncoded.margsahayak.Auth.SharedPrefrenceUser;
import ml.uncoded.margsahayak.MainActivity;
import ml.uncoded.margsahayak.MainApplication;
import ml.uncoded.margsahayak.Network.mCallBack;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.BisagResponse;
import ml.uncoded.margsahayak.models.ComplainModel;
import ml.uncoded.margsahayak.models.Dialogs;
import ml.uncoded.margsahayak.models.OfflineComplainModel;
import ml.uncoded.margsahayak.models.StaticMethods;

public class InputActivity2 extends AppCompatActivity {


    private Uri fileUri, mCropImagedUri;
    Bitmap bitmap = null;
    String mDiscription, mGri;

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    private static final int CROP_IMAGE_REQUEST_CODE = 102;

    public static final int MEDIA_TYPE_IMAGE = 1;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Marg_Sahayak";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input3);
        mDiscription = "No Description";
        initListners();
    }
    private void initListners() {

        final Button btnDescription=findViewById(R.id.add_discription);
        //Code for both add discription and select greivance
        btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog mAdd_discription_dialog = new Dialog(InputActivity2.this);
                mAdd_discription_dialog.setContentView(R.layout.discription_add_dialog_layout);
                final EditText edtDescription=mAdd_discription_dialog.findViewById(R.id.edt_discription);
                mAdd_discription_dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                if(mDiscription == "No Description")
                    edtDescription.setText("");
                else
                    edtDescription.setText(mDiscription);

                mAdd_discription_dialog.show();
                mAdd_discription_dialog.findViewById(R.id.btn_add_discription).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDiscription = edtDescription.getText().toString().trim();
                        if(!mDiscription.equals("No Description")){
                            btnDescription.setText("Edit Description");
                            edtDescription.setText(mDiscription);
                        }
                        if(mDiscription==null||mDiscription.trim().length()==0){
                            mDiscription = "No Description";
                            btnDescription.setText("Add Description");

                        }
                        mAdd_discription_dialog.dismiss();

                    }
                });

                mAdd_discription_dialog.findViewById(R.id.btn_cancle_description).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdd_discription_dialog.dismiss();
                    }
                });
            }
        });

        findViewById(R.id.select_grievance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog mSelect_grievance_dialog = new Dialog(InputActivity2.this);
                mSelect_grievance_dialog.setContentView(R.layout.grievance_select_dialog_layout);
                mSelect_grievance_dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                final ListView mGrievanceListView = (ListView) mSelect_grievance_dialog.findViewById(R.id.select_grievance_list);
                String[] mListOfGrievance = getResources().getStringArray(R.array.select_grievance_array);
                ArrayAdapter mGrievanceAdapter = new ArrayAdapter(InputActivity2.this, android.R.layout.simple_list_item_1, mListOfGrievance);
                mGrievanceListView.setAdapter(mGrievanceAdapter);
                mGrievanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        mGri = (String) adapterView.getAdapter().getItem(i);
                        ((Button) findViewById(R.id.select_grievance)).setText(mGri);
                        mSelect_grievance_dialog.dismiss();
                    }
                });

                mSelect_grievance_dialog.show();
            }
        });


        findViewById(R.id.capture_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureImage();

            }
        });

        findViewById(R.id.btn_retry_img_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureImage();

            }
        });
        findViewById(R.id.closeInput).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs dialogs;
                //Get input data & put into mInput Data
                if(mCropImagedUri==null){
                    dialogs = new Dialogs("Please Capture An Image");
                    dialogs.show(getFragmentManager(), "ErrMSG");
                }
                else if(mGri==null||mGri.length()==0){
                    dialogs = new Dialogs("Please Select Grievance");
                    dialogs.show(getFragmentManager(), "ErrMSG");
                }
                else{
                    final OfflineComplainModel mInputData = new OfflineComplainModel();
                    mInputData.setGrievanceDescription(mDiscription);
                    mInputData.setGrievanceName(mGri);
                    String[] path = mCropImagedUri.getPath().split("/");
                    mInputData.setImgurl(path[path.length - 1]);


                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    mInputData.setTime(formatter.format(date));

                    //code to get Location from gps
                    LocationManager locationManager = SingletonLocationManager.getInstance(getApplication(), "From Upload Button");
                    MyLocationListener locationListener = MyLocationListener.getInstance("Upload Button Input Activity");
                    String lon = null, lat = null;
                    if (locationManager != null) {
                        Log.d("MyLocationListner", "LocationListner Stopped");
                        if (ActivityCompat.checkSelfPermission(InputActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(InputActivity2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (l == null) {
                            l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                        if (l == null) {
                            l = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        }
                        if (l != null) {
                            lon = "" + l.getLongitude();
                            lat = "" + l.getLatitude();
                        }
                        if (locationListener.lon != null && locationListener.lat != null) {
                            Toast.makeText(InputActivity2.this, "" + locationListener.lon + locationListener.lat, Toast.LENGTH_SHORT).show();
                            lon = locationListener.lon;
                            lat = locationListener.lat;
                        }
                        locationManager.removeUpdates(locationListener);
                        locationListener = null;
                    }

                    lat = "23.10524664";
                    lon = "72.58701144";
                    //location set in offline complain model
                    RealmList<String> mLocationList = new RealmList<String>(lat, lon);
                    mInputData.setLocation(mLocationList);


                    //Give save offline or cancel dialog with pls enable internet to ulasd internet
                    if (!StaticMethods.checkInternetConnectivity(InputActivity2.this)) {
                        //diaog
                        final Dialog mOfflineDialog = new Dialog(InputActivity2.this);
                        mOfflineDialog.setContentView(R.layout.save_to_offline_dialog_layout);
                        mOfflineDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                        mOfflineDialog.show();

                        ((Button) mOfflineDialog.findViewById(R.id.save_offline_complaint)).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                mOfflineDialog.dismiss();
                                saveOffline(mInputData);

                            }
                        });

                        ((Button) mOfflineDialog.findViewById(R.id.cancel_complaint)).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                mOfflineDialog.dismiss();
                                //startActivity(new Intent(InputActivity2.this, MainActivity.class));
                                finish();
                            }
                        });
                    } else {
                        //Check Bisag APi
                        final ProgressBar mProgressBar = findViewById(R.id.InputProgressBar);
                        mProgressBar.setVisibility(View.VISIBLE);
                        final MainApplication m = ((MainApplication) getApplicationContext());
                        Log.d("MyLocationListner", lon + lat);
                        lat = "23.10524664";
                        lon = "72.58701144";
                        Toast.makeText(InputActivity2.this, "" + lat + lon, Toast.LENGTH_SHORT).show();

                        m.mApi.callBisagApi(lat, lon)
                                .enqueue(new mCallBack<List<BisagResponse>>(InputActivity2.this, mProgressBar) {

                                    @Override
                                    public void onSuccessfullResponse(View progressBar, List<BisagResponse> response, Context c) {

                                        float minD = Float.parseFloat(response.get(0).distance), tempD;
                                        int index = 0;
                                        for (int i = 0; i < response.size(); i++) {
                                            index = i;
                                            tempD = Float.parseFloat(response.get(0).distance);
                                            if (minD > tempD) {
                                                minD = tempD;
                                            }
                                        }

                                        //Show you are too far away from road
                                        //Canot upload complain
                                        // ok

                                        if (minD > 1.5) {
                                            String strErrMsg = "You are too far away from road.";
                                            Dialogs dialogs;
                                            dialogs = new Dialogs(strErrMsg);
                                            dialogs.show(((Activity) c).getFragmentManager(), "ErrMSG");

//      Dialog mDistanceDialog=new Dialog(InputActivity2.this);
//                                        mDistanceDialog.setContentView(R.layout.custom_dialog_layout);
//                                        ((TextView)mDistanceDialog.findViewById(R.id.customDialog_tv_message)).setText("You are too far away from road");
//                                        mDistanceDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
//                                        mDistanceDialog.show();
//
//                                        ((Button)mDistanceDialog.findViewById(R.id.customDialog_btn_Ok)).setOnClickListener(new View.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(View v) {
//                                                finish();
//                                            }
//                                        });
                                            return;
                                        }

                                        BisagResponse mBisagresponse = response.get(index);

                                        Toast.makeText(InputActivity2.this, "" +
                                                mBisagresponse.roadCode + " " + mBisagresponse.roadName + " " +
                                                mBisagresponse.raodType, Toast.LENGTH_SHORT).show();

                                        //Upload image ---
                                        //String filePath = "/storage/emulated/0/Pictures/Marg_Sahayak/IMG_20180928_155751.jpg";
                                        String filePath = mCropImagedUri.getPath();
                                        //   Toast.makeText(c, "Filepath : " + filePath, Toast.LENGTH_SHORT).show();
                                        Log.d("debug", filePath);
                                        ImageUpload mImageUpload = new ImageUpload(InputActivity2.this, filePath, mProgressBar);
                                        ImageUpload.uploadImage();
                                        ComplainModel mComplainModel = new ComplainModel();
                                        mComplainModel.setDescription(mInputData.getGrievanceDescription());
                                        Log.d("debug", "onSuccessfullResponse: " + mComplainModel.getComplaintStatus());
                                        mComplainModel.setGrivType(mInputData.getGrievanceName());
                                        mComplainModel.setTime(mInputData.getTime());
                                        mComplainModel.setUrl(mInputData.getImgurl());
                                        mComplainModel.setLocation(mInputData.getLocation());
                                        mComplainModel.setComplaintStatus("Pending.");
                                        mComplainModel.setRoadCode(mBisagresponse.roadCode);
                                        mComplainModel.setRoadName(mBisagresponse.roadName);
                                        mComplainModel.setRoadType(mBisagresponse.raodType);
                                        // Upload Complain ---
                                        ((MainApplication) getApplicationContext()).mApi
                                                .postComplaint(SharedPrefrenceUser.getInstance(InputActivity2.this).getToken(), mComplainModel)
                                                .enqueue(new mCallBack<ComplainModel>(InputActivity2.this, mProgressBar) {

                                                    @Override
                                                    public void onSuccessfullResponse(final View progressBar, final ComplainModel response, Context c) {


                                                        if (Boolean.parseBoolean(response.getSuccess())) {

                                                            Realm r = Realm.getDefaultInstance();
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            r.executeTransactionAsync(new Realm.Transaction() {
                                                                @Override
                                                                public void execute(Realm realm) {
                                                                    realm.copyToRealm(response);

                                                                }
                                                            }, new Realm.Transaction.OnSuccess() {
                                                                @Override
                                                                public void onSuccess() {
//                                                                Intent i = new Intent(InputActivity2.this, MainActivity.class);
//                                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                                                startActivity(i);
                                                                    finish();
                                                                }
                                                            });
                                                        } else {
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
                                        Toast.makeText(InputActivity2.this, "Bisag Response failure", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });
    }

    private void captureImage() {

        requestCameraRuntimePermission();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    public void requestCameraRuntimePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");

                //handle folder not created error

                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CROP_IMAGE_REQUEST_CODE && null != data) {
            if (resultCode == RESULT_OK) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                bitmap = BitmapFactory.decodeFile(mCropImagedUri.getPath(), options);
                showImage();
            }
            else if (resultCode == RESULT_CANCELED) {

                finish();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to crop image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        if (requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //  Toast.makeText(this, "" + picturePath, Toast.LENGTH_SHORT).show();
            //  fileUri = Uri.fromFile(new File(picturePath));
            fileUri = Uri.parse(picturePath);
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 1;
            bitmap = BitmapFactory.decodeFile(picturePath,
                    options);

            showImage();


        } else if (requestCode == 1) {
            Intent i = new Intent(this, MainActivity.class);
            // startActivity(i);
            Toast.makeText(getApplicationContext(),
                    "User cancelled permission", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                rotateImageFunction();
                cropImageFunction();

            } else if (resultCode == RESULT_CANCELED) {

                finish();
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    void rotateImageFunction() {


        try {
            ExifInterface ei = new ExifInterface(fileUri.getPath());
            int ori = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch (ori) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            bitmap = rotatedBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void cropImageFunction() {
//call the standard crop action intent (the user device may not support it)
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri
        cropIntent.setDataAndType(fileUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("scale", true);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 500);
        cropIntent.putExtra("outputY", 500);
        //retrieve data on return
        cropIntent.putExtra("return-data", false);

        mCropImagedUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, CROP_IMAGE_REQUEST_CODE);


    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void showImage() {
        findViewById(R.id.cameraIcon).setVisibility(View.INVISIBLE);
        ImageView mImageView = (ImageView) findViewById(R.id.imagePriview);
        mImageView.setVisibility(View.VISIBLE);
        findViewById(R.id.btn_retry_img_capture).setVisibility(View.VISIBLE);
        mImageView.setImageBitmap(bitmap);
        findViewById(R.id.take_picture_instruction).setVisibility(View.INVISIBLE);
        findViewById(R.id.capture_img).setVisibility(View.GONE);
    }

    private void saveOffline(final OfflineComplainModel mInputData) {

        Realm r = Realm.getDefaultInstance();
        r.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Realm r = Realm.getDefaultInstance();
                Number maxId = r.where(OfflineComplainModel.class).max("id");
                int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                OfflineComplainModel offlineComplainModel = r.createObject(OfflineComplainModel.class, nextId);
                offlineComplainModel.setTime(mInputData.getTime());
                offlineComplainModel.setGrievanceDescription(mInputData.getGrievanceDescription());
                offlineComplainModel.setLocation(mInputData.getLocation());
                offlineComplainModel.setGrievanceName(mInputData.getGrievanceName());
                offlineComplainModel.setImgurl(mCropImagedUri.getPath());

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Intent i = new Intent(InputActivity2.this, MainActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(i);
                finish();

            }
        });


    }
}

