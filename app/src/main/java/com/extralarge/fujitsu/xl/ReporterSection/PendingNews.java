package com.extralarge.fujitsu.xl.ReporterSection;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.extralarge.fujitsu.xl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingNews extends Fragment {

    private static final String TAG = PendingNews.class.getSimpleName();

    // Movies json url

    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    // private ListView listView;

    private RecyclerView recyclerView;

    private RecycleAdapter adapter;
    int strtext;

    public PendingNews() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_status, container, false);

       // listView = (ListView) view.findViewById(R.id.list);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
//        adapter = new CustomListAdapter(getContext(), movieList);
       // listView.setAdapter(adapter);
        adapter = new RecycleAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
       recyclerView.setItemAnimator(new DefaultItemAnimator());

      // recyclerView.getItemAnimator().setChangeDuration(0);

       // adapter.getItemAnimator().setSupportsChangeAnimations(false);

        pDialog = new ProgressDialog(getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
//        getActionBar().setBackgroundDrawable(
//                new ColorDrawable(Color.parseColor("#1b1b1b")));

        // Creating volley request obj
        populatedat();


        return  view;
    }

    public void populatedat(){

        Bundle bundle = this.getArguments();
        strtext = bundle.getInt("message", 0);
        Log.d("idv", String.valueOf(strtext));

        final String url = "http://api.minews.in/slimapp/public/api/posts/user/pending/";

        String newurl = url+strtext;

        JsonArrayRequest movieReq = new JsonArrayRequest(newurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
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

                                // Genre is json array  (Number) obj.get("type")
//                                JSONArray genreArry = obj.getJSONArray("created");
//                                ArrayList<String> genre = new ArrayList<String>();
//                                for (int j = 0; j < genreArry.length(); j++) {
//                                    genre.add((String) genreArry.get(j));
//                                }
//                                movie.setGenre(genre);

                                // adding movie to movies array
                                movieList.add(movie);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                        //  adapter.setHasStableIds(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

        if(movieList!=null) movieList.clear();

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

