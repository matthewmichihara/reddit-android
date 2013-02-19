package com.fourpool.reddit.android.ui;

import android.content.Context;
import com.commonsware.cwac.endless.EndlessAdapter;
import com.fourpool.reddit.android.R;
import com.fourpool.reddit.android.fetcher.ListingsFetcher;

/**
 * @author Matthew Michihara
 */
public class ListingEndlessArrayAdapter extends EndlessAdapter {
    private final ListingsFetcher mListingsFetcher;

    public ListingEndlessArrayAdapter(Context context, ListingsFetcher listingsFetcher) {
        super(context, new ListingAdapter(context, listingsFetcher.getCurrentListings()), R.layout.list_item_loading);

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
