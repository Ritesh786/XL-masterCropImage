package com.extralarge.fujitsu.xl.ReporterSection;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.extralarge.fujitsu.xl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fujitsu on 27/05/2017.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {


    private  List<Movie> movieItems;
    Context context;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre,rating;
        NetworkImageView thumbNail;

        public MyViewHolder(View view) {
            super(view);
            thumbNail = (NetworkImageView) view
                    .findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);
          //  rating = (TextView) view.findViewById(R.id.rating);
            genre = (TextView) view.findViewById(R.id.genre);
           year = (TextView) view.findViewById(R.id.releaseYear);
        }
    }


    public RecycleAdapter(List<Movie> moviesList) {

        this.movieItems = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Movie movie = movieItems.get(position);

        imageLoader.get(movie.getThumbnailUrl(), ImageLoader.getImageListener(holder.thumbNail, R.mipmap.ic_dainik, android.R.drawable.ic_dialog_alert));

        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
      // holder.thumbNail.setImageUrl(movieItems.get(position).getThumbnailUrl(), imageLoader);
      //  holder.rating.setText(""+movie.getRating());
       // Picasso.with(context).load(movieItems.get(position).getThumbnailUrl()).into(holder.thumbNail);

        if(!URLUtil.isValidUrl(movieItems.get(position).getThumbnailUrl()))
        {
            holder.thumbNail.setVisibility(View.GONE);
        }
        else
        {
            holder.thumbNail.setVisibility(View.VISIBLE);//add this
            try {
                holder.thumbNail.setImageUrl(movieItems.get(position).getThumbnailUrl(), imageLoader);
            }catch (Exception e){

                Toast.makeText(context,"On Activity "+e.toString(),Toast.LENGTH_LONG).show();
            }
        }


    }


    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getItemCount() {

        return movieItems.size();
    }


}

