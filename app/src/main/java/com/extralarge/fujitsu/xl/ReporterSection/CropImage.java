package com.extralarge.fujitsu.xl.ReporterSection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.extralarge.fujitsu.xl.R;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CropImage extends AppCompatActivity {

    CropImageView cropImageView;
    LinearLayout cropBtn;
    boolean cameraSource;
    String filename="";
    String photoUri;
    Bitmap bitmap;
    Bitmap cropped;
    public static final String PHOTO_URI="photouri";
    private static final int PICK_CROPIMAGE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        cropImageView=(CropImageView)findViewById(R.id.cropImageView);
        cropBtn=(LinearLayout)findViewById(R.id.cropBtn);

       Intent data = getIntent();
        String camearaimage = data.getStringExtra("cameraji");
        boolean checkcam = data.getBooleanExtra("camerajiboolean",false);
        Log.d("camimg00","camimg "+camearaimage);
        if(checkcam){

            bitmap = StringToBitMap(camearaimage);
            Log.d("bitmaocam00","bitmapvam "+bitmap);
        }
else {
            photoUri = getIntent().getStringExtra("ramji");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(photoUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cropImageView.setImageBitmap(bitmap);


        cropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage(bitmap);
            }
        });
    }

    public void cropImage(Bitmap bitmap)
    {
         cropped = cropImageView.getCroppedImage();
           String bitimage = getStringImage(cropped);
           Intent returnIntent = new Intent();
           returnIntent.putExtra("cropimage",bitimage);
           setResult(PICK_CROPIMAGE, returnIntent);

          finish();
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
