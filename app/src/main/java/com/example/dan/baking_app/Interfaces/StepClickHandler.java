package com.example.dan.baking_app.Interfaces;

import com.example.dan.baking_app.objects.Step;

/**
 * Interface for handling when a user clicks on a recipe step
 */
public interface StepClickHandler {

    void onStepClick(Step step);

}
