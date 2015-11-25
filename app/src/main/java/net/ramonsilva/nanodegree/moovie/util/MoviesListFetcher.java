package net.ramonsilva.nanodegree.moovie.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import net.ramonsilva.nanodegree.moovie.R;
import net.ramonsilva.nanodegree.moovie.model.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Rfsilva on 25/11/2015.
 */
public class MoviesListFetcher extends AsyncTask<Void, Void, ArrayList<Movie>> {
    private final String LOG_TAG = MoviesListFetcher.class.getSimpleName();

    private Context context;
    private ArrayAdapter adapter;

    public MoviesListFetcher(Context context, ArrayAdapter adapter){
        super();
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
            final String baseUrl = "http://api.themoviedb.org/3/discover/movie";
            final String apiKey = context.getResources().getString(R.string.APIKEY);


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

            ArrayList<Movie> movies = MovieUtil.getMovies(moviesJsonStr);

            return movies;

        } catch (Exception ex){
            Log.e(LOG_TAG, "Não foi possivel obter os filmes " + ex.getMessage());
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
    protected void onPostExecute(ArrayList<Movie> result) {
        if(result != null){
            adapter.clear();
            for(Movie movie : result){
                adapter.add(movie);
            }
        }
    }
}
