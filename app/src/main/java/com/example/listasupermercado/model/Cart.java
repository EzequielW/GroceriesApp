package com.example.listasupermercado.model;

import java.util.ArrayList;

public class Cart {
    private String name;
    private ArrayList<ProductDetail> products;

    public Cart(String name, ArrayList<ProductDetail> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ProductDetail> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductDetail> products) {
        this.products = products;
    }
}
