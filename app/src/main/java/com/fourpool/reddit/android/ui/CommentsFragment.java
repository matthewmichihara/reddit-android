package com.fourpool.reddit.android.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.fourpool.reddit.android.R;
import com.fourpool.reddit.android.fetcher.CommentsFetcher;
import com.fourpool.reddit.android.model.Comment;
import com.fourpool.reddit.android.model.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Michihara
 */
public class CommentsFragment extends SherlockFragment implements LoaderManager.LoaderCallbacks<List<Comment>> {
    public static final String ARG_LISTING = "listing";
    private final List<Comment> mComments = new ArrayList<Comment>();
    private String mCommentsUrl;
    private CommentAdapter mCommentAdapter;
    private TextView mTvTitle;
    private ListView mLvComments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comments, container, false);
        mTvTitle = (TextView) root.findViewById(R.id.title);
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

        // During startup, check if there are arguments passed to the fragment. onStart is a good place to do this
        // because the layout has already been applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set the comments based on the link passed in.
            Listing listing = args.getParcelable(ARG_LISTING);
            updateContent(listing);
        }
    }

    public void updateContent(Listing listing) {
        mCommentsUrl = listing.getPermalink();
        String title = listing.getTitle();

        mTvTitle.setText(title);

        getLoaderManager().initLoader(0, null, this).forceLoad();
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

        mCommentAdapter = new CommentAdapter(getActivity(), mComments);
        mLvComments.setAdapter(mCommentAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Comment>> loader) {
    }
}

