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

/**
 * {@code CatalogoAdapter} es un {@link RecyclerView.Adapter} que muestra una lista de objetos {@link Articulo}
 * en una vista de catálogo. Maneja la creación de objetos {@link ViewHolder}, la vinculación de datos a las vistas
 * y la gestión de los clics del usuario en los elementos.
 * <p>
 * Este adaptador utiliza el diseño <code>row_catalogo.xml</code> para cada elemento de la lista.
 */
public class CatalogoAdapter extends RecyclerView.Adapter<CatalogoAdapter.ViewHolder> {
    /**
     * Definición de interfaz para una devolución de llamada que se invocará cuando se haga clic en un elemento del catálogo.
     */
    public interface ItemClickListener {
        /**
         * Se llama cuando se hace clic en un elemento del catálogo.
         *
         * @param view     La {@link View} en la que se hizo clic.
         * @param position La posición del elemento en el que se hizo clic en el adaptador.
         */
        public void onClick (View view, int position);
    }

    private ArrayList<Articulo> dataSet;
    private CatalogoAdapter.ItemClickListener clickListener;
    private Context context;
    /**
     * {@code ViewHolder} describe una vista de elemento y metadatos sobre su lugar dentro del
     * {@link RecyclerView}.
     * <p>
     * El {@code ViewHolder} también contiene referencias a las vistas dentro del diseño del elemento,
     * lo que reduce la necesidad de llamar repetidamente a {@link View#findViewById(int)} durante la vinculación.
     * También implementa {@link View.OnClickListener} para manejar los clics en los elementos.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txvNombreArticulo,txvCategoria, txvStock, txvPrecio;
        private final ImageView imgArticulo;
        /**
         * Constructor para el {@code ViewHolder}.
         *
         * @param view La {@link View} que representa el diseño del elemento.
         */
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
        /**
         * Se llama cuando se hace clic en la vista del elemento. Este método notifica al
         * {@link ItemClickListener} del evento de clic.
         *
         * @param view La {@link View} en la que se hizo clic.
         */
        public void onClick (View view) {clickListener.onClick(view, getAdapterPosition());}
    }
    /**
     * Establece el {@link ItemClickListener} que se notificará de los eventos de clic en los elementos.
     *
     * @param clickListener El {@link ItemClickListener} que se va a establecer.
     */
    public void setOnClickListener (ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
    /**
     * Constructor para {@code CatalogoAdapter}.
     *
     * @param dataSet El {@link ArrayList} de objetos {@link Articulo} para mostrar.
     * @param context El {@link Context} de la aplicación.
     */
    public CatalogoAdapter (ArrayList<Articulo> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }
    /**
     * Crea un nuevo {@code ViewHolder} e infla el diseño del elemento.
     *
     * @param parent   El {@link ViewGroup} en el que se agregará la nueva {@link View} después de que se haya vinculado a los datos.
     * @param viewType El tipo de vista de la nueva {@link View}.
     * @return Un nuevo {@code ViewHolder} que contiene una {@link View} del diseño del elemento.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_catalogo, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Vincula los datos a las vistas dentro del {@code ViewHolder}.
     *
     * @param viewHolder El {@code ViewHolder} al que se van a vincular los datos.
     * @param position   La posición del elemento dentro del conjunto de datos.
     */
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
    /**
     * Returns the total number of items in the data set.
     * @return The total number of items in the data set.
     */
    @Override
    public int getItemCount () {return dataSet.size();}
}
