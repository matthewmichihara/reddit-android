package com.fourpool.reddit.android.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.fourpool.reddit.android.fetcher.CommentsFetcher;
import com.fourpool.reddit.android.model.Comment;
import com.fourpool.reddit.android.R;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment {
    public static final String ARG_LINK = "link";
    private final List<Comment> mComments = new ArrayList<Comment>();
    private CommentListAdapter mCommentListAdapter;
    private String mCurrentLink = null;
    private TextView mTvTitle;
    private ListView mLvComments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore the previous comment set by
        // onSaveInstanceState(). This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentLink = savedInstanceState.getString(ARG_LINK, null);
        }

        View root = inflater.inflate(R.layout.fragment_comments, container, false);
        mTvTitle = (TextView) root.findViewById(R.id.link_title);
        mLvComments = (ListView) root.findViewById(R.id.lv_comments);

        mLvComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comment comment = (Comment) parent.getItemAtPosition(position);
                List<Comment> children = comment.getReplies();

                Log.e("ASDF", "num children " + children.size());

                if (comment.getIsExpanded()) {
                    mComments.removeAll(children);
                } else {
                    int commentIndex = mComments.indexOf(comment);
                    mComments.addAll(commentIndex + 1, children);
                }

                // Toggle expanded state.
                comment.setIsExpanded(!comment.getIsExpanded());

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
        mTvTitle.setText(url);
        mCurrentLink = url;

        new AsyncTask<Void, Void, List<Comment>>() {
            @Override
            protected List<Comment> doInBackground(Void... params) {
                return CommentsFetcher.fetch("http://www.reddit.com/comments/183y5i.json");
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

