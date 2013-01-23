package com.fourpool.reddit.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.fourpool.reddit.R;
import com.fourpool.reddit.model.Link;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LinkListFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = LinkListFragment.class.getSimpleName();
    private LinkArrayAdapter mAdapter;
    private Callbacks mCallbacks;

    public interface Callbacks {
        public void onLinkClicked(Link link);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_link_list, container, false);

        mAdapter = new LinkArrayAdapter(getActivity(), new ArrayList<Link>());
        ListView listView = (ListView) v.findViewById(R.id.link_list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Link link = (Link) parent.getItemAtPosition(position);
                mCallbacks.onLinkClicked(link);
            }
        });

        getLoaderManager().initLoader(0, null, this).forceLoad();

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new ClassCastException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle data) {
        Log.e(TAG, "onCreateLoader called");
        Loader<String> loader = new AsyncTaskLoader<String>(getActivity()) {
            @Override
            public String loadInBackground() {
                return HttpRequest.get("http://www.reddit.com/.json").body();
            }
        };

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String json) {
        Log.e(TAG, "onLoadFinished called");
        if (getActivity() == null) {
            return;
        }

        List<Link> linkList = new ArrayList<Link>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray children = data.getJSONArray("children");
            for (int i = 0; i < children.length(); i++) {
                JSONObject child = children.getJSONObject(i);
                JSONObject childData = child.getJSONObject("data");

                String title = childData.getString("title");
                String strPermalink = childData.getString("permalink");

                URL permalink = null;
                try {
                    permalink = new URL("http://www.reddit.com" + strPermalink);
                } catch (MalformedURLException e) {
                    Log.e(TAG, "Error parsing permalink", e);
                    return;
                }

                Link link = new Link(title, permalink);
                linkList.add(link);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
            return;
        }

        mAdapter.clear();
        mAdapter.addAll(linkList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }
}
