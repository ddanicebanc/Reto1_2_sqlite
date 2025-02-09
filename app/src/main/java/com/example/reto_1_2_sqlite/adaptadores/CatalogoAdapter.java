package com.example.reto_1_2_sqlite.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.modelos.Articulo;

import java.util.ArrayList;

public class CatalogoAdapter extends RecyclerView.Adapter<CatalogoAdapter.ViewHolder> {
    public interface ItemClickListener {
        public void onClick (View view, int position);
    }

    private ArrayList<Articulo> dataSet;
    private CatalogoAdapter.ItemClickListener clickListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txvNombreArticulo,txvCategoria, txvStock, txvPrecio;
        private final ImageView imgArticulo;

        public ViewHolder (View view) {
            super(view);
            view.setOnClickListener(this);

            txvNombreArticulo = view.findViewById(R.id.txvNombreArticulo);
            txvCategoria = view.findViewById(R.id.cat_cat_content);
            txvStock = view.findViewById(R.id.cat_stock_content);
            txvPrecio = view.findViewById(R.id.cat_price_content);
            imgArticulo = view.findViewById(R.id.imgCatalogo);
        }

        public TextView getTxvNombreArticulo () {return txvNombreArticulo;}
        public TextView getTxvCategoria () {return txvCategoria;}
        public TextView getTxvStock () {return txvStock;}
        public TextView getTxvPrecio () {return txvPrecio;}
        public ImageView getImgArticulo () {return imgArticulo;}

        public void onClick (View view) {clickListener.onClick(view, getAdapterPosition());}
    }

    public void setOnClickListener (ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public CatalogoAdapter (ArrayList<Articulo> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_catalogo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (ViewHolder viewHolder, final int position) {
        String nombre = dataSet.get(position).getNombre();
        String categoria = dataSet.get(position).getCategoria();
        String stock = dataSet.get(position).getStock();
        String precio = dataSet.get(position).getPrecio();
        Bitmap imagenArticulo = dataSet.get(position).getImagen();

        viewHolder.getTxvNombreArticulo().setText(nombre);
        viewHolder.getImgArticulo().setImageBitmap(imagenArticulo);
        viewHolder.getTxvCategoria().setText(categoria);
        viewHolder.getTxvStock().setText(stock);
        viewHolder.getTxvPrecio().setText(precio);
    }

    @Override
    public int getItemCount () {return dataSet.size();}
}
