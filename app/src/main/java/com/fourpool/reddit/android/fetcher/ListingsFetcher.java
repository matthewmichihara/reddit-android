package com.fourpool.reddit.android.fetcher;

import android.util.Log;
import com.fourpool.reddit.android.model.Listing;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** @author Matthew Michihara */
public class ListingsFetcher {
    private static final String TAG = ListingsFetcher.class.getSimpleName();
    private final String mSubreddit;
    private final List<Listing> mListings = new ArrayList<Listing>();
    private String mAfter;

    public ListingsFetcher(String subreddit) {
        mSubreddit = subreddit;
    }

    /** Clears the internal state. */
    public synchronized void clear() {
        mListings.clear();
        mAfter = null;
    }

    /** Returns a copy of all fetched listings. */
    public synchronized List<Listing> getCurrentListings() {
        return new ArrayList<Listing>(mListings);
    }

    public synchronized void loadMore() {
        String url = "http://reddit.com/.json";

        if (mSubreddit != null) {
            url = "http://reddit.com/r/" + mSubreddit + ".json";
        }

        if (mAfter != null) {
            url += "?after=" + mAfter;
        }

        ListingData listingData = fetch(url);

        mListings.addAll(listingData.mListings);
        mAfter = listingData.mAfter;
    }

    /** Synchronously fetches listing data. */
    private ListingData fetch(String url) {
        List<Listing> listings = new ArrayList<Listing>();
        String after = null;

        String listingsJsonString = HttpRequest.get(url).body();

        try {
            JSONObject rootListingsObject = new JSONObject(listingsJsonString);
            JSONObject dataObject = rootListingsObject.getJSONObject("data");
            JSONArray listingsJsonArray = dataObject.getJSONArray("children");
            after = dataObject.getString("after");

            for (int i = 0; i < listingsJsonArray.length(); i++) {
                JSONObject childListing = listingsJsonArray.getJSONObject(i);
                JSONObject childListingData = childListing.getJSONObject("data");

                String title = childListingData.getString("title");
                String permalink = childListingData.getString("permalink");
                String listingUrl = childListingData.getString("url");
                String author = childListingData.getString("author");
                String subreddit = childListingData.getString("subreddit");
                long createdUtc = childListingData.getLong("created_utc");
                int score = childListingData.getInt("score");

                Listing listing = new Listing(title, permalink, listingUrl, author, subreddit, createdUtc, score);
                listings.add(listing);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing listings", e);
        }

        return new ListingData(listings, after);
    }

    /** Container object for returning listing data. */
    class ListingData {
        List<Listing> mListings;
        String mAfter;

        public ListingData(List<Listing> listings, String after) {
            mListings = listings;
            mAfter = after;
        }
    }
}
