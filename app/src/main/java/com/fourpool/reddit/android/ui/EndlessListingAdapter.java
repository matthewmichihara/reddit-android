package com.fourpool.reddit.android.ui;

import android.content.Context;
import com.commonsware.cwac.endless.EndlessAdapter;
import com.fourpool.reddit.android.R;
import com.fourpool.reddit.android.fetcher.ListingsFetcher;
import com.fourpool.reddit.android.model.Listing;

import java.util.List;

/**
 * @author Matthew Michihara
 */
public class EndlessListingAdapter extends EndlessAdapter {
    private final ListingsFetcher mListingsFetcher;

    public EndlessListingAdapter(Context context, List<Listing> listings, ListingsFetcher listingsFetcher) {
        super(context, new ListingAdapter(context, listings), R.layout.list_item_loading);

        mListingsFetcher = listingsFetcher;
    }

    @Override
    protected boolean cacheInBackground() {
        mListingsFetcher.loadMore();
        return true;
    }

    @Override
    protected void appendCachedData() {
        ListingAdapter listingAdapter = (ListingAdapter) getWrappedAdapter();
        listingAdapter.clear();
        listingAdapter.addAll(mListingsFetcher.getCurrentListings());
    }
}
