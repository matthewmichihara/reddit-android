package com.fourpool.reddit.android.test;

import com.fourpool.reddit.android.model.Comment;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Michihara
 */
public class CommentTest extends TestCase {
    /**
     *
     */
    public void testGetAllDescendants() {
        Comment commentA = new Comment("authorA", "bodyA", new ArrayList<Comment>());

        List<Comment> commentRepliesB = new ArrayList<Comment>();
        commentRepliesB.add(commentA);
        Comment  commentB = new Comment("authorB", "bodyB", commentRepliesB);
        commentA.setParent(commentB);

        List<Comment> commentRepliesC = new ArrayList<Comment>();
        commentRepliesC.add(commentB);
        Comment commentC = new Comment("authorC", "bodyC", commentRepliesC);
        commentB.setParent(commentC);

        List<Comment> descendants = commentC.getAllDescendantReplies();
        assertEquals(2, descendants.size());
        assertEquals(commentB, descendants.get(0));
        assertEquals(commentA, descendants.get(1));

        // Check levels.
        assertEquals(2, commentA.getLevel());
        assertEquals(1, commentB.getLevel());
        assertEquals(0, commentC.getLevel());
    }
}
