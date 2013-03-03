package com.fourpool.reddit.android.ui;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.fourpool.reddit.android.R;
import com.fourpool.reddit.android.model.Listing;

import java.util.List;

/**
 * @author Matthew Michihara
 */
public class ListingArrayAdapter extends ArrayAdapter<Listing> {
    public ListingArrayAdapter(Context context, List<Listing> list) {
        super(context, R.layout.list_item_listing, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Listing listing = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_listing, null);

            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            holder.tvSubtitle = (TextView) convertView.findViewById(R.id.subtitle);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Extract listing information we need.
        String title = listing.getTitle();

        CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(listing.getCreatedUtc());
        String author = listing.getAuthor();
        String subreddit = listing.getSubreddit();
        int score = listing.getScore();
        String subtitle = getContext().getString(R.string.m_points_submitted_n_time_ago_by_x_to_y, score,
                relativeTimeSpanString, author, subreddit);

        // Populate views.
        holder.tvTitle.setText(title);
        holder.tvSubtitle.setText(Html.fromHtml(subtitle));

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvSubtitle;
    }
}
