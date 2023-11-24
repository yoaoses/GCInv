package com.example.inventarioGCAdmin.interfaces;

import java.util.ArrayList;

//sugerencia de chat gpt-4 solucion para metodos asincronos
// interface => listener para elemento esperando carga (equivalente al "await" js)
public interface OnInventariosLoadedListener {
    void onInventariosLoaded(ArrayList<String> inventarios);

    void onError(String errorMessage);
}
