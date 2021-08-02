package com.example.mainactivity.retrofit;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mainactivity.database.PostDatabase;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Post implements Parcelable {

    public static final Creator<Post> CREATOR = new PostCreator();

    @SerializedName("title")
    private String title;
    @SerializedName("link")
    private String link;
    @SerializedName("description")
    private String description;
    @SerializedName("pubDate")
    private String pubDate;
    @SerializedName("image_url")
    private String imageUrl;

    public Post(String title, String link, String description, String pubDate, String imageUrl) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.imageUrl = imageUrl;
    }

    public Post(PostDatabase postDatabase) {
        this.title = postDatabase.post.getTitle();
        this.link = postDatabase.post.getLink();
        this.description = postDatabase.post.getDescription();
        this.pubDate = postDatabase.post.getPubDate();
        this.imageUrl = postDatabase.post.getImageUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(title, post.title) &&
                Objects.equals(link, post.link) &&
                Objects.equals(description, post.description) &&
                Objects.equals(pubDate, post.pubDate) &&
                Objects.equals(imageUrl, post.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, description, pubDate, imageUrl);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(description);
        dest.writeString(pubDate);
        dest.writeString(imageUrl);
    }

    private static class PostCreator implements Creator<Post> {

        @Override
        public Post createFromParcel(Parcel source) {
            String title = source.readString();
            String link = source.readString();
            String description = source.readString();
            String pubDate = source.readString();
            String imageUrl = source.readString();

            return new Post(title, link, description, pubDate, imageUrl);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    }
}
