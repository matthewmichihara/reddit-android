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
import com.fourpool.reddit.android.fetcher.ListingsFetcher;
import com.fourpool.reddit.android.model.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a list of links for a particular subreddit.
 *
 * @author Matthew Michihara
 */
public class SubredditFragment extends SherlockFragment implements LoaderManager.LoaderCallbacks<List<Listing>> {

    private static final String TAG = SubredditFragment.class.getSimpleName();
    private ListingAdapter mAdapter;
    private Callbacks mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listing, container, false);

        mAdapter = new ListingAdapter(getActivity(), new ArrayList<Listing>());
        ListView listView = (ListView) v.findViewById(R.id.link_list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Listing listing = (Listing) parent.getItemAtPosition(position);
                mCallbacks.onListingClicked(listing);
            }
        });

        getLoaderManager().initLoader(0, null, this).forceLoad();

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new ClassCastException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public Loader<List<Listing>> onCreateLoader(int id, Bundle data) {
        Loader<List<Listing>> loader = new AsyncTaskLoader<List<Listing>>(getActivity()) {
            @Override
            public List<Listing> loadInBackground() {
                // Empty string means front page.
                return ListingsFetcher.fetch(null);
            }
        };

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Listing>> loader, List<Listing> listings) {
        if (getActivity() == null) {
            return;
        }

        mAdapter.clear();
        mAdapter.addAll(listings);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Listing>> loader) {
    }

    interface Callbacks {
        void onListingClicked(Listing listing);
    }
}
