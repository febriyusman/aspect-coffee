package com.febrianiida.uts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InvoiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice); // Pastikan Anda memiliki layout activity_invoice.xml

        // Inisialisasi komponen UI
        ImageView logoImageView = findViewById(R.id.logoImageView);
        TextView totalPaymentTV = findViewById(R.id.totalPaymentTV);
        EditText namaEditText = findViewById(R.id.namaEditText);
        EditText nomorHpEditText = findViewById(R.id.nomorHpEditText);
        RadioGroup metodePembayaranRadioGroup = findViewById(R.id.metodePembayaranRadioGroup);
        Button pesanButton = findViewById(R.id.pesanButton);

        // Ambil total amount dari intent
        String totalAmount = getIntent().getStringExtra("TOTAL_AMOUNT");
        if (totalAmount != null) {
            totalPaymentTV.setText(totalAmount);
        } else {
            totalPaymentTV.setText("Total: Rp 0");
        }

        // Set listener untuk tombol pesan
        pesanButton.setOnClickListener(v -> {
            String nama = namaEditText.getText().toString();
            String nomorHp = nomorHpEditText.getText().toString();

            int selectedPaymentMethodId = metodePembayaranRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedPaymentMethod = findViewById(selectedPaymentMethodId);
            String metodePembayaran = selectedPaymentMethod != null ? selectedPaymentMethod.getText().toString() : "Tidak dipilih";

            // Lakukan proses selanjutnya, misalnya menyimpan data atau menampilkan ringkasan
            // Tambahkan logika yang sesuai di sini
        });
    }
}
