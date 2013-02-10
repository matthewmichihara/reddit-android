package com.fourpool.reddit.android.test;

import android.test.ActivityInstrumentationTestCase2;
import com.fourpool.reddit.android.ui.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testActivity() {
        MainActivity activity = getActivity();
        assertNotNull(activity);
    }
}

