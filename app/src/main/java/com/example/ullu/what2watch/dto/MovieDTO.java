package com.example.ullu.what2watch.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Ullu on 25.10.2015.
 */
public class MovieDTO implements Parcelable {

    private String originalTitle;

    private String imageUrl;

    private String plotSynopsis;

    private double userRating;

    private double popularity;

    private int votes;

    private String releaseDate;

    public MovieDTO(){};

    private MovieDTO(Parcel in) {
        this.originalTitle = in.readString();
        this.imageUrl = in.readString();
        this.plotSynopsis = in.readString();
        this.userRating = in.readDouble();
        this.popularity = in.readDouble();
        this.votes = in.readInt();
        this.releaseDate = in.readString();
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    public int getVotes() {
        return votes;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(imageUrl);
        dest.writeString(plotSynopsis);
        dest.writeDouble(userRating);
        dest.writeDouble(popularity);
        dest.writeInt(votes);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<MovieDTO> CREATOR
            = new Parcelable.Creator<MovieDTO>() {
        public MovieDTO createFromParcel(Parcel in) {
            return new MovieDTO(in);
        }

        public MovieDTO[] newArray(int size) {
            return new MovieDTO[size];
        }
    };

}
