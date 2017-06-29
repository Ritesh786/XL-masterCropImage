package com.extralarge.fujitsu.xl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.extralarge.fujitsu.xl.FCM.MyFirebaseInstanceIDService;
import com.extralarge.fujitsu.xl.FCM.tokensave;
import com.extralarge.fujitsu.xl.ReporterSection.BecomeReporter;
import com.extralarge.fujitsu.xl.ReporterSection.ReporterDashboard;
import com.extralarge.fujitsu.xl.ReporterSection.ReporterLogin;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    android.support.v7.widget.Toolbar toolbar;
    private BroadcastReceiver broadcastReceiver;
    String token;

    String name;

    UserSessionManager session;
    int i = 0;
    static boolean f = true;
  //  SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        session = new UserSessionManager(getApplicationContext());
        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationview);


        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */

        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

        ImageView msearch = (ImageView) toolbar.findViewById(R.id.searchimage);
        msearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent searchint = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(searchint);
            }
        });


        Intent intent = getIntent();
         name = intent.getStringExtra("session");

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nav_item_home) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                    toolbar.setTitle("EXCEL");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                }
//
//                if (menuItem.getItemId() == R.id.nav_item_national) {
//                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
//                    xfragmentTransaction.replace(R.id.containerView, new MainNews()).commit();
//                    toolbar.setTitle("राष्ट्रीय");
//                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
//
//                }
//
//                if (menuItem.getItemId() == R.id.nav_item_International) {
//                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
//                    xfragmentTransaction.replace(R.id.containerView, new State()).commit();
//                    toolbar.setTitle("International");
//                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
//
//                }
//
//                if (menuItem.getItemId() == R.id.nav_item_states) {
//                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
//                    xfragmentTransaction.replace(R.id.containerView, new State()).commit();
//                    toolbar.setTitle("States");
//                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
//
//                }
//                if (menuItem.getItemId() == R.id.nav_item_business) {
//                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
//                    xfragmentTransaction.replace(R.id.containerView, new State()).commit();
//                    toolbar.setTitle("Business");
//                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
//
//                }
//
//                if (menuItem.getItemId() == R.id.nav_item_city) {
//                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
//                    xfragmentTransaction.replace(R.id.containerView, new State()).commit();
//                    toolbar.setTitle("Cities");
//                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
//
//                }


                if (menuItem.getItemId() == R.id.nav_item_becomereporter) {

                    Intent becomereporterint = new Intent(MainActivity.this, BecomeReporter.class);
                    startActivity(becomereporterint);
                }

                if (menuItem.getItemId() == R.id.nav_item_reporterlogin) {

                   // session.createUserLoginSession(name);

                    Intent reporterloginint = new Intent(MainActivity.this, ReporterLogin.class);
//                    reporterloginint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    reporterloginint.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(reporterloginint);
                }

                if (menuItem.getItemId() == R.id.nav_item_uploadnews) {

                    Intent dashboardintent = new Intent(MainActivity.this, ReporterDashboard.class);
                    startActivity(dashboardintent);
                    finish();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */


        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                token = tokensave.getInstance(MainActivity.this).getDeviceToken();
                Log.d("tok00",token);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                if(!prefs.getBoolean("firstTime", false)) {

                    SendtokenofNews();

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstTime", true);
                    editor.commit();
                }

            }
        };

        registerReceiver(broadcastReceiver,new IntentFilter(MyFirebaseInstanceIDService.TOKEN_BROADCAST));

        }


    public void SendtokenofNews() {
        
        final String LOGIN_URL = "http://api.minews.in/slimapp/public/api/readers/";
        String newurl = LOGIN_URL+token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, newurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("jaba",response.toString());
//
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");

                            if(success){
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //   Log.d("jabadi",motptext);
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }) {

        };RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }


    }
