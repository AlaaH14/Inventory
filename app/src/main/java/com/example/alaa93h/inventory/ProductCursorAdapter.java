package com.example.alaa93h.inventory;

/**
 * Created by Alaa1 on 25/01/2018.
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.alaa93h.inventory.database.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView productNameTextView = (TextView) view.findViewById(R.id.name);
        TextView supplierTextView = (TextView) view.findViewById(R.id.supplier);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        Button sale = (Button) view.findViewById(R.id.sale);

        final Context context1 = context;
        sale.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(quantityTextView.getText().toString());
                if (quantity != 0)
                    quantity = quantity - 1;
                else
                    quantity = 0;
//                ContentValues values = new ContentValues();
//                values.put(ProductEntry.QUANTITY, quantity);
                quantityTextView.setText(String.valueOf(quantity));

//                context1.getContentResolver().update(EditorActivity.mCurrentProductUri, values, null, null);
            }
        });

        int productColumnIndex = cursor.getColumnIndex(ProductEntry.PRODUCT_NAME);
        int supplierColumnIndex = cursor.getColumnIndex(ProductEntry.SUPPLIER);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.QUANTITY);

        String productName = cursor.getString(productColumnIndex);
        String supplierName = cursor.getString(supplierColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        String quantity = cursor.getString(quantityColumnIndex);

        if (TextUtils.isEmpty(supplierName)) {
            supplierName = context.getString(R.string.unknown_supplier);
        }

        productNameTextView.setText(productName);
        supplierTextView.setText(supplierName);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);
    }
}