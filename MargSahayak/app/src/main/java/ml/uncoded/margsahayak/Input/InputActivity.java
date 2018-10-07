package ml.uncoded.margsahayak.Input;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import ml.uncoded.margsahayak.MainActivity;
import ml.uncoded.margsahayak.R;
import ml.uncoded.margsahayak.models.StaticMethods;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class InputActivity extends AppCompatActivity{

    public LocationManager locationManager;
    MyLocationListener locationListener;
    String lat, lon;
    String dis = "No discription";
    Bitmap bitmap = null;


    //UI


    // Activity request codes
        private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
        private static final int GALLERY_IMAGE_REQUEST_CODE = 101;
    private String griString;
    public static final int MEDIA_TYPE_IMAGE = 1;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Marg_Sahayak";
    private String lat2,lang;
    private Uri fileUri;
    private ImageView imgPreview;
    private String finalImageUrl;
    private boolean continueInput = false;
    private ImageView imgPreviewMain;
    private int acc;

    public InputActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Intent i = getIntent();
        lon = i.getStringExtra("lon");
        lat = i.getStringExtra("lat");

        acc = i.getIntExtra("acc",100);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

      //      locationListener = new MyLocationListener(this, lat,lon,acc);
     //       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
        }
        captureImage();
        setContentView(R.layout.activity_input);


        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        imgPreviewMain = (ImageView) findViewById(R.id.imagePreviewMain);
        Date currentTime = Calendar.getInstance().getTime();
        TextView d = (TextView)findViewById(R.id.time);
        final TextView loc = (TextView)findViewById(R.id.locationEdit);
        final EditText location = (EditText) findViewById(R.id.locationEdit);
        location.setText("23.75435,"+"\n"+"72.345435");

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(locationManager != null) {

                    Toast.makeText(getApplicationContext(),"LocationListner Stopped",Toast.LENGTH_LONG);
                    locationManager.removeUpdates(locationListener);
                    locationManager = null;
                }

            }
        });

        d.setText(currentTime.toString());
        final EditText textArea = (EditText) findViewById(R.id.textArea_information);

        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, GALLERY_IMAGE_REQUEST_CODE);
            }
        });


        String[] s=  {"1","2"};
        ArrayList<String> gri = new ArrayList<String>();
        Collections.addAll(gri,s);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, gri);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner sItems = (Spinner) findViewById(R.id.grivance);
        final int length = gri.size();
        sItems.setAdapter(adapter);

        Button submit = (Button) findViewById(R.id.submit);

        Button cancel = (Button) findViewById(R.id.cancelLink);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(input.this,navigationMain.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String locationEdited = location.getText().toString();
                final String[] locationEditedA = locationEdited.split(",");

                griString = sItems.getSelectedItem().toString();

                try {
                    griString = URLEncoder.encode(griString,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                dis = textArea.getText().toString();

                if(dis.isEmpty())
                    dis="No Desciption";
                try {
                    dis = URLEncoder.encode(dis,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //Reploace
                lang = locationEditedA[1].replaceAll("\n","");
                lat2 = locationEditedA[0].replaceAll("\n","");;
                Log.v("input2",lang + lat2);
//                final double lang = Double.parseDouble(locationListener.lat);
//                final double lat2 = Double.parseDouble(locationListener.lon);

                Log.v("MyLocationListener", " " + lang + " " + lat2);
//                if(locationManager != null) {
//                    locationManager.removeUpdates(locationListener);
//                    locationManager = null;
//                }



                if(StaticMethods.checkInternetConnectivity(InputActivity.this)){
                    //Only Image Upload
                    String filePath = fileUri.getPath();
                    View mView = findViewById(R.id.progressBarInput);
                    mView.setVisibility(View.VISIBLE);
                    ImageUpload mImageUpload = new ImageUpload(InputActivity.this,filePath,mView);
                    ImageUpload.uploadImage();
                }else{
                    //Inset into Offline db
                }
            }
        });


    }


    private void captureImage() {

        requestRuntimePermission();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void requestRuntimePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Toast.makeText(this, ""+picturePath, Toast.LENGTH_SHORT).show();
            //  fileUri = Uri.fromFile(new File(picturePath));
                fileUri=Uri.parse(picturePath);
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 1;
            bitmap = BitmapFactory.decodeFile(picturePath,
                    options);

            imgPreviewMain.setImageBitmap(bitmap);
            imgPreview.setImageBitmap(bitmap);


        }else if (requestCode == 1) {
            Intent i = new Intent(this,MainActivity.class);
            // startActivity(i);
            Toast.makeText(getApplicationContext(),
                    "User cancelled permission", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                    imgPreviewMain.setVisibility(View.VISIBLE);
                    imgPreview.setVisibility(View.VISIBLE);
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    options.inSampleSize = 1;
//                    String path = MediaStore.Images.Media.insertImage(InputActivity.this.getContentResolver(), bitmap, "Title", "Image stored");
//                    fileUri =  Uri.parse(path);

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                    imgPreviewMain.setImageBitmap(bitmap);
                    imgPreview.setImageBitmap(bitmap);

            } else if (resultCode == RESULT_CANCELED) {

                // Intent i = new Intent(this,MainActivity.class);
                // startActivity(i);
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
        }else {
            return null;
        }

        return mediaFile;
    }
}