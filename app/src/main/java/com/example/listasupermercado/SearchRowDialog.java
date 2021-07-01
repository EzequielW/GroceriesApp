package com.example.listasupermercado;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.listasupermercado.model.Product;
import com.example.listasupermercado.model.ProductDetail;

import java.util.ArrayList;

public class SearchRowDialog {

    public void showDialog(final Context context, final Product product){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.search_dialog);

        // Set title
        TextView title = dialog.findViewById(R.id.search_dialog_title);
        title.setText(product.getProductName());

        // Adjust width of the dialog to 6/7 of the screen
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width)/7, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Set up spinner unit options
        ArrayList<String> unitList = new ArrayList<>();
        unitList.add("Kg");
        unitList.add("g");
        unitList.add("L");
        unitList.add("Ml");
        unitList.add("u");

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, unitList);
        final Spinner unitSpinner = dialog.findViewById(R.id.detail_spinner);
        unitSpinner.setAdapter(unitAdapter);

        Button cancelButton = dialog.findViewById(R.id.detail_cancel);
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        Button addItemButton = dialog.findViewById(R.id.detail_add_item);
        addItemButton.setOnClickListener( v -> {
            dialog.dismiss();
            // Get quantity field value
            EditText quantityField = dialog.findViewById(R.id.detail_quantity);
            int quantity = Integer.parseInt(quantityField.getText().toString());

            ProductDetail productDetail = new ProductDetail(product, (String)unitSpinner.getSelectedItem(), quantity);
            Intent intent = new Intent();
            intent.putExtra("selectedProduct", productDetail);
            ((Activity)(context)).setResult(Activity.RESULT_OK, intent);
            ((Activity)(context)).finish();
        });

        dialog.show();

    }
}
