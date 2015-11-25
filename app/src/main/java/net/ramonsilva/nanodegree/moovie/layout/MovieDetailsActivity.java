package net.ramonsilva.nanodegree.moovie.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ramonsilva.nanodegree.moovie.R;
import net.ramonsilva.nanodegree.moovie.model.Movie;
import net.ramonsilva.nanodegree.moovie.util.MovieFetcher;

import java.text.ParseException;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Long id =  intent.getLongExtra(Intent.EXTRA_TEXT, 0);

        Long[] params = new Long[1];
        params[0] = id;
        carregarFicha(params);
    }




    private void carregarFicha(Long[] params){
        MovieFetcher moviesFetcher = new MovieFetcher(MovieDetailsActivity.this);
        moviesFetcher.execute(params);
    }

    public void refreshUI(Movie movie) throws ParseException {

        ImageView imageView = (ImageView) findViewById(R.id.imageViewPoster);
        Picasso.with(MovieDetailsActivity.this)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_cartaz_placeholder)
                .error(R.drawable.ic_cartaz_placeholder)
                .into(imageView);

        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(movie.getTitle());

        String[] data = movie.getReleaseDate().split("-");
        TextView textViewYear = (TextView) findViewById(R.id.textViewYear);
        textViewYear.setText(data[0]);

        TextView textViewDuration = (TextView) findViewById(R.id.textViewDuration);
        textViewDuration.setText(movie.getDuration()  + " min");

        TextView textViewRating = (TextView) findViewById(R.id.textViewRating);
        textViewRating.setText(movie.getRating() + "/10" );

        TextView textViewDescrition = (TextView) findViewById(R.id.textViewDescrition);
        textViewDescrition.setText(movie.getOverview());

    }

}
