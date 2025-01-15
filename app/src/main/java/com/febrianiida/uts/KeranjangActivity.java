package com.febrianiida.uts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class KeranjangActivity extends AppCompatActivity {
    private EditText[] quantityInputs;
    private TextView totalTextView;
    private Button checkoutButton;

    private final int[] prices = {30000, 35000, 35000, 30000, 20000, 25000, 40000}; // Harga masing-masing produk

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        // Mengambil referensi dari layout
        quantityInputs = new EditText[]{
                findViewById(R.id.btnAddAmericano),
                findViewById(R.id.btnAddCoffeeLatte),
                findViewById(R.id.btnAddChocolate),
                findViewById(R.id.btnAddBrownSugar),
                findViewById(R.id.btnAddLemonTea),
                findViewById(R.id.btnAddMangoTea),
                findViewById(R.id.btnAddMatchaLatte)
        };

        totalTextView = findViewById(R.id.totalTextView);
        checkoutButton = findViewById(R.id.checkoutButton);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout();
            }
        });
    }

    // Method untuk menghitung total
    private void calculateTotal() {
        int total = 0;
        for (int i = 0; i < quantityInputs.length; i++) {
            String quantityStr = quantityInputs[i].getText().toString();
            int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);
            total += prices[i] * quantity;
        }
        totalTextView.setText("Total: Rp " + total);
    }

    // Metode untuk menangani checkout
    private void checkout() {
        calculateTotal();
        Intent intent = new Intent(this, InvoiceActivity.class);
        intent.putExtra("TOTAL_AMOUNT", totalTextView.getText().toString());
        startActivity(intent);
    }

    // Metode untuk tombol tambah dan kurang
    public void btnAdd1(View view) {
        updateQuantity(0, 1);
    }

    public void btnMin1(View view) {
        updateQuantity(0, -1);
    }

    public void btnAdd2(View view) {
        updateQuantity(1, 1);
    }

    public void btnMin2(View view) {
        updateQuantity(1, -1);
    }

    public void btnAdd3(View view) {
        updateQuantity(2, 1);
    }

    public void btnMin3(View view) {
        updateQuantity(2, -1);
    }

    public void btnAdd4(View view) {
        updateQuantity(3, 1);
    }

    public void btnMin4(View view) {
        updateQuantity(3, -1);
    }

    public void btnAdd5(View view) {
        updateQuantity(4, 1);
    }

    public void btnMin5(View view) {
        updateQuantity(4, -1);
    }

    public void btnAdd6(View view) {
        updateQuantity(5, 1);
    }

    public void btnMin6(View view) {
        updateQuantity(5, -1);
    }

    public void btnAdd7(View view) {
        updateQuantity(6, 1);
    }

    public void btnMin7(View view) {
        updateQuantity(6, -1);
    }

    private void updateQuantity(int index, int change) {
        EditText quantityInput = quantityInputs[index];
        int currentQuantity = Integer.parseInt(quantityInput.getText().toString());
        currentQuantity = Math.max(0, currentQuantity + change); // Mencegah jumlah negatif
        quantityInput.setText(String.valueOf(currentQuantity));
        calculateTotal();
    }
}
