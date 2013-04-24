package com.fourpool.reddit.android.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.fourpool.reddit.android.R;
import com.fourpool.reddit.android.RedditApplication;
import com.fourpool.reddit.android.model.Listing;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class MainActivity extends SherlockFragmentActivity {
    @Inject Bus mBus;
    private CommentsFragment mCommentsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inject ourself into the object graph.
        ((RedditApplication) getApplication()).inject(this);


        // Check that the activity is using the layout version with the fragment container.
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state, then we don't need to do anything and should
            // return or else we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
        }

        SubredditFragment subredditFragment = new SubredditFragment();

        // In case this activity was started with special instructions from an Intent, pass the Intent's extras to the
        // fragment as arguments.
        subredditFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the fragment container.
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, subredditFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        mBus.unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.remove(mCommentsFragment);
                ft.commit();
                manager.popBackStack();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void listingSelected(Listing listing) {
        // If only a single fragment can be displayed at a time, start the CommentsFragment and pass it the selected
        // listing.
        Bundle args = new Bundle();
        args.putParcelable(CommentsFragment.ARG_LISTING, listing);

        // Create comments fragment.
        mCommentsFragment = new CommentsFragment();
        mCommentsFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment container with this fragment, and add the transaction to the back stack
        // so the user can navigate back.
        transaction.replace(R.id.fragment_container, mCommentsFragment);
        transaction.addToBackStack(null);

        // Commit the transaction.
        transaction.commit();

        // Display the action bar home button.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

