package com.fourpool.reddit.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Matthew Michihara
 */
public class Listing implements Parcelable {
    private final String mTitle;
    private final String mPermalink;

    public Listing(String title, String permalink) {
        mTitle = title;
        mPermalink = "http://reddit.com" + permalink;
    }

    private Listing(Parcel in) {
        mTitle = in.readString();
        mPermalink = in.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPermalink() {
        return mPermalink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mPermalink);
    }

    public static final Parcelable.Creator<Listing> CREATOR
            = new Parcelable.Creator<Listing>() {
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };



}
