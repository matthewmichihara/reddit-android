package com.fourpool.reddit.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.fourpool.reddit.android.R;
import com.fourpool.reddit.android.RedditApplication;
import com.fourpool.reddit.android.fetcher.ListingsFetcher;
import com.fourpool.reddit.android.model.Listing;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Displays a list of links for a particular subreddit.
 *
 * @author Matthew Michihara
 */
public class SubredditFragment extends SherlockFragment implements LoaderManager.LoaderCallbacks<ListingsFetcher> {

    private static final String TAG = SubredditFragment.class.getSimpleName();
    @Inject Bus mBus;
    private ListingsFetcher mListingFetcher;
    private PullToRefreshListView mLvListings;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((RedditApplication) activity.getApplication()).inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListingFetcher = new ListingsFetcher(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listing, container, false);

        mLvListings = (PullToRefreshListView) v.findViewById(R.id.link_list);

        mLvListings.setEmptyView(v.findViewById(R.id.pb_loading));

        mLvListings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Listing listing = (Listing) parent.getItemAtPosition(position);
                mBus.post(listing);
            }
        });

        mLvListings.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mListingFetcher.clear();
                getLoaderManager().restartLoader(0, null, SubredditFragment.this).forceLoad();
            }
        });

        getLoaderManager().initLoader(0, null, this).forceLoad();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Hide the action bar 'up' button. Not sure if this belongs here...
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public Loader<ListingsFetcher> onCreateLoader(int id, Bundle data) {
        Loader<ListingsFetcher> loader = new AsyncTaskLoader<ListingsFetcher>(getActivity()) {
            @Override
            public ListingsFetcher loadInBackground() {
                mListingFetcher.loadMore();
                return mListingFetcher;
            }
        };

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ListingsFetcher> loader, ListingsFetcher listingsFetcher) {
        if (getActivity() == null) {
            return;
        }

        ListingEndlessArrayAdapter adapter = new ListingEndlessArrayAdapter(getActivity(), listingsFetcher);
        mLvListings.setAdapter(adapter);
        mLvListings.onRefreshComplete();
    }

    @Override
    public void onLoaderReset(Loader<ListingsFetcher> loader) {
        // Um stuff should probably be done here but I don't really get how this works...
    }
}
