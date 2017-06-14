package com.example.dan.baking_app;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by dcalabrese on 6/14/2017.
 */

@RunWith(AndroidJUnit4.class)
public class OpenIngredientsDetailViewTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void scrollToRecipeCard() {
        onView(withId(R.id.recyclerview_recipe_list))
                .perform(RecyclerViewActions.scrollToPosition(0));
    }

    @Test
    public void clickRecipeCard() {
        onView(withId(R.id.recyclerview_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}
