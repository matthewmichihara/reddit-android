package com.fourpool.reddit.android.model;

import java.util.List;

/**
 * @author Matthew Michihara
 */
public class Comment {
    private final String mAuthor;
    private final String mBody;
    private final List<Comment> mReplies;
    private Comment mParent;
    private boolean mIsExpanded;

    public Comment(String author, String body, List<Comment> replies) {
        mAuthor = author;
        mBody = body;
        mReplies = replies;

        mIsExpanded = false;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getBody() {
        return mBody;
    }

    public List<Comment> getReplies() {
        return mReplies;
    }

    public Comment getParent() {
        return mParent;
    }

    public void setParent(Comment parent) {
        mParent = parent;
    }

    public int getLevel() {
        int level = 0;
        Comment c = mParent;
        while (c != null) {
            c = c.getParent();
            level++;
        }

        return level;
    }

    public boolean getIsExpanded() {
        return mIsExpanded;
    }

    public void setIsExpanded(boolean expanded) {
        mIsExpanded = expanded;
    }
}
