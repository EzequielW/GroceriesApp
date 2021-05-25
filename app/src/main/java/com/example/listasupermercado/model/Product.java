package com.example.listasupermercado.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String productImageName;
    private String productName;

    public Product(){}

    public Product(String productImageName, String productName){
        this.productImageName = productImageName;
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageName() {
        return productImageName;
    }

    public void setProductImageName(String productImageName) {
        this.productImageName = productImageName;
    }

    protected Product(Parcel in) {
        productImageName = in.readString();
        productName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productImageName);
        dest.writeString(productName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}