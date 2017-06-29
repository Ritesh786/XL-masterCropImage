package com.extralarge.fujitsu.xl;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.extralarge.fujitsu.xl.ReporterSection.AppController;
import com.extralarge.fujitsu.xl.ReporterSection.Movie;
import com.extralarge.fujitsu.xl.ReporterSection.PendingNews;
import com.extralarge.fujitsu.xl.ReporterSection.RecycleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    ImageView mbackimage, msearchimage;
    EditText msearchedit;

    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    // private ListView listView;

    private RecyclerView recyclerView;

    private RecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mbackimage = (ImageView) findViewById(R.id.back_imagesearch);
        msearchimage = (ImageView) findViewById(R.id.search_image);

        msearchedit = (EditText) findViewById(R.id.search_edittxt);

        mbackimage.setOnClickListener(this);
        msearchimage.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        adapter = new RecycleAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.back_imagesearch:

                SearchActivity.this.finish();

                break;

            case R.id.search_image:

                movieList.clear();
                searchbar();

                break;

        }
    }




    public void searchbar() {

        pDialog = new ProgressDialog(SearchActivity.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

       final String  strtext = msearchedit.getText().toString().trim();

        final String url = "http://api.minews.in/slimapp/public/api/posts/search/";

        String newurl = url+strtext;

        JsonArrayRequest movieReq = new JsonArrayRequest(newurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("rest101", strtext);
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Movie movie = new Movie();

                                String imagestr = obj.getString("image");
                                String imagrurl = "http://minews.in/storage/app/public/uploads/";
                                String imageurlfull = imagrurl+imagestr;

                                movie.setTitle(obj.getString("headline"));
                                movie.setThumbnailUrl(imageurlfull);
                                movie.setRating(obj.getString("content"));

                                movie.setYear(obj.getString("category"));
                                movie.setGenre(obj.getString("created_at"));

                                movieList.add(movie);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errr120", "Error: " + error.toString());
                Log.d("rest102", strtext);
                Toast.makeText(SearchActivity.this,error.toString(),Toast.LENGTH_LONG).show();

                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    }


