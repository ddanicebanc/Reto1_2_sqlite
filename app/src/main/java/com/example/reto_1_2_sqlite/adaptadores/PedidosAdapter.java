package com.example.reto_1_2_sqlite.adaptadores;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            txvNumeroPedido = view.findViewById(R.id.)
        }
    }
}
