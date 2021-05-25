package com.example.listasupermercado.model;

import java.util.ArrayList;

public class Category {
    private int categoryID;
    private String categoryName;
    private ArrayList<Product> products;

    public Category(int categoryID, String categoryName, ArrayList<Product> products) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.products = products;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
