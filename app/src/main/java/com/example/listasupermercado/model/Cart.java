package com.example.listasupermercado.model;

import java.util.ArrayList;
import java.util.Map;

public class Cart {
    private String name;
    private ArrayList<ProductDetail> products;
    private String author;
    private Map<String, String> sharedUsers;

    public Cart(){}

    public Cart(String name, ArrayList<ProductDetail> products, String author, Map<String, String> sharedUsers) {
        this.name = name;
        this.products = products;
        this.author = author;
        this.sharedUsers = sharedUsers;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Map<String, String> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(Map<String, String> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }
}
