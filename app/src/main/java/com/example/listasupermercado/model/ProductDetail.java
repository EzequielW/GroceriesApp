package com.example.listasupermercado.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.Input;

public class ProductDetail implements Parcelable, KryoSerializable {
    private Product product;
    private String productUnit;
    private int productQuantity;

    public ProductDetail(){}

    public ProductDetail(Product product, String productUnit, int productQuantity){
        this.product = product;
        this.productUnit = productUnit;
        this.productQuantity = productQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }


    /*
        Parcelable methods
     */

    protected ProductDetail(Parcel in) {
        product = (Product) in.readValue(Product.class.getClassLoader());
        productUnit = in.readString();
        productQuantity = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(product);
        dest.writeString(productUnit);
        dest.writeInt(productQuantity);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductDetail> CREATOR = new Parcelable.Creator<ProductDetail>() {
        @Override
        public ProductDetail createFromParcel(Parcel in) {
            return new ProductDetail(in);
        }

        @Override
        public ProductDetail[] newArray(int size) {
            return new ProductDetail[size];
        }
    };

    /*
        KryoSerializable methods
     */
    public void write(Kryo kryo, Output output){
        output.writeInt(this.productQuantity);
        output.writeString(this.productUnit);
        kryo.writeClassAndObject(output, this.product);
    }

    public void read(Kryo kryo, Input input){
        this.productQuantity = input.readInt();
        this.productUnit = input.readString();
        this.product = (Product) kryo.readClassAndObject(input);
    }
}