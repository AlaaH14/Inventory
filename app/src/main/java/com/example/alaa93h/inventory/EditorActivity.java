package com.example.alaa93h.inventory;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;

import com.example.alaa93h.inventory.database.ProductContract.ProductEntry;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 0;

    private Uri mCurrentProductUri;

    private EditText mProductNameEditText;
    private EditText mPriceEditText;
    private EditText mSupplierEditText;
    private EditText mSupplierEmailEditText;
    private EditText mSupplierPhoneEditText;
    private EditText mQuantityValueTextView;
    private Button mUp;
    private Button mDown;
    private int quantity;

    private boolean mProductHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    public int getQuantity() {
        return quantity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();
        Button contact = (Button) findViewById(R.id.contact);

        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_product));
            contact.setVisibility(View.GONE);
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_prduct));
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mSupplierPhoneEditText.getText().toString()));
                    startActivity(intent);
                }
            });
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        mProductNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mSupplierEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);
        mSupplierEmailEditText = (EditText) findViewById(R.id.edit_supplier_email);
        mUp = (Button) findViewById(R.id.up);
        mDown = (Button) findViewById(R.id.down);
        mQuantityValueTextView = (EditText) findViewById(R.id.quan_value);

        quantity = Integer.parseInt(mQuantityValueTextView.getText().toString().trim());

        mUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = quantity + 1;
                mQuantityValueTextView.setText(String.valueOf(quantity));
            }
        });

        mDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity != 0)
                    quantity = quantity - 1;
                else
                    quantity = 0;

                mQuantityValueTextView.setText(String.valueOf(quantity));
            }
        });

        mProductNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchListener);
        mSupplierEmailEditText.setOnTouchListener(mTouchListener);
        mUp.setOnTouchListener(mTouchListener);
        mDown.setOnTouchListener(mTouchListener);
    }

    private void saveProduct() {

        String pNameString = mProductNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String sPhone = mSupplierPhoneEditText.getText().toString().trim();
        String sEmail = mSupplierEmailEditText.getText().toString().trim();

        if (mCurrentProductUri == null &&
                TextUtils.isEmpty(pNameString) && TextUtils.isEmpty(supplierString) &&
                TextUtils.isEmpty(priceString) && TextUtils.isEmpty(sPhone)
                && TextUtils.isEmpty(sEmail)) {
            return;
        }

        if (pNameString.isEmpty() | priceString.isEmpty() | supplierString.isEmpty()
                | sPhone.isEmpty() | sEmail.isEmpty()) {

            Toast.makeText(this, R.string.warning_save,
                    Toast.LENGTH_SHORT).show();

        } else {

            ContentValues values = new ContentValues();
            values.put(ProductEntry.PRODUCT_NAME, pNameString);
            values.put(ProductEntry.PRICE, priceString);
            values.put(ProductEntry.SUPPLIER, supplierString);
            values.put(ProductEntry.SUPPLIER_PHONE, sPhone);
            values.put(ProductEntry.SUPPLIER_EMAIL, sEmail);
            values.put(ProductEntry.QUANTITY, quantity);

            if (mCurrentProductUri == null) {
                Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.editor_update_product_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:

                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.PRODUCT_NAME,
                ProductEntry.PRICE,
                ProductEntry.QUANTITY,
                ProductEntry.SUPPLIER,
                ProductEntry.SUPPLIER_EMAIL,
                ProductEntry.SUPPLIER_PHONE};

        return new CursorLoader(this,   // Parent activity context
                mCurrentProductUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            int pNameColumnIndex = cursor.getColumnIndex(ProductEntry.PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.QUANTITY);
            int sNameColumnIndex = cursor.getColumnIndex(ProductEntry.SUPPLIER);
            int sPhoneColumnIndex = cursor.getColumnIndex(ProductEntry.SUPPLIER_PHONE);
            int sEmailColumnIndex = cursor.getColumnIndex(ProductEntry.SUPPLIER_EMAIL);


            String pName = cursor.getString(pNameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String sName = cursor.getString(sNameColumnIndex);
            String sPhone = cursor.getString(sPhoneColumnIndex);
            String sEmail = cursor.getString(sEmailColumnIndex);

            mProductNameEditText.setText(pName);
            mPriceEditText.setText(price);
            mQuantityValueTextView.setText(Integer.toString(quantity));
            mSupplierEditText.setText(sName);
            mSupplierPhoneEditText.setText(sPhone);
            mSupplierEmailEditText.setText(sEmail);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mProductNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityValueTextView.setText("");
        mSupplierEditText.setText("");
        mSupplierPhoneEditText.setText("");
        mSupplierEmailEditText.setText("");
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {
        if (mCurrentProductUri != null) {
            int rowDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            if (rowDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}