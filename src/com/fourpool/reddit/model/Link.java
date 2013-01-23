package com.fourpool.reddit.model;

import java.net.URL;

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
