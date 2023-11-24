package com.example.inventarioGCAdmin;

import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

public class Item {
    private String TAG= "GCInv";
    private int barcode;
    private String nombreDescriptivo;
    private int stock;
    private int minstock;

    public Item(int barcode, String nombreDescriptivo, int stock, int minstock) {
        Log.d(TAG, "Item: "+barcode+", "+nombreDescriptivo+", "+stock+" "+minstock);
        this.setBarcode(barcode);
        this.setNombreDescriptivo(nombreDescriptivo);
        this.setStock(stock);
        this.setMinstock(minstock);
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getNombreDescriptivo() {
        return nombreDescriptivo;
    }

    public void setNombreDescriptivo(String nombreDescriptivo) {
        this.nombreDescriptivo = nombreDescriptivo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMinstock() {
        return minstock;
    }

    public void setMinstock(int minstock) {
        this.minstock = minstock;
    }

}
