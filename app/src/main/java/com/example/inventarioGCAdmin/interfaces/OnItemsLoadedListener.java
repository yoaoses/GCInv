package com.example.inventarioGCAdmin.interfaces;

import com.example.inventarioGCAdmin.Item;

import java.util.ArrayList;

//sugerencia de chat gpt-4 solucion para metodos asincronos
// interface => listener para elemento esperando carga (equivalente al "await" js)
public interface OnItemsLoadedListener {
    void onItemsLoaded(ArrayList<Item> itemList);
}

