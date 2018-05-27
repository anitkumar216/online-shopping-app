package com.example.turbo.yourshop.Model;

/**
 * Created by Vineet Choudhary on 4/9/2018.
 */

public class Order {
    private String ProductId;
    private String ProdctName;
    private String Quantity;
    private String Price;
    private String Discount;

    public Order() {
    }

    public Order(String productId, String prodctName, String quantity, String price, String discount) {
        ProductId = productId;
        ProdctName = prodctName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }


    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProdctName() {
        return ProdctName;
    }

    public void setProdctName(String prodctName) {
        ProdctName = prodctName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
