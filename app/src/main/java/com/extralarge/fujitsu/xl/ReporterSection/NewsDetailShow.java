package com.extralarge.fujitsu.xl.ReporterSection;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.extralarge.fujitsu.xl.AbsRuntimePermission;
import com.extralarge.fujitsu.xl.MainActivity;
import com.extralarge.fujitsu.xl.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsDetailShow extends AbsRuntimePermission implements View.OnClickListener {

    TextView mtype, mheadline, mcontent, mcaption;
    ImageView mnewsimmage, mbackimage, mshareimage;
    String type, headline, content, caption, image,id;
    URL url;
    Button mgobckbtn;
    Bitmap myBitmap;
    private static final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_show);

        mtype = (TextView) findViewById(R.id.newstype);
        mheadline = (TextView) findViewById(R.id.newsheadline);
        mcontent = (TextView) findViewById(R.id.newscontent);
        mcaption = (TextView) findViewById(R.id.newsimgcaption);
        mnewsimmage = (ImageView) findViewById(R.id.newsimage);
        mgobckbtn = (Button) findViewById(R.id.back_btn);

        mbackimage = (ImageView) findViewById(R.id.back_image);
        mshareimage = (ImageView) findViewById(R.id.share_image);
        mbackimage.setOnClickListener(this);
        mshareimage.setOnClickListener(this);

        mgobckbtn.setOnClickListener(this);

//        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_newsdetail);
//        toolbar.setTitle("EXCEL");
//        toolbar.setTitleTextColor(ContextCompat.getColor(NewsDetailShow.this, R.color.black));
//
//        mshareimage = (ImageView) toolbar.findViewById(R.id.share_image);
//        mshareimage.setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        headline = intent.getStringExtra("headline");
        content = intent.getStringExtra("content");
        // caption = intent.getStringExtra("caption");
        image = intent.getStringExtra("image");
        id = intent.getStringExtra("id");

         mtype.setText(type);
        mheadline.setText(headline);
        mcontent.setText(content);
        //   mcaption.setText(caption);

        try {
            url = new URL(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Picasso.with(getApplicationContext()).load(String.valueOf(url)).resize(1024, 1024).into(mnewsimmage);




    }

    @Override
    public void onPermissionsGranted(int requestCode) {

        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        myBitmap = v1.getDrawingCache();
        saveBitmap(myBitmap);

    }

    public void saveBitmap(Bitmap bitmap) {
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "Pictures/screenshot.png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            sendMail(imagePath);
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public void sendMail(File  file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "For Latest News Updates \n Download \n Excel Samachar App \n https://play.google.com/store/apps/details?id=midigi.testaws&hl=en");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.back_btn:

                NewsDetailShow.this.finish();
                break;
            case R.id.back_image:

                NewsDetailShow.this.finish();
                break;
            case R.id.share_image:


                requestAppPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        R.string.msg, REQUEST_PERMISSION);

                break;


        }
    }



}
