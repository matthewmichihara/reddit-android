package com.fourpool.reddit.android.ui;

import android.content.Context;
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
public class ListingAdapter extends ArrayAdapter<Listing> {
    public ListingAdapter(Context context, List<Listing> list) {
        super(context, R.layout.layout_link_item, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Listing listing = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_link_item, null);

            holder = new ViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.link_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(listing.getTitle());

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
    }
}
