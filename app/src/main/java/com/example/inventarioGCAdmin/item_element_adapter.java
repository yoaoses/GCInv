package com.example.inventarioGCAdmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;


//tutorial: https://www.youtube.com/watch?v=Uur6-64KqxI
public class item_element_adapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context contexto;
    ArrayList<Item> datos;

    public item_element_adapter(Context contexto, ArrayList<Item> datos) {
        this.contexto = contexto;
        this.datos = datos;
        //iniciacion del inflater=>contexto.LAYOUT_INFLATER_SERVICE : crea instancia del XML
        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //asigno el xml del item
        final View vistaItem=inflater.inflate(R.layout.item_element,null);
        //elementos del item contenidos en vistaItem
        TextView nombre=(TextView) vistaItem.findViewById(R.id.nombreItem);
        TextView stock=(TextView) vistaItem.findViewById(R.id.stock);
        TextView barcode=(TextView) vistaItem.findViewById(R.id.barcode);
        ConstraintLayout fondo= (ConstraintLayout) vistaItem.findViewById(R.id.completeElement);
        Item element=datos.get(position);
        nombre.setText(element.getNombreDescriptivo());
        stock.setText(String.valueOf(element.getStock()));
        barcode.setText(String.valueOf( element.getBarcode()));
        if (element.getStock() == element.getMinstock()) {
            fondo.setBackgroundColor(Color.parseColor("#90FF5722")); // Color naranja pastel
        } else if (element.getStock() < element.getMinstock() - 1) {
            fondo.setBackgroundColor(Color.parseColor("#FFEF3238")); // Color rojo pastel
        } else if (element.getStock() < element.getMinstock() + 2) {
            fondo.setBackgroundColor(Color.parseColor("#FFF4ABAD")); // Color amarillo pastel
        } else {
            fondo.setBackgroundColor(Color.parseColor("#D2F8E4")); // Otro color de fondo
        }
        fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItem(position);
                Intent goDetails= new Intent(contexto, DataDisplay.class);
                contexto.startActivity(goDetails);
            }
        });
        return vistaItem;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public void setItem(int position) {
        CurrentState.setSelected(datos.get(position).getBarcode());
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }


}
