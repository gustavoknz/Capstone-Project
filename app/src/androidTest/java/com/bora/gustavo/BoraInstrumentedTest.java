package com.bora.gustavo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bora.gustavo.activities.MainActivity;
import com.bora.gustavo.helper.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class BoraInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void init() {
        mMainActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        Utils mockUtils = mock(Utils.class);
        doReturn("whatever").when(mockUtils).getUserUid();
        final Activity activity = mMainActivityTestRule.launchActivity(new Intent());
        ((MainActivity) activity).setUtilsInstance(mockUtils);
    }

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.bora.gustavo", appContext.getPackageName());
    }

    @Test
    public void testNavigationDrawer() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        if (new Utils().getUserUid() == null) {
            onView(withText(R.string.navigation_login)).check(matches(isDisplayed()));
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_login));
            onView(withId(R.id.login_button)).check(matches(isDisplayed()));
            onView(withId(R.id.login_button)).perform(click());
            onView(withId(R.id.login_button)).check(matches(isDisplayed()));
        } else {
            onView(withText(R.string.navigation_logout)).check(matches(isDisplayed()));
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
            onView(withText(R.string.navigation_logout)).check(doesNotExist());
        }
    }

    @Test
    public void testFabNewGymCancel() {
        onView(withId(R.id.main_fab)).perform(click());
        onView(withText(R.string.new_gym_dialog_message)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click());
        onView(withText(R.string.new_gym_dialog_message)).check(doesNotExist());
    }

    @Test
    public void testFabNewGymSaveFailAddress() {
        onView(withId(R.id.main_fab)).perform(click());
        onView(withText(R.string.new_gym_dialog_message)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.new_gym_dialog_message)).check(matches(isDisplayed()));
    }

    @Test
    public void testFabNewGymAddressHasFocus() {
        onView(withId(R.id.main_fab)).perform(click());
        onView(withText(R.string.new_gym_dialog_message)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.new_gym_dialog_message)).check(matches(isDisplayed()));
    }

    @Test
    public void testFabNewGymSaveFailEquipment() {
        onView(withId(R.id.main_fab)).perform(click());
        onView(withText(R.string.new_gym_dialog_message)).check(matches(isDisplayed()));
        onView(withId(R.id.form_gym_address)).perform(typeText("A random address"), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText(R.string.new_gym_dialog_message)).check(matches(isDisplayed()));
    }
}
