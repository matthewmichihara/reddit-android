package com.fourpool.reddit.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.fourpool.reddit.android.R;
import com.fourpool.reddit.android.RedditApplication;
import com.fourpool.reddit.android.fetcher.CommentsFetcher;
import com.fourpool.reddit.android.model.Comment;
import com.fourpool.reddit.android.model.Listing;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Michihara
 */
public class CommentsFragment extends SherlockFragment implements LoaderManager.LoaderCallbacks<List<Comment>> {
    public static final String ARG_LISTING = "listing";
    private final List<Comment> mComments = new ArrayList<Comment>();
    @Inject Bus mBus;
    private String mCommentsUrl;
    private CommentArrayAdapter mCommentAdapter;
    private TextView mTvTitle;
    private TextView mTvSubtitle;
    private ListView mLvComments;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((RedditApplication) activity.getApplication()).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comments, container, false);
        mTvTitle = (TextView) root.findViewById(R.id.title);
        mTvSubtitle = (TextView) root.findViewById(R.id.subtitle);
        mLvComments = (ListView) root.findViewById(R.id.lv_comments);

        mLvComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comment comment = (Comment) parent.getItemAtPosition(position);

                if (comment.getIsExpanded()) {
                    List<Comment> descendants = comment.getAllDescendantReplies();
                    mComments.removeAll(descendants);
                    for (Comment descendant : descendants) {
                        descendant.setIsExpanded(false);
                    }
                    comment.setIsExpanded(false);

                } else {
                    int commentIndex = mComments.indexOf(comment);
                    List<Comment> children = comment.getReplies();
                    mComments.addAll(commentIndex + 1, children);
                    comment.setIsExpanded(true);
                }

                mCommentAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            Listing listing = args.getParcelable(ARG_LISTING);
            onListingSelected(listing);
        }
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
    public Loader<List<Comment>> onCreateLoader(int i, Bundle bundle) {
        Loader<List<Comment>> loader = new AsyncTaskLoader<List<Comment>>(getActivity()) {
            @Override
            public List<Comment> loadInBackground() {
                return CommentsFetcher.fetch(mCommentsUrl + ".json");
            }
        };

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Comment>> loader, List<Comment> comments) {
        mComments.clear();
        mComments.addAll(comments);

        mCommentAdapter = new CommentArrayAdapter(getActivity(), mComments);
        mLvComments.setAdapter(mCommentAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Comment>> loader) {
    }

    @Subscribe
    public void onListingSelected(Listing listing) {
        mCommentsUrl = listing.getPermalink();
        String title = listing.getTitle();
        CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(listing.getCreatedUtc());
        String author = listing.getAuthor();
        String subreddit = listing.getSubreddit();
        String subtitle = getActivity().getString(R.string.submitted_n_time_ago_by_x_to_y, relativeTimeSpanString,
                author, subreddit);

        mTvTitle.setText(title);
        mTvSubtitle.setText(subtitle);

        getLoaderManager().initLoader(0, null, this).forceLoad();
    }
}

