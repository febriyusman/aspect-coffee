package com.febrianiida.uts;

import android.widget.Button;

public class Product {
    private int id;
    private String name;
    private String description;
    private int price;
    private String imageBase64;
    private Button editButton;
    private Button deleteButton;


    public Product(int id, String name, String description, int price, String imageBase64) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageBase64 = imageBase64;
        this.editButton = editButton;
        this.deleteButton = deleteButton;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public Button getEditButton() {return editButton;}
    public Button getDeleteButton() {return deleteButton;}
    public String getImageBase64() {return imageBase64;}
}
