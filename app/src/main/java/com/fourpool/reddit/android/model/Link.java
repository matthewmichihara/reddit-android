package com.fourpool.reddit.android.model;

import java.net.URL;

/**
 * @author Matthew Michihara
 */
public class Link {
    private final String mTitle;
    private final URL mPermalink;

    public Link(String title, URL permalink) {
        mTitle = title;
        mPermalink = permalink;
    }

    public String getTitle() {
        return mTitle;
    }

    public URL getPermalink() {
        return mPermalink;
    }
}
