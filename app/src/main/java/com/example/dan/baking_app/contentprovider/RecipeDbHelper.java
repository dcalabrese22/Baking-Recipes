package com.example.dan.baking_app.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dcalabrese on 6/2/2017.
 */

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipeDb.db";

    private static final int VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_RECIPE_TABLE =
                "Create Table " + RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                        RecipeContract.RecipeEntry._ID              + " Integer Primary Key, " +
                        RecipeContract.RecipeEntry.COLUMN_NAME      + " Text Not Null" +
                        ");";
        db.execSQL(CREATE_RECIPE_TABLE);

        final String CREATE_INGREDIENT_TABLE =
                "Create Table " + RecipeContract.IngredientEntry.TABLE_NAME + " (" +
                        RecipeContract.IngredientEntry._ID              + " Integer Primary Key, " +
                        RecipeContract.IngredientEntry.COLUMN_NAME      + " Text Not Null, "     +
                        RecipeContract.IngredientEntry.COLUMN_MEASURE   + " Text Not Null, "     +
                        RecipeContract.IngredientEntry.COLUMN_QUANTITY  + " Real Not Null, "     +
                        RecipeContract.IngredientEntry.FOREIGN_KEY      + " Integer Not Null, "  +
                        "Foreign Key (" + RecipeContract.IngredientEntry.FOREIGN_KEY + ") References " +
                        RecipeContract.RecipeEntry.TABLE_NAME + "(" +
                        RecipeContract.RecipeEntry._ID + ")" +
                        ");";
        db.execSQL(CREATE_INGREDIENT_TABLE);

        final String CREATE_STEP_TABLE =
                "Create Table " + RecipeContract.StepEntry.TABLE_NAME + " (" +
                        RecipeContract.StepEntry._ID                + " Integer Primary Key, "  +
                        RecipeContract.StepEntry.COLUMN_DESCRIPTION + " Text Not Null, "        +
                        RecipeContract.StepEntry.COLUMN_SHORT_DESC  + " Text Not Null, "        +
                        RecipeContract.StepEntry.COLUMN_URL         + " Text Not Null, "        +
                        RecipeContract.StepEntry.FOREIGN_KEY        + " Integer Not Null, "     +
                        "Foreign Key (" + RecipeContract.StepEntry.FOREIGN_KEY + ") References "+
                        RecipeContract.RecipeEntry.TABLE_NAME + "(" +
                        RecipeContract.RecipeEntry._ID + ")" +
                        ");";
        db.execSQL(CREATE_STEP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
