package com.example.reto_1_2_sqlite.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.modelos.Visita;

import java.util.ArrayList;

/**
 * {@code VisitasAdapter} es un {@link RecyclerView.Adapter} que muestra una lista de objetos {@link Visita}
 * en una vista de visitas. Maneja la creación de objetos {@link ViewHolder}, la vinculación de datos a las vistas
 * y la gestión de los clics del usuario en los elementos.
 * <p>
 * Este adaptador utiliza el diseño <code>row_visitas.xml</code> para cada elemento de la lista.
 */
public class VisitasAdapter extends RecyclerView.Adapter<VisitasAdapter.ViewHolder> {

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

    private ArrayList<Visita> dataSet;
    private ItemClickListener clicListener;
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
        private final TextView txvPartnerName;
        private final TextView txvVisitDate;
        private final TextView txvDireccion;

        /**
         * Constructor para el {@code ViewHolder}.
         * @param view La {@link View} que representa el diseño del elemento.
         */
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            txvPartnerName = view.findViewById(R.id.txvPartnerName);
            txvVisitDate = view.findViewById(R.id.txvVisitDate);
            txvDireccion = view.findViewById(R.id.txvDireccion);
        }

        /**
         * Devuelve el {@link TextView} que muestra el nombre del socio.
         * @return El {@link TextView} que muestra el nombre del socio.
         */
        public TextView getTxvPartnerName() {
            return txvPartnerName;
        }

        /**
         * Devuelve el {@link TextView} que muestra la fecha de la visita.
         * @return El {@link TextView} que muestra la fecha de la visita.
         */
        public TextView getTxvVisitDate() {
            return txvVisitDate;
        }

        /**
         * Devuelve el {@link TextView} que muestra la dirección.
         * @return El {@link TextView} que muestra la dirección.
         */
        public TextView getTxvDireccion() {
            return txvDireccion;
        }

        /**
         * Se llama cuando se hace clic en la vista del elemento. Este método notifica al
         * {@link ItemClickListener} del evento de clic.
         * @param view La {@link View} en la que se hizo clic.
         */
        @Override
        public void onClick(View view) {
            if (clicListener != null) { // Check for null to avoid exception
                clicListener.onClick(view, getAdapterPosition());
            }
        }
    }

    /**
     * Constructor para {@code VisitasAdapter}.
     * @param dataSet El {@link ArrayList} de objetos {@link Visita} para mostrar.
     * @param context El {@link Context} de la aplicación.
     */
    public VisitasAdapter(ArrayList<Visita> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    /**
     * Establece el {@link ItemClickListener} que se notificará de los eventos de clic en los elementos.
     * @param clicListener El {@link ItemClickListener} que se va a establecer.
     */
    public void setOnClickListener(ItemClickListener clicListener) {
        this.clicListener = clicListener;
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
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_visitas, viewGroup, false);

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
        Visita visita = dataSet.get(position); // Get the Visita object

        String partnerName = visita.getPartnerName();
        String fechaVisita = visita.getFechaVisita();
        String direccion = visita.getDireccion();

        viewHolder.getTxvPartnerName().setText(partnerName);
        viewHolder.getTxvVisitDate().setText(fechaVisita);
        viewHolder.getTxvDireccion().setText(direccion);
    }

    /**
     * Devuelve el número de elementos en el conjunto de datos.
     * @return El número de elementos en el conjunto de datos.
     */
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}