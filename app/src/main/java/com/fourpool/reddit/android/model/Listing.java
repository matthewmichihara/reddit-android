package com.fourpool.reddit.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/** @author Matthew Michihara */
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
    private static final long MILLISECONDS_IN_SECOND = 1000;
    private final String mTitle;
    private final String mPermalink;
    private final String mUrl;
    private final String mAuthor;
    private final String mSubreddit;
    private final long mCreatedUtc;
    private final int mScore;

    public Listing(String title, String permalink, String url, String author, String subreddit, long createdUtc,
                   int score) {
        mTitle = title;
        mPermalink = "http://reddit.com" + permalink;
        mUrl = url;
        mAuthor = author;
        mSubreddit = subreddit;
        mCreatedUtc = createdUtc * MILLISECONDS_IN_SECOND;
        mScore = score;
    }

    private Listing(Parcel in) {
        mTitle = in.readString();
        mPermalink = in.readString();
        mUrl = in.readString();
        mAuthor = in.readString();
        mSubreddit = in.readString();
        mCreatedUtc = in.readLong();
        mScore = in.readInt();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPermalink() {
        return mPermalink;
    }

    public String getUrl() {
        return mUrl;
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

    public int getScore() {
        return mScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mPermalink);
        out.writeString(mUrl);
        out.writeString(mAuthor);
        out.writeString(mSubreddit);
        out.writeLong(mCreatedUtc);
        out.writeInt(mScore);
    }


}
