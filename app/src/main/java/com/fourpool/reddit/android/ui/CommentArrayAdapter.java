package com.fourpool.reddit.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fourpool.reddit.android.R;
import com.fourpool.reddit.android.model.Comment;

import java.util.List;

/**
 * @author Matthew Michihara
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment> {
    private static final int CHILD_COMMENT_PADDING = 20;

    public CommentArrayAdapter(Context context, List<Comment> comments) {
        super(context, R.layout.list_item_comment, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_comment, null);
        }

        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tv_author);
        TextView tvText = (TextView) convertView.findViewById(R.id.tv_text);
        View vPadding = convertView.findViewById(R.id.v_padding);

        Comment comment = getItem(position);

        int padding = comment.getLevel() * CHILD_COMMENT_PADDING;

        tvAuthor.setText(comment.getAuthor());
        tvText.setText(comment.getBody());
        vPadding.setLayoutParams(new RelativeLayout.LayoutParams(padding, ViewGroup.LayoutParams.MATCH_PARENT));

        return convertView;
    }
}
