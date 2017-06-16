package com.example.dan.baking_app;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.action.ViewActions.click;

/**
 * Created by dcalabrese on 6/16/2017.
 */

class ClickOnTextView implements ViewAction {

    ViewAction click = click();
    int textViewId;

    public ClickOnTextView(int textViewId) {
        this.textViewId = textViewId;
    }

    @Override
    public Matcher<View> getConstraints() {
        return click.getConstraints();

    }

    @Override
    public String getDescription() {
        return " click on TextView with id: " + textViewId;
    }

    @Override
    public void perform(UiController uiController, View view) {
        click.perform(uiController, view.findViewById(textViewId));
    }
}
