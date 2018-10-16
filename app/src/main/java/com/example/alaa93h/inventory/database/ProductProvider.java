package com.example.alaa93h.inventory.database;

/**
 * Created by Alaa1 on 1/22/18.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.alaa93h.inventory.database.ProductContract.ProductEntry;

public class ProductProvider extends ContentProvider {

    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    private static final int PRODUCTS = 100;

    private static final int PRODUCTS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCTS_ID);
    }

    private ProductDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:

                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCTS_ID:

                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        String pName = values.getAsString(ProductEntry.PRODUCT_NAME);
        if (pName == null) {
            throw new IllegalArgumentException("product requires a name");
        }

        String supplier = values.getAsString(ProductEntry.SUPPLIER);
        if (supplier == null) {
            throw new IllegalArgumentException("supplier requires a name");
        }

        String price = values.getAsString(ProductEntry.PRICE);
        if (price == null) {
            throw new IllegalArgumentException("product requires a price");
        }

        String sPhone = values.getAsString(ProductEntry.SUPPLIER_PHONE);
        if (sPhone == null) {
            throw new IllegalArgumentException("supplier requires a name");
        }

        String sEmail = values.getAsString(ProductEntry.SUPPLIER_EMAIL);
        if (sEmail == null) {
            throw new IllegalArgumentException("supplier requires an e-mail");
        }

        Integer quantity = values.getAsInteger(ProductEntry.QUANTITY);
        if ( quantity < 0) {
            throw new IllegalArgumentException("products requires valid quantity");
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(ProductEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCTS_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ProductEntry.PRODUCT_NAME)) {
            String pName = values.getAsString(ProductEntry.PRODUCT_NAME);
            if (pName == null) {
                throw new IllegalArgumentException("product requires a name");
            }
        }

        if (values.containsKey(ProductEntry.SUPPLIER)) {
            String sName = values.getAsString(ProductEntry.SUPPLIER);
            if (sName == null) {
                throw new IllegalArgumentException("supplier requires a name");
            }
        }

        if (values.containsKey(ProductEntry.PRICE)) {
            String price = values.getAsString(ProductEntry.PRICE);
            if (price == null) {
                throw new IllegalArgumentException("product requires a price");
            }
        }

        if (values.containsKey(ProductEntry.SUPPLIER_PHONE)) {
            String sPhone = values.getAsString(ProductEntry.SUPPLIER_PHONE);
            if (sPhone == null) {
                throw new IllegalArgumentException("supplier requires a phone");
            }
        }

        if (values.containsKey(ProductEntry.SUPPLIER_EMAIL)) {
            String sEmail = values.getAsString(ProductEntry.SUPPLIER_EMAIL);
            if (sEmail == null) {
                throw new IllegalArgumentException("supplier requires an e-mail");
            }
        }

        if (values.containsKey(ProductEntry.QUANTITY)) {
            Integer quantity = values.getAsInteger(ProductEntry.QUANTITY);
            if (quantity < 0) {
                throw new IllegalArgumentException("product requires valid quantity");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowUpdated = db.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                rowDeleted = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCTS_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowDeleted = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCTS_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}