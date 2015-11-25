package net.ramonsilva.nanodegree.moovie.util;

import net.ramonsilva.nanodegree.moovie.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rfsilva on 25/11/2015.
 */
public class MovieUtil {
    private final static String BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String TAMANHO_POSTER = "w500";


    public static ArrayList<Movie> getMovies(String moviesJsonStr) throws JSONException {

        JSONObject result = new JSONObject(moviesJsonStr);
        JSONArray movies = result.getJSONArray("results");

        ArrayList<Movie> moviesList = new ArrayList<>();

        for(int i = 0; i < movies.length(); i++){
            JSONObject movie = movies.getJSONObject(i);
            final Movie m = getMovie(movie);
            moviesList.add(m);
        }

        return moviesList;
    }

    public static Movie getMovie(String moviesJsonStr) throws JSONException{
        JSONObject result = new JSONObject(moviesJsonStr);
        return getMovie(result);
    }

    private static Movie getMovie(JSONObject json) throws JSONException{
        final Movie m = new Movie();
        m.setId(json.getInt("id"));
        m.setPosterUrl(BASE_URL + TAMANHO_POSTER + json.getString("poster_path"));
        m.setTitle(json.getString("title"));
        m.setReleaseDate(json.getString("release_date"));
        m.setRating(json.getDouble("vote_average"));

        m.setOverview(json.getString("overview"));

        if(json.has("runtime")){
            m.setDuration(json.getString("runtime"));
        }

        return m;
    }



}
