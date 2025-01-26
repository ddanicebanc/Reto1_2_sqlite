package com.example.reto_1_2_sqlite.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.modelos.LineaPedido;

import java.util.ArrayList;

public class LineasAdapter extends RecyclerView.Adapter<LineasAdapter.ViewHolder> {
    //Interface para que MainActivity o cualquier otra actividad implemente el listener y poder
    // capturar el evento fuera del adaptador
    public interface ItemClickListener {
        public void onClick(View view, int position);
    }

    //Aquí podemos pasar cualquier tipo de colección de objetos
    private ArrayList<LineaPedido> dataSet;
    //Lo utilizamos para poder capturar los clics sobre los elementos fuera del adaptador
    private LineasAdapter.ItemClickListener clicListener;
    //Referencia al contexto para poder obtener las imagenes que vamos a pintar
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txvNumeroLinea;
        private final TextView txvNombreArticulo;
        private final ImageView imgArticulo;

        public ViewHolder (View view) {
            super(view);
            view.setOnClickListener(this);

            txvNumeroLinea = view.findViewById(R.id.txvNumeroLinea);
            txvNombreArticulo = view.findViewById(R.id.txvNombreArticulo);
            imgArticulo = view.findViewById(R.id.imgLinea);
        }

        public TextView getTxvNumeroLinea () {return txvNumeroLinea;}
        public TextView getTxvNombreArticulo () {return txvNombreArticulo;}
        public ImageView getImgArticulo () {return imgArticulo;}

        @Override
        public void onClick (View view) {clicListener.onClick(view, getAdapterPosition());}
    }

    public LineasAdapter (ArrayList<LineaPedido> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_lineas_pedido, viewGroup, false);

        return new ViewHolder(view);
    }

    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        int numeroLinea = dataSet.get(position).getNumeroLinea();
        String nombreArticulo = dataSet.get(position).getNombreArticulo();
        Bitmap imagenArticulo = dataSet.get(position).getImage();

        viewHolder.getTxvNumeroLinea().setText(String.valueOf(numeroLinea));
        viewHolder.getTxvNombreArticulo().setText(nombreArticulo);
        viewHolder.getImgArticulo().setImageBitmap(imagenArticulo);
    }

    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
