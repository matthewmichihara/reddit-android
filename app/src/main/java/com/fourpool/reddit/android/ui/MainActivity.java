package com.fourpool.reddit.android.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fourpool.reddit.android.R;

public class MainActivity extends SherlockFragmentActivity implements SubredditFragment.Callbacks {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

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
    public void onLinkClicked(String permalink) {
        // Create comments fragment and pass it the link to its content.
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString(CommentsFragment.ARG_LINK, permalink);
        commentsFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment container with this fragment, and add the transaction to the back stack
        // so the user can navigate back.
        transaction.replace(R.id.fragment_container, commentsFragment);
        transaction.addToBackStack(null);

        // Commit the transaction.
        transaction.commit();
    }
}

