package com.fourpool.reddit.android.model;

/**
 * @author Matthew Michihara
 */
public class Listing {
    private final String mTitle;
    private final String mPermalink;

    public Listing(String title, String permalink) {
        mTitle = title;
        mPermalink = "http://reddit.com" + permalink;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPermalink() {
        return mPermalink;
    }
}
