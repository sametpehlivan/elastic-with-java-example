package com.sametp.els.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ProductDto {
    @JsonIgnore
    private String id;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("price")
    private double price;
    public ProductDto(){}
    public ProductDto(String id, String productName,double price) {
        this.id = id;
        this.price = price;
        this.productName = productName;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                '}';
    }
}
