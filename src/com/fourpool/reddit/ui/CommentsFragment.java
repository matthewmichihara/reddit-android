package com.fourpool.reddit.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fourpool.reddit.R;

public class CommentsFragment extends Fragment {
    public final static String ARG_LINK = "link";
    private String mCurrentLink = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore the previous comment set by onSaveInstanceState(). This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentLink = savedInstanceState.getString(ARG_LINK, null);
        }

        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment. onStart is a good palce to do this because the layout has already been applied to the fragment at this point so we can safely call the method below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set the comments based on the link passed in.
            updateContent(args.getString(ARG_LINK));
        } else if (mCurrentLink != null) {
            // Set the comments based on saved instance state defined during onCreateView.
            updateContent(mCurrentLink);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current comment link in case we need to recreate the fragment.
        outState.putString(ARG_LINK, mCurrentLink);
    }

    public void updateContent(String url) {
        TextView tvTitle = (TextView) getActivity().findViewById(R.id.link_title);
        tvTitle.setText(url);
        mCurrentLink = url;
    }
}

