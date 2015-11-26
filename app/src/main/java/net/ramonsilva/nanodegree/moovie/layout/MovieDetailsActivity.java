package net.ramonsilva.nanodegree.moovie.layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ramonsilva.nanodegree.moovie.R;
import net.ramonsilva.nanodegree.moovie.model.Movie;
import net.ramonsilva.nanodegree.moovie.util.MovieFetcher;

import java.text.ParseException;

public class MovieDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        this.id =  intent.getLongExtra(Intent.EXTRA_TEXT, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Long[] params = new Long[1];
        params[0] = this.id;
        carregarFicha(params);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void carregarFicha(Long[] params){
        MovieFetcher moviesFetcher = new MovieFetcher(MovieDetailsActivity.this);
        moviesFetcher.execute(params);
    }

    public void refreshUI(Movie movie) throws ParseException {

        ImageView imageView = (ImageView) findViewById(R.id.imageViewPoster);
        Picasso.with(MovieDetailsActivity.this)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_cartaz_placeholder)
                .resize(500,750)
                .centerInside()
                .into(imageView);

        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(movie.getTitle());

        String[] data = movie.getReleaseDate().split("-");
        TextView textViewYear = (TextView) findViewById(R.id.textViewYear);
        textViewYear.setText(data[0]);

        TextView textViewDuration = (TextView) findViewById(R.id.textViewDuration);
        if("null".equalsIgnoreCase(movie.getDuration())){
            textViewDuration.setVisibility(View.GONE);
        } else {
            textViewDuration.setText(movie.getDuration() + " min");
        }

        TextView textViewRating = (TextView) findViewById(R.id.textViewRating);
        textViewRating.setText(movie.getRating() + "/10");


        TextView textViewDescrition = (TextView) findViewById(R.id.textViewDescrition);

        if("null".equalsIgnoreCase(movie.getOverview())){
            textViewDescrition.setVisibility(View.GONE);
        } else {
            textViewDescrition.setText(movie.getOverview());
        }

        TextView textViewOriginal = (TextView) findViewById(R.id.textOriginalTitle);
        textViewOriginal.setText("(" + movie.getOriginalTitle() + ")");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
