package com.fourpool.reddit.android;

import android.app.Application;
import com.fourpool.reddit.android.ui.CommentsFragment;
import com.fourpool.reddit.android.ui.MainActivity;
import com.fourpool.reddit.android.ui.SubredditFragment;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author Matthew Michihara
 */
public class RedditApplication extends Application {
    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(new RedditModule());
    }

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

    @Module(
            entryPoints = {MainActivity.class, SubredditFragment.class, CommentsFragment.class}
    )
    static class RedditModule {
        @Provides
        @Singleton
        Bus provideBus() {
            return new Bus();
        }
    }
}
