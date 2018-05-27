package com.example.turbo.yourshop.Model;

import java.util.List;

/**
 * Created by Vineet Choudhary on 4/12/2018.
 */

public class Request {

    private String phone;
    private String name;
    private String address;
    private String total;
    private String status;
    private List<Order> item;

    public Request() {
    }

    public Request(String phone, String name, String address, String total, List<Order> item) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = "0";
        this.item = item;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getItem() {
        return item;
    }

    public void setItem(List<Order> item) {
        this.item = item;
    }
}
