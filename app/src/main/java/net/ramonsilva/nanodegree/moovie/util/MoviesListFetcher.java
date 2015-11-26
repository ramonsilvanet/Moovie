package net.ramonsilva.nanodegree.moovie.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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
    private ProgressDialog dialog;
    private boolean has_error = false;

    public MoviesListFetcher(Context context, ArrayAdapter adapter){
        super();
        this.context = context;
        this.adapter = adapter;
        this.dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage(context.getResources().getString(R.string.please_wait));
        this.dialog.show();
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
            final String baseUrl = "http://api.themoviedb.org/3/discover/movie";
            final String apiKey = context.getResources().getString(R.string.APIKEY);

            final SharedPreferences sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(context);
            final String sort_by = sharedPrefs.getString(context.getResources().getString(R.string.pref_sort_key), context.getResources().getString(R.string.pref_sort_popularity));

            final String english = context.getResources().getString(R.string.pref_lang_en);
            final String language = sharedPrefs.getString(context.getResources().getString(R.string.pref_lang_key), english);
            String posterLanguage;

            if(english.equals(language)){
                posterLanguage = language;
            } else {
                posterLanguage = language + ",en,null";
            }

            final Uri uri = Uri.parse(baseUrl)
                    .buildUpon()
                    .appendQueryParameter("api_key", apiKey)
                    .appendQueryParameter("sort_by", sort_by)
                    .appendQueryParameter("include_image_language", posterLanguage)
                    .appendQueryParameter("language", language)
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
            has_error = false;
            return movies;

        } catch (Exception ex){
            Log.e(LOG_TAG, "Não foi possivel obter os filmes " + ex.getMessage());
            ex.printStackTrace();
            has_error = true;
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

    private void showAlertMessage(){
        CharSequence text = context.getResources().getString(R.string.network_error_message);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if(has_error){
            showAlertMessage();
        }

        if(result != null){
            adapter.clear();
            for(Movie movie : result){
                adapter.add(movie);
            }
        }
    }
}
