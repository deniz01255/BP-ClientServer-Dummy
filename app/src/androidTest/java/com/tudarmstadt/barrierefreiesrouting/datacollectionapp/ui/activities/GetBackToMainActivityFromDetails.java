package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GetBackToMainActivityFromDetails {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void getBackToMainActivityFromDetails() {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.nav_action_get_details), withContentDescription("Get Details"), isDisplayed()));
        bottomNavigationItemView.perform(click());

        pressBack();

    }

}
