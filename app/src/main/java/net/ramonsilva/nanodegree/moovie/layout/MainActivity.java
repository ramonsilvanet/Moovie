package net.ramonsilva.nanodegree.moovie.layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.ramonsilva.nanodegree.moovie.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {


    private ImageListAdapter mImageListAdapeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ArrayList<String> movies = new ArrayList<String>();

        GridView gridView = (GridView) findViewById(R.id.usage_example_gridview);
        mImageListAdapeter = new ImageListAdapter(MainActivity.this, movies);

        gridView.setAdapter(mImageListAdapeter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Details.class);
                //intent.putExtra(Intent.EXTRA_TEXT, weekForecasts.get(position));
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
        MoviesFetcher weatherTask = new MoviesFetcher();
        weatherTask.execute();
    }

    public class ImageListAdapter extends ArrayAdapter {

        private Context context;
        private ArrayList<String> imageUrls;
        private LayoutInflater inflater;

        public ImageListAdapter(Context context, ArrayList<String> imageUrls) {
            super(context, R.layout.listview_item_image, imageUrls);

            this.context = context;
            this.imageUrls = imageUrls;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.listview_item_image, parent, false);
            }

            Picasso
                    .with(context)
                    .load(imageUrls.get(position))
                    .fit()
                    .into((ImageView) convertView);

            return convertView;
        }
    }

    public String[] getMoviePosters(String moviesJsonStr) throws JSONException {

        final String baseUrl = "http://image.tmdb.org/t/p/";
        final String tamanho = "w185";

        JSONObject result = new JSONObject(moviesJsonStr);
        JSONArray movies = result.getJSONArray("results");

        String[] posters = new String[movies.length()];

        for(int i = 0; i < movies.length(); i++){
            JSONObject movie = movies.getJSONObject(i);
            posters[i] =  baseUrl + tamanho + movie.getString("poster_path");
        }

        return posters;
    }


    public class MoviesFetcher extends AsyncTask<Void, Void, String[]>{
        private final String LOG_TAG = MoviesFetcher.class.getSimpleName();

        @Override
        protected String[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try {
                final String baseUrl = "http://api.themoviedb.org/3/discover/movie";
                final String apiKey = getString(R.string.APIKEY);


                final Uri uri = Uri.parse(baseUrl)
                        .buildUpon()
                        .appendQueryParameter("api_key", apiKey)
                        .appendQueryParameter("sort_by", "popularity.desc")
                        .appendQueryParameter("include_image_language", "pt,en")
                        .appendQueryParameter("language", "pt")
                        .build();

                URL url = new URL(uri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                moviesJsonStr = buffer.toString();

                String[] moviesPosters = getMoviePosters(moviesJsonStr);

                return moviesPosters;

            } catch (Exception ex){
                Log.e(LOG_TAG, "Não foi possivel obter os filmes", ex);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Problemas ao fechar o Stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
                if(result != null){
                    mImageListAdapeter.clear();
                    for(String movie : result){
                        mImageListAdapeter.add(movie);
                    }
                }
        }
    }
}
