package com.febrianiida.uts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        // Load data dari database
        List<Product> products = getProductsFromDatabase();
        ProductAdapter adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);

        // Tombol Tambah Produk
        findViewById(R.id.addButton).setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, MenuInput.class);
            startActivity(intent);
        });
    }

    private List<Product> getProductsFromDatabase() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllItems();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                String imageBase64 = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                productList.add(new Product(id, name, description, price, imageBase64));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return productList;
    }



    @Override
    protected void onResume() {
        super.onResume();
        List<Product> products = getProductsFromDatabase();
        ProductAdapter adapter = new ProductAdapter(products);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
    }

}
