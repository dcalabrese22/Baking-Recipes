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

    private RecipeClickHandler mRecipeClickHandler;

    private ArrayList<Recipe> mRecipes;

    public MasterListAdapter(RecipeClickHandler recipeClickHandler) {
        mRecipeClickHandler = recipeClickHandler;
    }

    public void setRecipeData(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public ArrayList<Recipe> getData() {
        return mRecipes;
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
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {
        TextView target = holder.recipeName;
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



    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView recipeName;

        public RecipeViewHolder(final View view) {
            super(view);
            recipeName = (TextView) view.findViewById(R.id.textview_recipe_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Recipe recipe = mRecipes.get(getAdapterPosition());
                    mRecipeClickHandler.onRecipeClick(recipe);
                }
            });
        }

    }

}
