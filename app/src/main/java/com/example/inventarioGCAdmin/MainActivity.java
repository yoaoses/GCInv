package com.example.inventarioGCAdmin;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventarioGCAdmin.interfaces.OnInventariosLoadedListener;
import com.example.inventarioGCAdmin.interfaces.OnItemsLoadedListener;

import org.checkerframework.checker.units.qual.Current;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String TAG="GCInv";
    Button criticalStock;
    CurrentState current=new CurrentState();
    DBManager db=new DBManager();
    ListView itemList;
    TextView inventoryText;
    EditText searchInput;
    AutoCompleteTextView inventoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBManager db=new DBManager();
        //uso de interfaz para metodos asincronos
        db.getInventariosDisponibles(new OnInventariosLoadedListener() {
            @Override
            public void onInventariosLoaded(ArrayList<String> inventarios) {
                // inventarios obtenido
                current.inventariosDisponibles=inventarios;
                loadInventories();
            }
            @Override
            public void onError(String errorMessage) {
                // Manejar errores
                Log.d(TAG, "Error al cargar inventarios: " + errorMessage);
            }
        });
        //---------------------------------------
        inventoryText=findViewById(R.id.invText);
        inventoryText.setText("Nada Seleccionado");
        criticalStock=findViewById(R.id.lowStock);
        searchInput=findViewById(R.id.searchInput);
        criticalStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(criticalStock.getText().toString().equals("Stock critico")){
                    criticalStock.setText("Quitar Filtros");
                    searchInput.setText("");
                    current.criticalStock();
                    if(current.filtered.size()>0){
                        buildFilteredList();
                    }else{
                        Toast.makeText(MainActivity.this, "Nada encontrado", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    criticalStock.setText("Stock critico");
                    searchInput.setText("");
                    buildItemsList();
                }
            }
        });
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(searchInput.getText().equals(""))){
                    criticalStock.setText("Quitar Filtros");
                    String input=searchInput.getText().toString();
                    if(input.length()>1){
                        current.filterString=input;
                        current.filterBySearch();
                        if(current.filtered.size()>0){
                            buildFilteredList();
                        }
                    }else{
                        buildItemsList();
                    }
                }
            }
        });
    }

    private void loadInventories(){
        inventoryList=findViewById(R.id.inventories);
        ArrayAdapter<String> invAdapter=new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, current.inventariosDisponibles);
        inventoryList.setAdapter(invAdapter);
        inventoryList.setOnItemClickListener((parent,view,position,id)->{
            db.setInventario((String) parent.getItemAtPosition(position));
            inventoryText.setText(db.getInventario().toString());
            criticalStock.setEnabled(true);
            setUpInventory();
        });
        if((db.getInventario().equals(""))){
            Toast.makeText(this, "Seleccione un inventario para empezar", Toast.LENGTH_SHORT).show();
        }else{
            inventoryText.setText(db.getInventario().toString());
            criticalStock.setEnabled(true);
            setUpInventory();
        }

    }

    private void setUpInventory(){

        //uso de interfaz para metodos asincronos
        db.getAllItems(new OnItemsLoadedListener() {
            @Override
            public void onItemsLoaded(ArrayList<Item> itemList) {
                Log.d(TAG, "onItemsLoaded: "+itemList);
                if (itemList != null) {
                    // Aquí puedes trabajar con la lista de elementos obtenidos
                    current.currentItems=itemList;
                    buildItemsList();
                } else {
                    // Manejar el caso de error o lista nula
                    Log.d(TAG, "Error al cargar elementos");
                }
            }
        });
        //-------------------------------------
    }

    public void buildItemsList(){
        current.logCurrentList();
        itemList=findViewById(R.id.listView_results);
        Log.d(TAG, "buildItemsList: "+current.currentItems.size());
        if((current.currentItems.size()>0)){
            item_element_adapter adapter = new item_element_adapter(MainActivity.this,  current.currentItems);
            itemList.setAdapter(adapter);
        }else{
            ArrayList<String>emptyRecord=new ArrayList<String>();
            emptyRecord.add("Agregar Items");
            ArrayAdapter<String> emptyInvAdapter=new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, emptyRecord);
            itemList.setAdapter(emptyInvAdapter);
            itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(MainActivity.this, DataDisplay.class);
                    current.newElement=true;
                    startActivity(i);
                }
            });
        }
    }
    public void buildFilteredList(){
        current.logCurrentList();
        itemList=findViewById(R.id.listView_results);
        item_element_adapter adapter = new item_element_adapter(MainActivity.this,  current.filtered);
        itemList.setAdapter(adapter);
    }

}