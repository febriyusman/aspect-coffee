package com.febrianiida.uts;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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

public class MenuInput extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    private DatabaseHelper databaseHelper;
    private EditText nameEditText, descriptionEditText, priceEditText;
    private RadioGroup categoryRadioGroup;
    private CheckBox checkboxNew, checkboxRecommended;
    private Button datePickerButton, selectImageButton, saveButton;
    private TextView dateTextView;
    private ImageView selectedImageView;

    private String selectedDate = "";
    private String encodedImage = ""; // Untuk menyimpan gambar dalam format Base64

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_input);

        // Inisialisasi DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Inisialisasi Views
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        categoryRadioGroup = findViewById(R.id.categoryRadioGroup);
        checkboxNew = findViewById(R.id.checkboxNew);
        checkboxRecommended = findViewById(R.id.checkboxRecommended);
        datePickerButton = findViewById(R.id.datePickerButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveButton = findViewById(R.id.saveButton);
        dateTextView = findViewById(R.id.dateTextView);
        selectedImageView = findViewById(R.id.selectedImageView);

        // Pilih tanggal
        datePickerButton.setOnClickListener(v -> showDatePicker());

        // Pilih gambar
        selectImageButton.setOnClickListener(v -> openImageSelector());

        // Simpan data
        saveButton.setOnClickListener(v -> saveData());
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


    private void saveData() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceText = priceEditText.getText().toString().trim();
        int selectedCategoryId = categoryRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedCategory = findViewById(selectedCategoryId);

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || selectedCategory == null || selectedDate.isEmpty() || encodedImage.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua data!", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceText);
        String category = selectedCategory.getText().toString();
        StringBuilder services = new StringBuilder();
        if (checkboxNew.isChecked()) services.append("Produk Baru");
        if (checkboxRecommended.isChecked()) {
            if (services.length() > 0) services.append(", ");
            services.append("Rekomendasi");
        }

       long result = databaseHelper.addItem(name, description, price, encodedImage, category, services.toString(), selectedDate);


        if (result != -1) {
            Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal menyimpan data.", Toast.LENGTH_SHORT).show();
        }
    }
}
