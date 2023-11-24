package com.example.inventarioGCAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventarioGCAdmin.interfaces.OnDataSavedListener;

import org.checkerframework.checker.units.qual.Current;

public class DataDisplay extends AppCompatActivity {

    private String TAG="GCInv";
    EditText itemName,barcode,stock,minStock;
    Button addStock,addMinStock,subsMinStock,subsStock,delRecord,saveData,newItem,goLanding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        stock=findViewById(R.id.stockInput);
        itemName=findViewById(R.id.itemName);
        addStock=findViewById(R.id.addStock);
        addMinStock=findViewById(R.id.addMinStock);
        subsStock=findViewById(R.id.subsStock);
        subsMinStock=findViewById(R.id.subsMinStock);
        barcode=findViewById(R.id.barcodeInput);
        minStock=findViewById(R.id.minStockInput);
        delRecord=findViewById(R.id.delRecord);
        saveData=findViewById(R.id.saveChanges);
        newItem=findViewById(R.id.newItem);
        goLanding=findViewById(R.id.returnToLanding);
        stock.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                stock.post(() -> {
                    stock.setSelection(0, stock.getText().length());
                });
            }
        });
        minStock.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                minStock.post(() -> {
                    minStock.setSelection(0, minStock.getText().length());
                });
            }
        });
        loadSelectedData();
        addInputButtonsListeners();
        setDBRelatedButtonListeners();

        //goLanding.findFocus();
    }
    public void addInputButtonsListeners(){

        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentState.addOneUnitToStock();
                updateStock();
            }
        });

        subsStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentState.subsOneUnitToStock();
                updateStock();
            }
        });

        addMinStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentState.addOneUnitToMinStock();
                updateMinStock();
            }
        });

        subsMinStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentState.subsOneUnitToMinStock();
                updateMinStock();
            }
        });

        newItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItem();
            }
        });

        goLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLanding= new Intent(DataDisplay.this, MainActivity.class);
                startActivity(goLanding);
            }
        });
    }
    public void setDBRelatedButtonListeners(){
        delRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkErrors();
                if(checkErrors()){
                    Item saveThis=
                            new Item(
                                Integer.parseInt(String.valueOf(barcode.getText())),
                                itemName.getText().toString(),
                                Integer.parseInt(String.valueOf(stock.getText())),
                                Integer.parseInt(String.valueOf(minStock.getText()))
                            );
                    DBManager db=new DBManager();
                    db.setItem(saveThis, new OnDataSavedListener() {
                        @Override
                        public void onInsertComplete(boolean isSuccess) {
                            //manejo eventos segun isSuccess, el true o false lo retorna la interfaz no la funcion de db
                            if(isSuccess){
                                Toast.makeText(DataDisplay.this, "Saved", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DataDisplay.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    private boolean checkErrors(){
        int errors=0;
        if(String.valueOf(barcode.getText()).equals("")){
            errors++;
        } else if (itemName.getText().toString().equals("")) {
            errors++;
        } else if (String.valueOf(minStock.getText()).equals("")) {
            errors++;
        } else if(String.valueOf(stock.getText()).equals("")) {
            errors++;
        }

        if(errors==0){
            return true;
        }else{
            Toast.makeText(DataDisplay.this, "Errores encontrados:"+errors , Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private void updateStock(){
        Item updated=CurrentState.getSelectedItem();
        stock.setText(String.valueOf(updated.getStock()));
    }
    private void updateMinStock(){
        Item updated=CurrentState.getSelectedItem();
        minStock.setText(String.valueOf(updated.getMinstock()));
    }
    public void newItem(){
        addMinStock=findViewById(R.id.addMinStock);
        subsMinStock=findViewById(R.id.subsMinStock);
        addMinStock.setEnabled(true);
        subsMinStock.setEnabled(true);
        barcode.setEnabled(true);
        barcode.setText("");
        minStock.setEnabled(true);
        minStock.setText("0");
        stock.setEnabled(true);
        stock.setText("0");
    }
    public void loadSelectedData(){
        Log.d(TAG, "loadSelectedData: "+CurrentState.newElement);
        if(!CurrentState.newElement){
            Item selectedItem=CurrentState.getSelectedItem();
            Log.d(TAG, "loadSelectedData: "+selectedItem.getNombreDescriptivo());
            itemName.setText(selectedItem.getNombreDescriptivo());
            barcode.setText(String.valueOf(selectedItem.getBarcode()));
            stock.setText(String.valueOf(selectedItem.getStock()));
            minStock.setText(String.valueOf(selectedItem.getMinstock()));
        }else{
            newItem();
            itemName.requestFocus();
            CurrentState.newItem();
        }
    }
}