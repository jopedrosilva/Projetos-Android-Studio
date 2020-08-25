package com.withconnection.youtubeapiv3.models;

import android.os.Parcel;
import android.os.Parcelable;

public class YoutubeDataModel implements Parcelable {
    private String title = "";
    private String description = "";
    private String publishedAt = "";
    private String thumbnail = "";
    private String video_id = "";

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public YoutubeDataModel() {
        super();
    }


    protected YoutubeDataModel(Parcel in) {
        this();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.publishedAt = in.readString();
        this.thumbnail = in.readString();
        this.video_id = in.readString();

    }

    public static final Parcelable.Creator<YoutubeDataModel> CREATOR = new Parcelable.Creator<YoutubeDataModel>() {
        @Override
        public YoutubeDataModel createFromParcel(Parcel in) {
            return new YoutubeDataModel(in);
        }

        @Override
        public YoutubeDataModel[] newArray(int size) {
            return new YoutubeDataModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
