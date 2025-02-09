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
/**
 * {@code LineasAdapter} es un {@link RecyclerView.Adapter} que muestra una lista de objetos {@link LineaPedido}
 * en una vista de líneas de pedido.  Maneja la creación de objetos {@link ViewHolder}, la vinculación de datos a las vistas
 * y la gestión de los clics del usuario en los elementos.
 * <p>
 * Este adaptador utiliza el diseño <code>row_lineas_pedido.xml</code> para cada elemento de la lista.
 */
public class LineasAdapter extends RecyclerView.Adapter<LineasAdapter.ViewHolder> {
    /**
     * Interfaz para que MainActivity o cualquier otra actividad implemente el listener y poder
     * capturar el evento fuera del adaptador.
     */
    public interface ItemClickListener {
        /**
         * Se llama cuando se hace clic en un elemento.
         * @param view La {@link View} en la que se hizo clic.
         * @param position La posición del elemento en el que se hizo clic.
         */
        public void onClick(View view, int position);
    }

    //Aquí podemos pasar cualquier tipo de colección de objetos
    private ArrayList<LineaPedido> dataSet;
    //Lo utilizamos para poder capturar los clics sobre los elementos fuera del adaptador
    private LineasAdapter.ItemClickListener clicListener;
    //Referencia al contexto para poder obtener las imagenes que vamos a pintar
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
        private final TextView txvNumeroLinea;
        private final TextView txvNombreArticulo;
        private final ImageView imgArticulo;
        /**
         * Constructor para el {@code ViewHolder}.
         * @param view La {@link View} que representa el diseño del elemento.
         */
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
        /**
         * Se llama cuando se hace clic en la vista del elemento. Este método notifica al
         * {@link ItemClickListener} del evento de clic.
         * @param view La {@link View} en la que se hizo clic.
         */
        @Override
        public void onClick (View view) {clicListener.onClick(view, getAdapterPosition());}
    }
    /**
     * Constructor para {@code LineasAdapter}.
     * @param dataSet El {@link ArrayList} de objetos {@link LineaPedido} para mostrar.
     * @param context El {@link Context} de la aplicación.
     */
    public LineasAdapter (ArrayList<LineaPedido> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    /**
     * Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen.
     * Crea un nuevo {@code ViewHolder} e infla el diseño del elemento.
     * @param viewGroup El {@link ViewGroup} en el que se agregará la nueva {@link View} después de que se haya vinculado a los datos.
     * @param viewType El tipo de vista de la nueva {@link View}.
     * @return Un nuevo {@code ViewHolder} que contiene una {@link View} del diseño del elemento.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_lineas_pedido, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen.
     * Vincula los datos a las vistas dentro del {@code ViewHolder}.
     * @param viewHolder El {@code ViewHolder} al que se van a vincular los datos.
     * @param position La posición del elemento dentro del conjunto de datos.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        int numeroLinea = dataSet.get(position).getNumeroLinea();
        String nombreArticulo = dataSet.get(position).getNombreArticulo();
        Bitmap imagenArticulo = dataSet.get(position).getImage();

        viewHolder.getTxvNumeroLinea().setText(String.valueOf(numeroLinea));
        viewHolder.getTxvNombreArticulo().setText(nombreArticulo);
        viewHolder.getImgArticulo().setImageBitmap(imagenArticulo);
    }

    /**
     * Devuelve el número de elementos de nuestro ArrayList, lo invoca automáticamente el LayoutManager.
     * @return El número de elementos en el conjunto de datos.
     */
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
