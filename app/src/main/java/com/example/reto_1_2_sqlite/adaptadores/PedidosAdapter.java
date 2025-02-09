package com.example.reto_1_2_sqlite.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.modelos.CabeceraPedido;

import java.util.ArrayList;

/**
 * {@code PedidosAdapter} es un {@link RecyclerView.Adapter} que muestra una lista de objetos {@link CabeceraPedido}
 * en una vista de pedidos. Maneja la creación de objetos {@link ViewHolder}, la vinculación de datos a las vistas
 * y la gestión de los clics del usuario en los elementos.
 * <p>
 * Este adaptador utiliza el diseño <code>row_cabeceras_pedido.xml</code> para cada elemento de la lista.
 */
public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolder> {

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

    private ArrayList<CabeceraPedido> dataSet;
    private ItemClickListener clickListener;
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
        private final TextView txvNumeroPedido;
        private final TextView txvFechaPedido;
        private final TextView txvFechaPago;

        /**
         * Constructor para el {@code ViewHolder}.
         * @param view La {@link View} que representa el diseño del elemento.
         */
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            txvNumeroPedido = view.findViewById(R.id.txvNumeroPedido);
            txvFechaPedido = view.findViewById(R.id.txvFechaPedido);
            txvFechaPago = view.findViewById(R.id.txvFechaPago);
        }

        /**
         * Devuelve el {@link TextView} que muestra el número de pedido.
         * @return El {@link TextView} que muestra el número de pedido.
         */
        public TextView getTxvNumeroPedido() {
            return txvNumeroPedido;
        }

        /**
         * Devuelve el {@link TextView} que muestra la fecha del pedido.
         * @return El {@link TextView} que muestra la fecha del pedido.
         */
        public TextView getTxvFechaPedido() {
            return txvFechaPedido;
        }

        /**
         * Devuelve el {@link TextView} que muestra la fecha de pago.
         * @return El {@link TextView} que muestra la fecha de pago.
         */
        public TextView getTxvFechaPago() {
            return txvFechaPago;
        }

        /**
         * Se llama cuando se hace clic en la vista del elemento. Este método notifica al
         * {@link ItemClickListener} del evento de clic.
         * @param view La {@link View} en la que se hizo clic.
         */
        @Override
        public void onClick(View view) {
            if (clickListener != null) { // Check for null to avoid exception
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }

    /**
     * Constructor para {@code PedidosAdapter}.
     * @param dataSet El {@link ArrayList} de objetos {@link CabeceraPedido} para mostrar.
     * @param context El {@link Context} de la aplicación.
     */
    public PedidosAdapter(ArrayList<CabeceraPedido> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    /**
     * Establece el {@link ItemClickListener} que se notificará de los eventos de clic en los elementos.
     * @param clickListener El {@link ItemClickListener} que se va a establecer.
     */
    public void setOnClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * Crea un nuevo {@code ViewHolder} e infla el diseño del elemento.
     * @param viewGroup El {@link ViewGroup} en el que se agregará la nueva {@link View} después de que se haya vinculado a los datos.
     * @param viewType El tipo de vista de la nueva {@link View}.
     * @return Un nuevo {@code ViewHolder} que contiene una {@link View} del diseño del elemento.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_cabeceras_pedido, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * Vincula los datos a las vistas dentro del {@code ViewHolder}.
     * @param viewHolder El {@code ViewHolder} al que se van a vincular los datos.
     * @param position La posición del elemento dentro del conjunto de datos.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        CabeceraPedido cabeceraPedido = dataSet.get(position); // Get the CabeceraPedido object

        int numeroPedido = cabeceraPedido.getId();
        String fechaPedido = reformatearFecha(cabeceraPedido.getFechaPedido());
        String fechaPago = reformatearFecha(cabeceraPedido.getFechaPago());

        viewHolder.getTxvNumeroPedido().setText(String.valueOf(numeroPedido));
        viewHolder.getTxvFechaPedido().setText(fechaPedido);
        viewHolder.getTxvFechaPago().setText(fechaPago);
    }

    /**
     * Devuelve el número de elementos en el conjunto de datos.
     * @return El número de elementos en el conjunto de datos.
     */
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    /**
     * Reformatea una fecha del formato AAAA-MM-DD al formato DD-MM-AAAA.
     * @param fecha La fecha en formato AAAA-MM-DD.
     * @return La fecha en formato DD-MM-AAAA.
     */
    private String reformatearFecha(String fecha) {
        String fechaSalida;
        String[] partes = fecha.split("-");

        fechaSalida = partes[2] + "-" + partes[1] + "-" + partes[0];

        return fechaSalida;
    }
}