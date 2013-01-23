package com.fourpool.reddit.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.fourpool.reddit.R;
import com.fourpool.reddit.model.Link;

import java.util.List;

public class LinkArrayAdapter extends ArrayAdapter<Link> {
    public LinkArrayAdapter(Context context, List<Link> links) {
        super(context, R.layout.layout_link_item, links);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Link link = getItem(position);
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

        holder.tvTitle.setText(link.getTitle());

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
    }
}
