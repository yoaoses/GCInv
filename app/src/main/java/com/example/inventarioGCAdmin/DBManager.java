package com.example.inventarioGCAdmin;

import android.util.Log;

import com.example.inventarioGCAdmin.interfaces.OnDataSavedListener;
import com.example.inventarioGCAdmin.interfaces.OnInventariosLoadedListener;
import com.example.inventarioGCAdmin.interfaces.OnItemsLoadedListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBManager {
    String TAG = "GCInv";
    private FirebaseFirestore db;

    private static String inventario="";

    public DBManager() {
        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();
        Log.d(TAG, "conectado=>"+inventario);
    }

    public String getInventario() {
        return this.inventario;
    }

    public void setInventario(String inventario) {
        this.inventario = inventario;
        Log.d(TAG, "Seleccionado" + this.inventario);
    }

    public void getInventariosDisponibles(OnInventariosLoadedListener listener) {
        ArrayList<String> inventarios = new ArrayList<>();
        db.collection("Inventarios")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        inventarios.add(document.getId());
                    }
                    listener.onInventariosLoaded(inventarios);
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }


    // eliminar un documento de invetario
    public boolean removeItem(int barcode) {
        try {
            db.collection(inventario)
                    .document(String.valueOf(barcode)) // Reemplaza "TuDocumentoPreKinder" con el ID real del documento
                    .delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getAllItems(OnItemsLoadedListener listener) {
        Log.d(TAG, "getAllItems de:" + this.getInventario() + "....");
        ArrayList<Item> itemList = new ArrayList<>();
        db.collection(this.getInventario())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        int barcode = Integer.parseInt(document.getId());
                        String nombreDescriptivo = document.getString("nombreDescriptivo");
                        int stock = document.getLong("stock").intValue();
                        int minstock = document.getLong("minstock").intValue();
                        Item item = new Item(barcode, nombreDescriptivo, stock, minstock);
                        Log.d(TAG, "Item: " + item.getNombreDescriptivo());
                        itemList.add(item);
                    }
                    listener.onItemsLoaded(itemList); // Llamada a la interfaz cuando se hayan cargado los elementos
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "getAllItems, error: " + e.toString());
                    listener.onItemsLoaded(null); // Llamada a la interfaz con null en caso de error
                });
    }

    public void setItem(Item newItem, OnDataSavedListener listener) {
        try {
            Map<String, Object> newItemMap = new HashMap<>();
            Log.d(TAG, "setItem: documentId=>"+getInventario()+" / "+newItem.getBarcode());
            newItemMap.put("nombreDescriptivo", newItem.getNombreDescriptivo());
            Log.d(TAG, "setItem: nombre=> "+ newItem.getNombreDescriptivo());
            newItemMap.put("stock", newItem.getStock());
            Log.d(TAG, "setItem: stock=>"+ newItem.getStock());
            newItemMap.put("minstock", newItem.getMinstock());
            Log.d(TAG, "setItem: minstock=>"+ newItem.getMinstock());

            db.collection(getInventario())
                    .document(String.valueOf(newItem.getBarcode()))
                    .set(newItemMap)
                    .addOnSuccessListener(aVoid -> {
                        // llama al listener con true
                        listener.onInsertComplete(true);
                    })
                    .addOnFailureListener(e -> {
                        // llama al listener con false
                        listener.onInsertComplete(false);
                    });
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de excepciones, llama al listener con false
            listener.onInsertComplete(false);
        }
    }

}

