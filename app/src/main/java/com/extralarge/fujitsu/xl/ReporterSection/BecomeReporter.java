package com.extralarge.fujitsu.xl.ReporterSection;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.extralarge.fujitsu.xl.AbsRuntimePermission;
import com.extralarge.fujitsu.xl.R;
import com.extralarge.fujitsu.xl.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BecomeReporter extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

        EditText  mpassword, mname, memail, mmobile, mpincode;
        Button mbtnregister;

        String   password="123456789", name, email, mobile, city, district, state, landmark, pincode,gender;
        String usertype= "individual";
        String  usernsme;

        AutoCompleteTextView   mcity, mdistrict, mstate,mgender;

        AlertDialog.Builder builder;
        ArrayAdapter<CharSequence> adapter;

        private static final String LOG_TAG = "Google Places Autocomplete";
        private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
        private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
        private static final String OUT_JSON = "/json";
        private static final String API_KEY = "AIzaSyD7_gkB6R8Tn2SVAgis-rrYJnB2KZtWbbQ";


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.becomereporter);




        mmobile = (EditText) findViewById(R.id.reg_mobile);

        mname = (EditText) findViewById(R.id.reg_name);
        memail = (EditText) findViewById(R.id.reg_email);

        mcity = (AutoCompleteTextView) findViewById(R.id.reg_city);
        mdistrict = (AutoCompleteTextView) findViewById(R.id.reg_area);
        mstate = (AutoCompleteTextView) findViewById(R.id.reg_state);
        mpincode = (EditText) findViewById(R.id.reg_pincode);
        mgender =(AutoCompleteTextView) findViewById(R.id.reg_gender);

        builder = new AlertDialog.Builder(getApplicationContext());

        String[] countries = getResources().getStringArray(R.array.CityNames);
        String[] genders = getResources().getStringArray(R.array.gender);

        ArrayAdapter<String> gendadapter =
                new ArrayAdapter<String>(getApplicationContext(), R.layout.autocomplete, genders);

        mgender.setAdapter(gendadapter);

        mcity.setAdapter(new GooglePlacesAutocompleteAdapter(BecomeReporter.this, R.layout.autocomplete));
        mstate.setAdapter(new GooglePlacesAutocompleteAdapter(BecomeReporter.this, R.layout.autocomplete));
        mdistrict.setAdapter(new GooglePlacesAutocompleteAdapter(BecomeReporter.this, R.layout.autocomplete));
        mcity.setOnItemClickListener(this);
        mstate.setOnItemClickListener(this);
        mdistrict.setOnItemClickListener(this);

        mbtnregister = (Button) findViewById(R.id.btn_Register);
        mbtnregister.setOnClickListener(this);



        }

        public boolean isValidPhoneNumber(String phoneNumber) {

        String expression ="^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";
        CharSequence inputString = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
        return true;
        }
        else{
        return false;
        }

        }



@Override
public void onClick(View v) {

        registerUser();
        }

private void registerUser() {


        final String KEY_mobile = "mobile";
        final String KEY_name = "username";
        final String KEY_email = "email";
        final String KEY_gender = "gender";
        final String KEY_state = "state";
        final String KEY_city = "city";
        final String KEY_district = "district";
        final String KEY_pincode = "pincode";




        mobile = mmobile.getText().toString().trim();
        name = mname.getText().toString().trim();
        email = memail.getText().toString().trim();
        gender = mgender.getText().toString().trim();
        state = mstate.getText().toString().trim();
        city = mcity.getText().toString().trim();
        district = mdistrict.getText().toString().trim();
        pincode = mpincode.getText().toString().trim();


            if(TextUtils.isEmpty(name)){
                    mname.requestFocus();
                    mname.setError("This Field Is Mandatory");
           } else if (TextUtils.isEmpty(password)) {
        mpassword.requestFocus();
        mpassword.setError("This Field Is Mandatory");
        } else if (TextUtils.isEmpty(name)) {
        mname.requestFocus();
        mname.setError("This Field Is Mandatory");
        } else if (TextUtils.isEmpty(city)) {
        mcity.requestFocus();
        mcity.setError("This Field Is Mandatory");
        }
         else if (TextUtils.isEmpty(state)) {
        mstate.requestFocus();
        mstate.setError("This Field Is Mandatory");
        }
         else if (TextUtils.isEmpty(pincode)) {
        mpincode.requestFocus();
        mpincode.setError("This Field Is Mandatory");
        }
        else if (pincode.length()<6) {
        mpincode.requestFocus();
        mpincode.setError("Please Fill Correct Pincode");
        }

        else {
        String url = null;
             String REGISTER_URL = "http://api.minews.in/user_reg.php";

        REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
        try {
        URL sourceUrl = new URL(REGISTER_URL);
        url = sourceUrl.toString();
        } catch (MalformedURLException e) {
        e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
        new Response.Listener<String>() {
@Override
public void onResponse(String response) {

        try {
        JSONObject jsonresponse = new JSONObject(response);
        boolean success = jsonresponse.getBoolean("success");

        if (success) {

        Intent registerintent = new Intent(BecomeReporter.this, ReporterLogin.class);
        startActivity(registerintent);
        } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(BecomeReporter.this);
        builder.setMessage("Registration Failed")
        .setNegativeButton("Retry", null)
        .create()
        .show();

        }

        } catch (JSONException e) {
        e.printStackTrace();
        }

        Toast.makeText(BecomeReporter.this, response.toString(), Toast.LENGTH_LONG).show();
        }
        },
        new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {

        Toast.makeText(BecomeReporter.this, error.toString(), Toast.LENGTH_LONG).show();

        }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put(KEY_mobile, mobile);
                params.put(KEY_name, name);
                params.put(KEY_email, email);
                params.put(KEY_gender,gender);
                params.put(KEY_state, state);
                params.put(KEY_city, city);
                params.put(KEY_district, district);
                params.put(KEY_pincode, pincode);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(BecomeReporter.this);
        requestQueue.add(stringRequest);
        }
        }


        public static ArrayList autocomplete(String input) {

                Log.d("chala00","chal");
                ArrayList resultList = null;
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                        sb.append("?key=" + API_KEY);
                        sb.append("&components=country:in");
                        sb.append("&input=" + URLEncoder.encode(input, "utf8"));
                        URL url = new URL(sb.toString());
                        conn = (HttpURLConnection) url.openConnection();
                        InputStreamReader in = new InputStreamReader(conn.getInputStream());

                        int read;
                        char[] buff = new char[1024];
                        while ((read = in.read(buff)) != -1) {
                                jsonResults.append(buff, 0, read);
                        }
                } catch (MalformedURLException e) {

                        return resultList;

                } catch (IOException e) {

                        return resultList;
                } finally {
                        if (conn != null) {

                                conn.disconnect();
                        }
                }
                try {

                        JSONObject jsonObj = new JSONObject(jsonResults.toString());
                        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                        resultList = new ArrayList(predsJsonArray.length());
                        Log.d("arrjs00", String.valueOf(predsJsonArray));
                        for (int i = 0; i < predsJsonArray.length(); i++) {
                                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                                System.out.println("============================================================");
                                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                        }
                } catch(JSONException e){

                }
                return resultList;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }

        class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements android.widget.Filterable {
                private ArrayList resultList;
                public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
                        super(context, textViewResourceId);
                }
                @Override
                public int getCount() {
                        return resultList.size();
                }
                @Override
                public String getItem(int index) {
                        return resultList.get(index).toString();
                }
                @Override
                public android.widget.Filter getFilter() {
                        android.widget.Filter filter = new android.widget.Filter() {
                                @Override
                                protected FilterResults performFiltering(CharSequence constraint) {
                                        FilterResults filterResults = new FilterResults();
                                        if (constraint != null) {

                                                resultList = autocomplete(constraint.toString());

                                                filterResults.values = resultList;
                                                filterResults.count = resultList.size();
                                        }
                                        return filterResults;
                                }
                                @Override
                                protected void publishResults(CharSequence constraint, FilterResults results) {
                                        if (results != null && results.count > 0) {
                                                notifyDataSetChanged();
                                        } else {
                                                notifyDataSetInvalidated();
                                        }
                                }
                        };
                        return filter;
                }
        }

}