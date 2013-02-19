package com.fourpool.reddit.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.fourpool.reddit.android.R;
import com.fourpool.reddit.android.model.Comment;

import java.util.List;

/**
 * @author Matthew Michihara
 */
public class CommentAdapter extends ArrayAdapter<Comment> {
    public CommentAdapter(Context context, List<Comment> comments) {
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