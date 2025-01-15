package com.febrianiida.uts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "aspectcoffee_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "menu_items";

    // Kolom tabel
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_CATEGORY = "category"; // Untuk RadioButton
    private static final String COLUMN_SERVICES = "services"; // Untuk Layanan Tambahan (Checkbox)
    private static final String COLUMN_DATE = "date"; // Untuk DatePicker

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat tabel database
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRICE + " INTEGER, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_SERVICES + " TEXT, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Menambah data
    public long addItem(String name, String description, int price, String imageBase64, String category, String services, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE, imageBase64);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_SERVICES, services); // Menyimpan layanan tambahan (Checkbox)
        values.put(COLUMN_DATE, date);
        return db.insert(TABLE_NAME, null, values);
    }

    // Mendapatkan semua data
    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Mengubah data (hanya kolom yang diisi)
    public int updateItem(int id, String name, String description, Integer price, String imageBase64, String category, String services, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Hanya tambahkan kolom yang memiliki nilai
        if (name != null) values.put(COLUMN_NAME, name);
        if (description != null) values.put(COLUMN_DESCRIPTION, description);
        if (price != null) values.put(COLUMN_PRICE, price);
        if (imageBase64 != null) values.put(COLUMN_IMAGE, imageBase64);
        if (category != null) values.put(COLUMN_CATEGORY, category);
        if (services != null) values.put(COLUMN_SERVICES, services);
        if (date != null) values.put(COLUMN_DATE, date);

        // Lakukan pembaruan berdasarkan ID
        return db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }


    // Menghapus data
    public int deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
