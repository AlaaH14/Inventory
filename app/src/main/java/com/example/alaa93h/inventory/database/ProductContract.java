package com.example.alaa93h.inventory.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Alaa1 on 1/19/18.
 */

public class ProductContract {

    public static final String CONTENT_AUTHORITY = "com.example.alaa93h.inventory";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PRODUCTS = "inventory";

    private ProductContract(){}

    public static final class ProductEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        // table name
        public static final String TABLE_NAME ="product_db";

        // columns
        public final static String _ID = BaseColumns._ID;
        public static final String PRODUCT_NAME ="product_name";
        public static final String PRICE ="price";
        public static final String QUANTITY ="quantity";
        public static final String PRODUCT_IMG ="product_img";
        public static final String SUPPLIER ="supplier_name";
        public static final String SUPPLIER_EMAIL ="supplier_email";
        public static final String SUPPLIER_PHONE ="supplier_phone";

    }
}
