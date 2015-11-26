package net.ramonsilva.nanodegree.moovie.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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
    private ProgressDialog dialog;
    private boolean has_error = false;

    public MovieFetcher(Context context){
        super();
        this.context = context;
        this.dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage(context.getResources().getString(R.string.please_wait));
        this.dialog.show();
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
                    .appendEncodedPath(movieId.toString())
                    .appendQueryParameter("api_key", apiKey)
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

            movieJsonStr = buffer.toString();

            Movie movie = MovieUtil.getMovie(movieJsonStr);
            has_error = false;
            return movie;

        } catch (Exception ex){
            Log.e(LOG_TAG, "Não foi possivel obter o filme " + ex.getMessage());
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
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if(has_error){
            showAlertMessage();
        }

        try {
            ((MovieDetailsActivity) this.context).refreshUI(movie);
        } catch (Exception ex){
            Log.e(LOG_TAG, "Erro ao atuakizar a UI");
            ex.printStackTrace();
        }
    }
}
