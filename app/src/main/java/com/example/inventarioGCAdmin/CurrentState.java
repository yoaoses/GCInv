package com.example.inventarioGCAdmin;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public  class CurrentState {

    public static boolean newElement=false;
    private static String TAG="GCInv";
    public static ArrayList<Item> currentItems;
    public static String filterString;
    public static ArrayList<Item> filtered=new ArrayList<>();
    public static ArrayList<String>inventariosDisponibles;
    private static Item selectedItem;

    public CurrentState() {
    }
    public static void newItem(){
        selectedItem=new Item(0,"",0,0);
    }
    public static void setSelected(int selected){
        for (Item element : currentItems) {
            if (element.getBarcode() == selected) {
                selectedItem = element;
                break;
            }
        }
        Log.d(TAG, "setSelected: "+selectedItem.getNombreDescriptivo());
    }
    public static Item getSelectedItem(){
        Log.d(TAG, "getSelectedItem: returns "+selectedItem.getNombreDescriptivo());
        return selectedItem;
    }
    public static void setNewItems(ArrayList<Item> dbItems){
        dbItems.forEach(item -> {
            currentItems.add(item);
        });
    }
    public static void logCurrentList(){

        currentItems.forEach(element->{
            Log.d(TAG, "logCurrentList: "+element.getNombreDescriptivo());
        });
    }

    public static void addOneUnitToStock(){
            selectedItem.setStock(
                    selectedItem.getStock()+1
            );
    }
    public static void subsOneUnitToStock(){
        if(selectedItem.getStock()>0){
            selectedItem.setStock(
                    selectedItem.getStock()-1
            );
        }
    }
    public static void addOneUnitToMinStock(){
        selectedItem.setMinstock(
                selectedItem.getMinstock()+1
        );
    }
    public static void subsOneUnitToMinStock(){
        if(selectedItem.getMinstock()>0){
            selectedItem.setMinstock(
                    selectedItem.getMinstock()-1
            );
        }
    }
    public static void criticalStock(){
        filtered.clear();
        currentItems.forEach(element->{
            if(element.getStock()<element.getMinstock()+1){
                filtered.add(element);
            }
        });
        Log.d(TAG, "criticalStock: "+filtered.size());
    }
    public static void filterBySearch(){
        filtered.clear();
        if(TextUtils.isDigitsOnly(filterString)){
            currentItems.forEach(element->{
                String listValue= String.valueOf(element.getBarcode());
                if(listValue.startsWith(filterString)){
                    filtered.add(element);
                }
            });
        }else{
            currentItems.forEach(element->{
                String listValue= element.getNombreDescriptivo().replaceAll("\\s+","").toLowerCase();
                String filterStringNospaces=filterString.replaceAll("\\s+","").toLowerCase();
                if(listValue.contains(filterStringNospaces)){
                    filtered.add(element);
                }
            });
        }
    }
}
