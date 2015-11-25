package net.ramonsilva.nanodegree.moovie.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import net.ramonsilva.nanodegree.moovie.R;
import net.ramonsilva.nanodegree.moovie.layout.MovieDetailsActivity;
import net.ramonsilva.nanodegree.moovie.model.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rfsilva on 25/11/2015.
 */
public class MovieFetcher extends AsyncTask<Long, Void, Movie> {

    private final String LOG_TAG = MovieFetcher.class.getSimpleName();
    private Context context;

    public MovieFetcher(Context context){
        super();
        this.context = context;
    }

    @Override
    protected Movie doInBackground(Long... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        Long movieId = params[0];

        try {
            final String baseUrl = "http://api.themoviedb.org/3/movie";
            final String apiKey = context.getResources().getString(R.string.APIKEY);


            final Uri uri = Uri.parse(baseUrl)
                    .buildUpon()
                    .appendEncodedPath(movieId.toString())
                    .appendQueryParameter("api_key", apiKey)
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

            movieJsonStr = buffer.toString();

            Movie movie = MovieUtil.getMovie(movieJsonStr);

            return movie;

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
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        try {
            ((MovieDetailsActivity) this.context).refreshUI(movie);
        } catch (Exception ex){
            Log.e(LOG_TAG, "Erro ao atuakizar a UI");
            ex.printStackTrace();
        }
    }
}
