package net.ramonsilva.nanodegree.moovie.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import net.ramonsilva.nanodegree.moovie.R;
import net.ramonsilva.nanodegree.moovie.model.Movie;
import net.ramonsilva.nanodegree.moovie.util.MovieListAdapter;
import net.ramonsilva.nanodegree.moovie.util.MoviesListFetcher;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MovieListAdapter mImageListAdapeter;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ArrayList<Movie> movies = new ArrayList<Movie>();

        GridView gridView = (GridView) findViewById(R.id.usage_example_gridview);
        mImageListAdapeter = new MovieListAdapter(MainActivity.this, movies);

        gridView.setAdapter(mImageListAdapeter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies(){
        MoviesListFetcher moviesFetcher = new MoviesListFetcher(MainActivity.this, mImageListAdapeter);
        moviesFetcher.execute();
    }
}
