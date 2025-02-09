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

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolder> {
    public interface ItemClickListener {
        public void onClick(View view, int position);
    }

    private ArrayList<CabeceraPedido> dataSet;
    private ItemClickListener clickListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txvNumeroPedido;
        private final TextView txvFechaPedido;
        private final TextView txvFechaPago;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            txvNumeroPedido = view.findViewById(R.id.txvNumeroPedido);
            txvFechaPedido = view.findViewById(R.id.txvFechaPedido);
            txvFechaPago = view.findViewById(R.id.txvFechaPago);
        }

        public TextView getTxvNumeroPedido () {return txvNumeroPedido;}
        public TextView getTxvFechaPedido () {return txvFechaPedido;}
        public TextView getTxvFechaPago () {return txvFechaPago;}

        @Override
        public void onClick (View view) {clickListener.onClick(view, getAdapterPosition());}
    }

    public PedidosAdapter (ArrayList<CabeceraPedido> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    public void setOnClickListener (ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_cabeceras_pedido, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (ViewHolder viewHolder, final int position) {
        int numeroPedido = dataSet.get(position).getId();
        String fechaPedido = reformatearFecha(dataSet.get(position).getFechaPedido());
        String fechaPago = reformatearFecha(dataSet.get(position).getFechaPago());

        viewHolder.getTxvNumeroPedido().setText(String.valueOf(numeroPedido));
        viewHolder.getTxvFechaPedido().setText(fechaPedido);
        viewHolder.getTxvFechaPago().setText(fechaPago);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private String reformatearFecha (String fecha) {
        String fechaSalida;
        String[] partes = fecha.split("-");

        fechaSalida = partes[2] + "-" + partes[1] + "-" + partes[0];

        return fechaSalida;
    }
}
