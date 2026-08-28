package model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Movie {
    private int id;
    private String title;

    @SerializedName("original_title")
    private String originalTitle;

    private String overview;
    private List<Genre> genres;
    private List<Keyword> keywords;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("release_date")
    private String releaseDate;

    private Integer runtime;

    @SerializedName("poster_url")
    private String posterUrl;

    private double popularity;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private int voteCount;

    private long budget;
    private long revenue;
    private String status;
    private String tagline;

    @SerializedName("production_companies")
    private List<ProductionCompany> productionCompanies;

    @SerializedName("production_countries")
    private List<ProductionCountry> productionCountries;

    @SerializedName("spoken_languages")
    private List<SpokenLanguage> spokenLanguages;

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public long getBudget() {
        return budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", rating=" + voteAverage +
                ", voteCount=" + voteCount +
                ", popularity=" + popularity +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }
}