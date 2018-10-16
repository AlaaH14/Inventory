package com.example.alaa93h.inventory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alaa93h.inventory.database.ProductContract.ProductEntry;

/**
 * Created by Alaa1 on 1/19/18.
 */

public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String PRODUCT_TABLE =  "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.PRODUCT_NAME+ " TEXT NOT NULL, "
                + ProductEntry.PRICE + " TEXT NOT NULL, "
                + ProductEntry.QUANTITY + " INTEGER NOT NULL, "
                + ProductEntry.PRODUCT_IMG + " TEXT, "
                + ProductEntry.SUPPLIER + " TEXT NOT NULL, "
                + ProductEntry.SUPPLIER_EMAIL + " TEXT NOT NULL, "
                + ProductEntry.SUPPLIER_PHONE + " TEXT NOT NULL);";
        db.execSQL(PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
