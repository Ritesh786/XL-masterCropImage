package com.extralarge.fujitsu.xl.ReporterSection;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.extralarge.fujitsu.xl.MainActivity;
import com.extralarge.fujitsu.xl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.bitmap;
import static android.app.Activity.RESULT_OK;
import static com.extralarge.fujitsu.xl.R.id.imageView;
import static com.extralarge.fujitsu.xl.R.id.verifyotp_btn;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

     EditText  mnewsheadline,mnewscontent,mnewsimagecaption;
     Spinner mnewstype;
     Button mchooseimagebtn,muploadnewsbtn;
    ImageView mnewsimage;
    int id;
    int strtext;
    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_REQUEST = 2;
    private static final int PICK_CROPIMAGE = 4;

    private Bitmap bitmap;
    Uri selectedImage;
    String str = "chala";
    Bundle bundle;
    String strtim;
    AlertDialog dialog;

    public static final String KEY_ID= "user_id";
    public static final String KEY_HHEADLINE = "headline";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_TYPE = "category";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CAPTION = "caption";
  //  public static final String UPLOAD_URL = "http://jigsawme.esy.es/picUpload/upload.php";


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


        mnewsheadline = (EditText) view.findViewById(R.id.news_heasdline);
        mnewscontent = (EditText) view.findViewById(R.id.news_content);
        mnewsimagecaption = (EditText) view.findViewById(R.id.news_caption);
        mnewstype = (Spinner) view.findViewById(R.id.news_type);
        mnewsimage = (ImageView) view.findViewById(R.id.news_Image);

        mchooseimagebtn = (Button) view.findViewById(R.id.chooseimage_btn);
        muploadnewsbtn = (Button) view.findViewById(R.id.uploadnews_btn);

        mchooseimagebtn.setOnClickListener(this);
        muploadnewsbtn.setOnClickListener(this);


        String newstypr[] = {"","National","International","State","Business","Bollywood","Entertainment"};

        mnewstype.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.autocomplete,newstypr);
        mnewstype.setAdapter(adapter);

        return view;
    }

    private void showFileChooser() {
try {
    if (android.os.Build.VERSION.SDK_INT >= 23) {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


    } else {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, ""), PICK_IMAGE_REQUEST);

    }
}catch (Exception e){

    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
}

    }



    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage() {



        try {
           bundle = this.getArguments();
            strtext = bundle.getInt("message", 0);
            Log.d("idv012", String.valueOf(strtext));

            final String userid = String.valueOf(strtext);
            final String headline = mnewsheadline.getText().toString().trim();
            final String content = mnewscontent.getText().toString().trim();
            final String type = mnewstype.getSelectedItem().toString();
            final String caption = mnewsimagecaption.getText().toString().trim();
            final String image = getStringImage(bitmap);


            String url = null;
            String REGISTER_URL = "http://minews.in/storage/app/public/upload.php";

            REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
            try {
                URL sourceUrl = new URL(REGISTER_URL);
                url = sourceUrl.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            final ProgressDialog loading = ProgressDialog.show(getContext(), "Uploading...", "Please wait...", false, false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("jaba", userid);
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");

                            if (success) {
                                                      mnewsimagecaption.setText("");
                                                      mnewsheadline.setText("");
                                                      mnewscontent.setText("");
                                                   //   mnewstype.setAdapter(null);
                                                      mnewsimage.setImageResource(0);
//                                PendingNews fragment = new PendingNews();
//                                FragmentManager manager = getFragmentManager();
//                                fragment.setArguments(bundle);
//                                manager.beginTransaction().replace(R.id.frame_trans, fragment).addToBackStack("Pending News").commit();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Upoading Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                            Log.d("jabadi", headline);
                            loading.dismiss();
                            Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("bada123", userid);

                            loading.dismiss();
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                            Log.d("error1234", error.toString());

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put(KEY_ID, userid);
                    params.put(KEY_HHEADLINE, headline);
                    params.put(KEY_CONTENT, content);
                    params.put(KEY_TYPE, type);
                    params.put(KEY_IMAGE, image);
                    params.put(KEY_CAPTION, caption);
                    return params;

                }

            };
           // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );


            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);



        }catch (Exception e){

            Log.d("msg1234",e.toString());
        }
    }



    @Override
    public void onClick(View v) {

        if(v == mchooseimagebtn){

            AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
            View mview =getActivity().getLayoutInflater().inflate(R.layout.chooseimage, null);
            Button mtakephoto = (Button) mview.findViewById(R.id.imagebycamera);
            mtakephoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }
            });

            Button mtakegallery = (Button) mview.findViewById(R.id.imagebygallery);
            mtakegallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showFileChooser();

                }
            });
            mbuilder.setView(mview);
             dialog = mbuilder.create();
            dialog.show();
        }

        if(v == muploadnewsbtn){
            uploadImage();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            Log.d("try4",str);
            super.onActivityResult(requestCode, resultCode, data);}catch (Exception e) {
            Log.d("try8", e.toString());
            Toast.makeText(getContext(), "On super " + e.toString(), Toast.LENGTH_LONG).show();

        }


//
//        if (requestCode >= PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//
//            Uri filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
//                mnewsimage.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//        }

        if(requestCode==PICK_IMAGE_REQUEST) {

            if(data==null){

                Toast.makeText(getContext()," Please Select Image For Uploading.... ",Toast.LENGTH_LONG).show();

            }else {
                Uri filePath = data.getData();
                Intent intentcrop = new Intent(getContext(), CropImage.class);
                intentcrop.putExtra("ramji", filePath.toString());
                startActivityForResult(intentcrop, PICK_CROPIMAGE);
            }
        }


//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            bitmap = (Bitmap) data.getExtras().get("data");
//            mnewsimage.setImageBitmap(bitmap);
//        }

        if(requestCode ==CAMERA_REQUEST ){
            if(data.getExtras()==null){

                Toast.makeText(getContext()," Please Take Image For Uploading.... ",Toast.LENGTH_LONG).show();

            }else {
                Bitmap bitmapcamear = (Bitmap) data.getExtras().get("data");
                String bitstring = getStringImage(bitmapcamear);
                boolean checktr = true;
                Intent intentcrop = new Intent(getContext(), CropImage.class);
                intentcrop.putExtra("cameraji", bitstring);
                intentcrop.putExtra("camerajiboolean", checktr);
                startActivityForResult(intentcrop, PICK_CROPIMAGE);
            }
        }

        if(requestCode==PICK_CROPIMAGE)
        {
            strtim = data.getStringExtra("cropimage");
            Log.d("imageindash","imageindd "+strtim);
            bitmap = StringToBitMap(strtim);
            Log.d("imageinbitmap","imageinbit "+bitmap);
            mnewsimage.setImageBitmap(bitmap);
            dialog.dismiss();

        }

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position < 1){
            TextView errorText = (TextView)mnewstype.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Choose A News Type");//changes the selected item text to this

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
