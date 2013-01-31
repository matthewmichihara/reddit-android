package com.fourpool.reddit.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.fourpool.baconapi.Listing;
import com.fourpool.reddit.R;

import java.util.ArrayList;
import java.util.List;

public class SubredditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Listing> {

    private static final String TAG = SubredditFragment.class.getSimpleName();
    private ListingDataChildAdapter mAdapter;
    private Callbacks mCallbacks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_link_list, container, false);

        mAdapter = new ListingDataChildAdapter(getActivity(), new ArrayList<Listing.ListingDataChild>());
        ListView listView = (ListView) v.findViewById(R.id.link_list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Listing.ListingDataChild ldc = (Listing.ListingDataChild) parent.getItemAtPosition(position);
                mCallbacks.onLinkClicked(ldc.data.permalink);
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
    public Loader<Listing> onCreateLoader(int id, Bundle data) {
        Log.e(TAG, "onCreateLoader called");
        Loader<Listing> loader = new AsyncTaskLoader<Listing>(getActivity()) {
            @Override
            public Listing loadInBackground() {
                return Listing.forSubreddit("swimming");
            }
        };

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Listing> loader, Listing listing) {
        Log.e(TAG, "onLoadFinished called");
        if (getActivity() == null) {
            return;
        }

        Listing.ListingData data = listing.data;
        List<Listing.ListingDataChild> children = data.children;

        mAdapter.clear();
        mAdapter.addAll(children);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Listing> loader) {
    }

    public interface Callbacks {
        public void onLinkClicked(String permalink);
    }
}
