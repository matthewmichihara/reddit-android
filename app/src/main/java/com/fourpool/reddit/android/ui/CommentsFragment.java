package com.fourpool.reddit.android.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
public class CommentsFragment extends SherlockFragment {
    public static final String ARG_LISTING = "listing";
    private final List<Comment> mComments = new ArrayList<Comment>();
    private CommentListAdapter mCommentListAdapter;
    private TextView mTvTitle;
    private ListView mLvComments;

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

                mCommentListAdapter.notifyDataSetChanged();
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
        final String url = listing.getPermalink();
        String title = listing.getTitle();

        mTvTitle.setText(title);

        new AsyncTask<Void, Void, List<Comment>>() {
            @Override
            protected List<Comment> doInBackground(Void... params) {
                return CommentsFetcher.fetch(url + ".json");
            }

            @Override
            protected void onPostExecute(List<Comment> comments) {
                mComments.clear();
                mComments.addAll(comments);

                mCommentListAdapter = new CommentListAdapter(getActivity(), mComments);
                mLvComments.setAdapter(mCommentListAdapter);
                mCommentListAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public class CommentListAdapter extends ArrayAdapter<Comment> {
        public CommentListAdapter(Context context, List<Comment> comments) {
            super(context, R.layout.list_item_comment, comments);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_comment, null);
            }

            TextView tvAuthor = (TextView) convertView.findViewById(R.id.tv_author);
            TextView tvText = (TextView) convertView.findViewById(R.id.tv_text);

            Comment comment = getItem(position);

            String padding = "";
            for (int i = 0; i < comment.getLevel(); i++) {
                padding += "---";
            }

            tvAuthor.setText(padding + comment.getAuthor());
            tvText.setText(padding + comment.getBody());

            return convertView;
        }

    }
}

