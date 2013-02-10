package com.fourpool.reddit.android.model;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

import java.util.List;

public class Listing {
    public String kind;
    public ListingData data;

    public static Listing forSubreddit(String subreddit) {
        String url = "http://www.reddit.com";
        if ((subreddit != null) && !subreddit.isEmpty()) {
            url += "/r/" + subreddit + ".json";
        } else {
            url += "/.json";
        }

        String json = HttpRequest.get(url).body();
        Listing listing = new Gson().fromJson(json, Listing.class);
        return listing;
    }

    public static class ListingData {
        public String modhash;
        public List<ListingDataChild> children;
        public String after;
        public String before;
    }

    public static class ListingDataChild {
        public String kind;
        public ListingDataChildData data;
    }

    public static class ListingDataChildData {
        public String domain;
        public String banned_by;
        public Object media_embed;
        public String subreddit;
        public String selftext_html;
        public String selftext;
        public String likes;
        public String link_flair_text;
        public String id;
        public boolean clicked;
        public String title;
        public int num_comments;
        public int score;
        public String approved_by;
        public boolean over_18;
        public boolean hidden;
        public String thumbnail;
        public String subreddit_id;
        public Object edited;
        public boolean link_flair_css_class;
        public boolean author_flair_css_class;
        public int downs;
        public boolean saved;
        public boolean is_self;
        public String permalink;
        public String name;
        public String created;
        public String url;
        public String author_flair_text;
        public String author;
        public String created_utc;
        public Object media;
        public String num_reports;
        public int ups;
    }
}
