package com.fourpool.reddit.android.fetcher;

import android.util.Log;
import com.fourpool.reddit.android.model.Listing;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Michihara
 */
public final class ListingsFetcher {
    private static final String TAG = ListingsFetcher.class.getSimpleName();

    private ListingsFetcher() {
    }

    /**
     * Fetches a {@link List} of {@link Listing}s for a particular subreddit.
     *
     * @param subredditName The name of the subreddit
     */
    public static List<Listing> fetch(String subredditName) {
        List<Listing> listings = new ArrayList<Listing>();

        // Default to the front page.
        String subredditUrl = "http://reddit.com/.json";
        if (subredditName != null) {
            subredditUrl = "http://reddit.com/r/" + subredditName + ".json";
        }
        String listingsJsonString = HttpRequest.get(subredditUrl).body();

        try {
            JSONObject rootListingsObject = new JSONObject(listingsJsonString);
            JSONObject dataObject = rootListingsObject.getJSONObject("data");
            JSONArray listingsJsonArray = dataObject.getJSONArray("children");

            for (int i = 0; i < listingsJsonArray.length(); i++) {
                JSONObject childListing = listingsJsonArray.getJSONObject(i);
                JSONObject childListingData = childListing.getJSONObject("data");

                String title = childListingData.getString("title");
                String permalink = childListingData.getString("permalink");
                String author = childListingData.getString("author");
                String subreddit = childListingData.getString("subreddit");
                long createdUtc = childListingData.getLong("created_utc");

                Listing listing = new Listing(title, permalink, author, subreddit, createdUtc);
                listings.add(listing);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing listings", e);
        }

        return listings;
    }
}
