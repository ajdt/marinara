package com.itsaunixsystem.marinara;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author: ajdt on 7/30/16.
 * @description: test that timer button UI works as expected
 */

@RunWith(AndroidJUnit4.class)
public class TimerButtonTest {

    @Rule
    public ActivityTestRule<TimerActivity> activity_rule =
            new ActivityTestRule<>(TimerActivity.class) ;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void appStartsInReadyState() {
        // ensure app displays READY image
        //onView(withId(R.id.timer_iv)).check(matches(withContentDescription(R.drawable.ready_600_200))) ;
        onView(withId(R.id.timer_iv)).check(matches(isDisplayed())) ;
        onView(withId(R.id.timer_iv)).check(matches(MyMatchers.IsUsingDrawable(R.drawable.ready_600_200))) ;
    }
}
