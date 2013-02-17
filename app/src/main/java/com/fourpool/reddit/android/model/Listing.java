package com.fourpool.reddit.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Matthew Michihara
 */
public class Listing implements Parcelable {
    public static final Parcelable.Creator<Listing> CREATOR
            = new Parcelable.Creator<Listing>() {
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };
    private final String mTitle;
    private final String mPermalink;
    private final String mAuthor;
    private final String mSubreddit;
    private final long mCreatedUtc;

    public Listing(String title, String permalink, String author, String subreddit, long createdUtc) {
        mTitle = title;
        mPermalink = "http://reddit.com" + permalink;
        mAuthor = author;
        mSubreddit = subreddit;
        mCreatedUtc = createdUtc * 1000;
    }

    private Listing(Parcel in) {
        mTitle = in.readString();
        mPermalink = in.readString();
        mAuthor = in.readString();
        mSubreddit = in.readString();
        mCreatedUtc = in.readLong();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPermalink() {
        return mPermalink;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getSubreddit() {
        return mSubreddit;
    }

    public long getCreatedUtc() {
        return mCreatedUtc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mPermalink);
        out.writeString(mAuthor);
        out.writeString(mSubreddit);
        out.writeLong(mCreatedUtc);
    }


}
