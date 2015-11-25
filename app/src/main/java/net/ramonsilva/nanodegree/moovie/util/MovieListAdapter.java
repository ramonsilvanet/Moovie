package net.ramonsilva.nanodegree.moovie.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.ramonsilva.nanodegree.moovie.R;
import net.ramonsilva.nanodegree.moovie.model.Movie;

import java.util.ArrayList;

/**
 * Created by Rfsilva on 25/11/2015.
 */
public class MovieListAdapter extends ArrayAdapter<Movie> {
    private Context context;
    private ArrayList<Movie> movies;
    private LayoutInflater inflater;

    private final String LOG_TAG = MovieListAdapter.class.getSimpleName();

    public MovieListAdapter(Context context, ArrayList<Movie> movies) {
        super(context, R.layout.listview_item_image, movies);

        this.context = context;
        this.movies = movies;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public Movie getItem(int position) {
        return this.movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.listview_item_image, parent, false);
        }

        Picasso
                .with(context)
                .load(movie.getPosterUrl())
                .fit()
                .into((ImageView) convertView);

        return convertView;
    }
}
