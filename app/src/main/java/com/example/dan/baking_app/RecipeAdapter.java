package com.example.dan.baking_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dan.baking_app.ClickHandlers.StepClickHandler;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Recipe;
import com.example.dan.baking_app.objects.Step;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;
    private ArrayList<Object> mIngredientsAndSteps = new ArrayList<>();

    private StepClickHandler mStepClickHandler;

    private static final int VIEWTYPE_INGREDIENT = 1;
    private static final int VIEWTYPE_STEP = 2;

    public RecipeAdapter() {
    }

    public void setData(ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {
        mIngredientsAndSteps.addAll(ingredients);
        mIngredientsAndSteps.addAll(steps);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (mIngredientsAndSteps == null) {
            return 0;
        } else {
            return mIngredientsAndSteps.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIngredientsAndSteps.get(position) instanceof Ingredient) {
            return VIEWTYPE_INGREDIENT;
        } else {
            return VIEWTYPE_STEP;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == VIEWTYPE_INGREDIENT) {
            TextView quantity = ((RecipeIngredientViewHolder) holder).ingredientQuantity;
            TextView name = ((RecipeIngredientViewHolder) holder).ingredientName;
            Ingredient ingredient = (Ingredient) mIngredientsAndSteps.get(position);
            String qAndMeasure = Integer.toString(ingredient.getQuantity()) + ingredient.getMeasure();
            quantity.setText(qAndMeasure);
            name.setText(ingredient.getName());
        } else if (viewType == VIEWTYPE_STEP) {
            TextView shortDesc = ((RecipeStepViewHolder) holder).stepShortDesc;
            Step step = (Step) mIngredientsAndSteps.get(position);
            shortDesc.setText(step.getShortDescription());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (viewType == VIEWTYPE_INGREDIENT) {
            int layoutForIngredient = R.layout.recipe_ingredient;
            View view = layoutInflater.inflate(layoutForIngredient, parent, false);
            return new RecipeIngredientViewHolder(view);
        } else {
            int layoutForStep = R.layout.recipe_step;
            View view = layoutInflater.inflate(layoutForStep, parent, false);
            return new RecipeStepViewHolder(view);
        }
    }

    public class RecipeIngredientViewHolder  extends RecyclerView.ViewHolder{

        TextView ingredientQuantity;
        TextView ingredientName;

        public RecipeIngredientViewHolder(View view) {
            super(view);
            ingredientName = (TextView) view.findViewById(R.id.textview_recipe_ingredient);
            ingredientQuantity = (TextView) view.findViewById(R.id.textview_recipe_quantity);
        }

    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView stepShortDesc;

        public RecipeStepViewHolder(View view) {
            super(view);
            stepShortDesc = (TextView) view.findViewById(R.id.textview_recipe_short_desc);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step step = (Step) mIngredientsAndSteps.get(adapterPosition);
            mStepClickHandler.onStepClick(step);
        }
    }
}
