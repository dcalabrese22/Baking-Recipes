package com.example.dan.baking_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dan.baking_app.ClickHandlers.RecipeClickHandler;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Recipe;

import java.util.ArrayList;

public class MasterListAdapter extends
        RecyclerView.Adapter<MasterListAdapter.RecipeViewHolder> {

    private static final int RECIPE_VIEW_TYPE = 1;
    private static final int INGREDIENT_VIEW_TYPE = 2;
    private static final int STEP_VIEW_TYPE = 3;

    private RecipeClickHandler mRecipeClickHandler;

    private ArrayList<Recipe> mRecipes;
    private Context mContext;

    public MasterListAdapter(Context context) {
        mContext = context;
    }

    public void setRecipeData(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        } else {
            return mRecipes.size();
        }
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        TextView target = ((RecipeViewHolder) holder).recipeName;
        Recipe recipe = mRecipes.get(position);
        String recipeName = recipe.getName();
        target.setText(recipeName);
    }


    @Override
    public MasterListAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        int layoutForRecipe = R.layout.recipe_item;
        View view = layoutInflater.inflate(layoutForRecipe, parent, false);
        return new RecipeViewHolder(view);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView recipeName;

        public RecipeViewHolder(View view) {
            super(view);
            recipeName = (TextView) view.findViewById(R.id.textview_recipe_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipes.get(adapterPosition);
            mRecipeClickHandler.onRecipeClick();
        }
    }

//    public class IngredientViewHolder extends RecyclerView.ViewHolder {
//
//
//    }
}
