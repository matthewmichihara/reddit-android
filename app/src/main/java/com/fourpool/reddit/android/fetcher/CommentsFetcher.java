package com.fourpool.reddit.android.fetcher;

import android.util.Log;
import com.fourpool.reddit.android.model.Comment;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Michihara
 */
public final class CommentsFetcher {
    private static final String TAG = CommentsFetcher.class.getSimpleName();

    private CommentsFetcher() {
        // Prevent instantiation.
    }

    public static List<Comment> fetch(String url) {
        List<Comment> comments = new ArrayList<Comment>();

        String commentsJsonString = HttpRequest.get(url).body();

        try {
            JSONArray rootCommentsArray = new JSONArray(commentsJsonString);
            JSONObject commentsListingObject = rootCommentsArray.getJSONObject(1);

            comments.addAll(getComments(commentsListingObject));
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing comments", e);
        }

        return comments;
    }

    private static List<Comment> getComments(JSONObject commentsObject) {
        List<Comment> comments = new ArrayList<Comment>();

        try {
            JSONObject commentsDataObject = commentsObject.getJSONObject("data");
            JSONArray commentsArray = commentsDataObject.getJSONArray("children");

            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject commentObject = commentsArray.getJSONObject(i);
                JSONObject commentDataObject = commentObject.getJSONObject("data");

                String author = commentDataObject.getString("author");
                String body = commentDataObject.getString("body");
                List<Comment> replies = new ArrayList<Comment>();

                JSONObject repliesObject = commentDataObject.optJSONObject("replies");
                if (repliesObject != null) {
                    replies.addAll(getComments(repliesObject));
                }

                Comment comment = new Comment(author, body, replies);

                for (Comment reply : replies) {
                    reply.setParent(comment);
                }

                comments.add(comment);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing comments", e);
        }

        return comments;
    }
}
