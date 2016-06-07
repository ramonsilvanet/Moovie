package net.ramonsilva.nanodegree.moovie.model;

/**
 * Created by Rfsilva on 25/11/2015.
 */
public class Movie {

    private String posterUrl;
    private int id;
    private String title;
    private String releaseDate;
    private String duration;
    private double rating;
    private String overview;
    private String originalTitle;
    private String genre;


    public String getPosterUrl(){
        return this.posterUrl;
    }

    public void setPosterUrl(String posterUrl){
        this.posterUrl = posterUrl;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public String getGenre(){
        return genre;
    }
}
