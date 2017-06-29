package com.extralarge.fujitsu.xl.ReporterSection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.extralarge.fujitsu.xl.R;

import java.util.List;

/**
 * Created by Fujitsu on 22/05/2017.
 */

public class CustomListAdapter  extends BaseAdapter {
private Context activity;
private LayoutInflater inflater;
private List<Movie> movieItems;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();



public CustomListAdapter(Context context ,List<Movie> movieItems) {

        this.activity = context;
        this.movieItems = movieItems;

        }


@Override
public int getCount() {
        return movieItems.size();
        }

@Override
public Object getItem(int location) {
        return movieItems.get(location);
        }

@Override
public long getItemId(int position) {
        return position;
        }



static class ViewHolder {
    NetworkImageView thumbNail;
    TextView title;
    // TextView rating;
    TextView genre;
    TextView year;
}


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.rist_row, null);
            holder = new ViewHolder();

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            holder.thumbNail = (NetworkImageView) convertView
                    .findViewById(R.id.thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            // holder.rating = (TextView) convertView.findViewById(R.id.rating);
            holder.genre = (TextView) convertView.findViewById(R.id.genre);
            holder.year = (TextView) convertView.findViewById(R.id.releaseYear);

            // getting movie data for the row
            Movie m = movieItems.get(position);

            // thumbnail image
            // thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

            if(!URLUtil.isValidUrl(movieItems.get(position).getThumbnailUrl()))
            {
                holder.thumbNail.setVisibility(View.GONE);
            }
            else
            {
                holder.thumbNail.setVisibility(View.VISIBLE);//add this
                holder.thumbNail.setImageUrl(movieItems.get(position).getThumbnailUrl(), imageLoader);
                String str = "rjbhai";
                Log.d("rj123",str);
            }

            // title
            holder.title.setText(m.getTitle());

            // rating
            // holder.rating.setText("News Type: " + String.valueOf(m.getRating()));

            // genre
            holder.genre.setText(m.getGenre());

            // release year
            holder.year.setText(String.valueOf(m.getYear()));
            convertView.setTag(holder);
        }
        else {

            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }


}

