package com.example.dan.baking_app.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class RecipeContentProvider extends ContentProvider {

    public static final int RECIPE = 100;
    public static final int RECIPE_WITH_ID = 101;
    public static final int RECIPE_WITH_NAME = 102;

    public static final int INGREDIENT = 200;
    public static final int INGREDIENT_WITH_ID = 201;

    public static final int STEP = 300;
    public static final int STEP_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPE, RECIPE);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPE + "/#", RECIPE_WITH_ID);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPE + "/name", RECIPE_WITH_NAME);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_INGREDIENT, INGREDIENT);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_INGREDIENT + "/#", INGREDIENT_WITH_ID);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_STEP, STEP);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_STEP + "/#", STEP_WITH_ID);

        return uriMatcher;
    }

    private RecipeDbHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase db = mHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case RECIPE:
                cursor = db.query(RecipeContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case RECIPE_WITH_NAME:
                cursor = db.query(RecipeContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENT:
                cursor = db.query(RecipeContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case STEP:
                cursor = db.query(RecipeContract.StepEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match) {
            case RECIPE:
                id = db.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI, id);
                } else {
                    throw new SQLiteException("Failed to insert row into " + uri);
                }
                break;
            case INGREDIENT:
                id = db.insert(RecipeContract.IngredientEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RecipeContract.IngredientEntry.CONTENT_URI, id);
                } else {
                    throw new SQLiteException("Failed to insert row into " + uri);
                }
                break;
            case STEP:
                id = db.insert(RecipeContract.StepEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RecipeContract.StepEntry.CONTENT_URI, id);
                } else {
                    throw new SQLiteException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int deleted;
        String id;

        switch (match) {
            case RECIPE_WITH_ID:
                id = uri.getPathSegments().get(1);
                deleted = db.delete(RecipeContract.RecipeEntry.TABLE_NAME, "_id=?", new String[] {id});
                break;
            case INGREDIENT_WITH_ID:
                id = uri.getPathSegments().get(1);
                deleted = db.delete(RecipeContract.IngredientEntry.TABLE_NAME, "_id=?", new String[] {id});
                break;
            case STEP_WITH_ID:
                id = uri.getPathSegments().get(1);
                deleted = db.delete(RecipeContract.StepEntry.TABLE_NAME, "_id=?", new String[] {id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (deleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
