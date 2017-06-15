package com.example.dan.baking_app;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class OpenIngredientsDetailViewTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void selectRecipeCard_SelectRecipeIngredient() throws InterruptedException{
        onView(withId(R.id.recyclerview_recipe_list))
                .perform(RecyclerViewActions.scrollToPosition(3));

        Thread.sleep(5000);

        onView(withId(R.id.recyclerview_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(withId(R.id.recyclerview_single_recipe))
                .perform(actionOnItemAtPosition(5, click()));
    }

}
