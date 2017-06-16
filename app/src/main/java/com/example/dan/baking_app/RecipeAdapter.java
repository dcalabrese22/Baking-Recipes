package com.example.dan.baking_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dan.baking_app.Interfaces.StepClickHandler;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Adapter for populating recyclerview to contain recipe ingredients and steps
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //arraylist if ingredients followed by steps
    private ArrayList<Object> mIngredientsAndSteps = new ArrayList<>();

    private StepClickHandler mStepClickHandler;

    //distinguish viewtypes to populate correctly
    private static final int VIEWTYPE_INGREDIENT = 1;
    private static final int VIEWTYPE_STEP = 2;

    public RecipeAdapter(StepClickHandler clickHandler) {
        mStepClickHandler = clickHandler;
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
        //handle views that need to be populated with an ingredient
        if (viewType == VIEWTYPE_INGREDIENT) {
            TextView quantity = ((RecipeIngredientViewHolder) holder).ingredientQuantity;
            TextView name = ((RecipeIngredientViewHolder) holder).ingredientName;
            Ingredient ingredient = (Ingredient) mIngredientsAndSteps.get(position);
            //strip zeros from double if necessary
            DecimalFormat df = new DecimalFormat("####.#");
            String quantityStr = df.format(ingredient.getQuantity());
            //don't display unit for things like eggs
            if (ingredient.getMeasure().equals("UNIT")) {
                quantity.setText(quantityStr);
            } else {
                String qAndMeasure = quantityStr + " " + ingredient.getMeasure().toLowerCase();
                quantity.setText(qAndMeasure);
            }
            name.setText(ingredient.getName());
        //handles views that need to be populated with a step
        } else if (viewType == VIEWTYPE_STEP) {
            TextView shortDesc = ((RecipeStepViewHolder) holder).stepShortDesc;
            ImageView thumb = ((RecipeStepViewHolder) holder).stepImage;
            Step step = (Step) mIngredientsAndSteps.get(position);
            String thumbUrl = step.getThumbnailUrl();
            if (!thumbUrl.equals("")) {
                Picasso.with(thumb.getContext()).load(thumbUrl).into(thumb);
            }
            shortDesc.setText(step.getShortDescription());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //inflate the ingredient layout for ingredients
        if (viewType == VIEWTYPE_INGREDIENT) {
            int layoutForIngredient = R.layout.recipe_ingredient;
            View view = layoutInflater.inflate(layoutForIngredient, parent, false);
            return new RecipeIngredientViewHolder(view);
        //inflate the step layout for steps
        } else {
            int layoutForStep = R.layout.recipe_step;
            View view = layoutInflater.inflate(layoutForStep, parent, false);
            return new RecipeStepViewHolder(view);
        }
    }

    //ViewHolder for Recipe Ingredients
    public class RecipeIngredientViewHolder  extends RecyclerView.ViewHolder{

        TextView ingredientQuantity;
        TextView ingredientName;

        public RecipeIngredientViewHolder(View view) {
            super(view);
            ingredientName = (TextView) view.findViewById(R.id.textview_recipe_ingredient);
            ingredientQuantity = (TextView) view.findViewById(R.id.textview_recipe_quantity);
        }

    }

    //ViewHolder for Recipe Steps
    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView stepShortDesc;
        ImageView stepImage;

        public RecipeStepViewHolder(View view) {
            super(view);
            stepShortDesc = (TextView) view.findViewById(R.id.textview_recipe_short_desc);
            stepImage = (ImageView) view.findViewById(R.id.imageview_step_thumb);
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
