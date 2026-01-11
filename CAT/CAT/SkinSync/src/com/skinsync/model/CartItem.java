package com.skinsync.model;

public class CartItem {
    // Ciri-ciri barang dalam cart (Mesti sama dengan data dari JS tadi)
    private String id;
    private String name;
    private double price;
    private int qty;
    private String image;

    // 1. Constructor Kosong (Wajib ada untuk standard Java)
    public CartItem() {
    }

    // 2. Constructor Penuh (Senang nak create objek baru)
    public CartItem(String id, String name, double price, int qty, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.image = image;
    }

    // 3. Getters and Setters (Encapsulation - Syarat OOP)
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Optional: Method untuk kira total harga item ini (Price * Qty)
    public double getTotalPrice() {
        return this.price * this.qty;
    }
}