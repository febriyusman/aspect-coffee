package com.febrianiida.uts;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    private DatabaseHelper databaseHelper;
    private EditText nameEditText, descriptionEditText, priceEditText;
    private RadioGroup categoryRadioGroup;
    private CheckBox checkboxNew, checkboxRecommended;
    private Button datePickerButton, selectImageButton, updateButton;
    private TextView dateTextView;
    private ImageView selectedImageView;

    private String selectedDate = "";
    private String encodedImage = "";
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        databaseHelper = new DatabaseHelper(this);

        // Inisialisasi views
        nameEditText = findViewById(R.id.editNameEditText);
        descriptionEditText = findViewById(R.id.editDescriptionEditText);
        priceEditText = findViewById(R.id.editPriceEditText);
        categoryRadioGroup = findViewById(R.id.editCategoryRadioGroup);
        checkboxNew = findViewById(R.id.editCheckboxNew);
        checkboxRecommended = findViewById(R.id.editCheckboxRecommended);
        datePickerButton = findViewById(R.id.editDatePickerButton);
        selectImageButton = findViewById(R.id.editSelectImageButton);
        updateButton = findViewById(R.id.updateButton);
        dateTextView = findViewById(R.id.editDateTextView);
        selectedImageView = findViewById(R.id.editSelectedImageView);

        // Ambil ID produk dari Intent
        productId = getIntent().getIntExtra("product_id", -1);

        // Load data produk berdasarkan ID
        loadProductData(productId);

        // Listener untuk memilih tanggal
        datePickerButton.setOnClickListener(v -> showDatePicker());

        // Listener untuk memilih gambar
        selectImageButton.setOnClickListener(v -> openImageSelector());

        // Listener untuk update data
        updateButton.setOnClickListener(v -> updateProductData());
    }

    private void loadProductData(int id) {
        Cursor cursor = databaseHelper.getAllItems(); // Ganti dengan query spesifik berdasarkan ID
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndexOrThrow("id")) == id) {
                    nameEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    descriptionEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                    priceEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                    selectedDate = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    dateTextView.setText(selectedDate);

                    // Pilihan kategori
                    String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                    if (category.equals("Minuman Panas")) categoryRadioGroup.check(R.id.editRadioHotDrink);
                    else if (category.equals("Minuman Dingin")) categoryRadioGroup.check(R.id.editRadioColdDrink);
                    else if (category.equals("Camilan")) categoryRadioGroup.check(R.id.editRadioSnack);
                    else if (category.equals("Makanan Berat")) categoryRadioGroup.check(R.id.editRadioFood);

                    // Checkbox fitur produk
                    String services = cursor.getString(cursor.getColumnIndexOrThrow("services"));
                    checkboxNew.setChecked(services.contains("Produk Baru"));
                    checkboxRecommended.setChecked(services.contains("Rekomendasi"));

                    // Gambar produk
                    // Ambil data gambar dalam Base64
                    String imageBase64 = cursor.getString(cursor.getColumnIndexOrThrow("image"));

                    // Cek jika kolom gambar tidak kosong dan valid
                    if (imageBase64 != null && !imageBase64.isEmpty()) {
                        try {
                            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            selectedImageView.setImageBitmap(decodedByte);
                            encodedImage = imageBase64; // Simpan gambar yang valid
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Gambar dalam database tidak valid!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Jika kolom gambar kosong, gunakan gambar default
                        selectedImageView.setImageResource(R.drawable.aspect); // Ganti dengan gambar default
                    }

                }
            }
            cursor.close();
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
            dateTextView.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                selectedImageView.setImageBitmap(bitmap);
                encodeImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    private void updateProductData() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceText = priceEditText.getText().toString().trim();
        int selectedCategoryId = categoryRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedCategory = findViewById(selectedCategoryId);

        // Cek nilai-nilai opsional
        String category = selectedCategory != null ? selectedCategory.getText().toString() : null;
        StringBuilder services = new StringBuilder();
        if (checkboxNew.isChecked()) services.append("Produk Baru");
        if (checkboxRecommended.isChecked()) {
            if (services.length() > 0) services.append(", ");
            services.append("Rekomendasi");
        }
        String servicesValue = services.length() > 0 ? services.toString() : null;

        // Konversi price menjadi Integer jika ada
        Integer price = priceText.isEmpty() ? null : Integer.parseInt(priceText);

        // Panggil updateItem dengan nilai yang relevan
        int result = databaseHelper.updateItem(
                productId,
                name.isEmpty() ? null : name,
                description.isEmpty() ? null : description,
                price,
                encodedImage.isEmpty() ? null : encodedImage, // Simpan gambar Base64, bukan placeholder
                category,
                servicesValue,
                selectedDate.isEmpty() ? null : selectedDate
        );

        if (result > 0) {
            Toast.makeText(this, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal memperbarui data.", Toast.LENGTH_SHORT).show();
        }
    }

}
