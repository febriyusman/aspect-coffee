package com.febrianiida.uts;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



//        List<Product> productList = new ArrayList<>();
//        productList.add(new Product("Americano", "Kopi hitam yang khas", 30000, R.drawable.americano));
//        productList.add(new Product("Coffee Latte", "Espresso dengan susu panas", 35000, R.drawable.coffeelatte));
//        productList.add(new Product("Chocolate", "Cokelat manis dan creamy", 35000, R.drawable.chocolate));
//        productList.add(new Product("Lemon Tea", "Teh dengan perasan lemon", 20000, R.drawable.machalate));
//        productList.add(new Product("Manggo Tea", "Teh dengan perasan lemon bercampur mangga", 20000, R.drawable.manggotea));
//        productList.add(new Product("Lechy Tea", "Teh dengan perasan lemon dengan campuran lemon", 20000, R.drawable.lemontea));
//
//
//        ProductAdapter adapter = new ProductAdapter(productList);
//        recyclerView.setAdapter(adapter);
    }
}
