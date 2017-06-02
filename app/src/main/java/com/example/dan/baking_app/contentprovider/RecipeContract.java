package com.example.dan.baking_app.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dcalabrese on 6/2/2017.
 */

public class RecipeContract {

    public static final String AUTHORITY = "com.example.dan.baking_app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECIPE = "recipe";
    public static final String PATH_INGREDIENT = "ingredient";
    public static final String PATH_STEP = "step";

    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPE).build();

        public static final String TABLE_NAME = "recipe";
        public static final String COLUMN_NAME = "name";
    }

    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENT).build();

        public static final String TABLE_NAME = "ingredient";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String FOREIGN_KEY = "recipe_id";
    }

    public static final class StepEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STEP).build();

        public static final String TABLE_NAME = "step";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SHORT_DESC = "short_description";
        public static final String COLUMN_URL = "url";
        public static final String FOREIGN_KEY = "recipe_id";

    }
}
